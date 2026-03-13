package org.hcm.lifpay.misc.service.impl;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.dto.req.UploadFileReq;
import org.hcm.lifpay.misc.dto.resp.TencentFileModel;
import org.hcm.lifpay.misc.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RefreshScope
public class UploadServiceImpl implements UploadService {


    private final static Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    // 允许上传的头像格式
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "webp");
    // 头像最大大小：10MB
    private static final long MAX_SIZE = 10 * 1024 * 1024;

    private final COSClient cosClient;



    @Value("${misc.cos.sdkAppId: 1255467455}")
    private String sdkAppId;
    @Value("${misc.cos.bucket: asset}")
    private String bucket;

    @Value("${misc.cos.path:https://img.lifpay.me}")
    private String cosBaseUrl;


    // 构造器注入COS客户端
    public UploadServiceImpl(COSClient cosClient) {
        this.cosClient = cosClient;
    }

    @Override
    public BaseResponse<TencentFileModel> uploadFile(UploadFileReq req) throws Exception {
        // 优化日志：不打印完整Base64（太长），只打印前缀
        logger.info("UploadServiceImpl.uploadFile - 文件名：{}, 场景标识：{}, Base64长度：{}",
                req.getFileName(), req.getSceneFlag(), req.getFileBase64Str().length());

        BaseResponse<TencentFileModel> response = new BaseResponse<>();
        TencentFileModel tencentFileModel = new TencentFileModel();
        tencentFileModel.setFileName(req.getFileName());

        // ===================== 1. 基础校验 =====================
        String base64Str = req.getFileBase64Str();
        if (StrUtil.isEmpty(base64Str)) {
            throw new IllegalArgumentException("上传的文件Base64不能为空");
        }
        if (StrUtil.isEmpty(req.getFileName())) {
            throw new IllegalArgumentException("文件名称不能为空");
        }

        // 校验文件格式
        String extension = FileUtil.extName(req.getFileName()).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("仅支持上传jpg、jpeg、png、webp格式的文件");
        }

        // ===================== 2. Base64解码（核心） =====================
        // 清理Base64前缀（如 data:image/png;base64,）
        String pureBase64 = base64Str.replaceAll("^data:image/\\w+;base64,", "");
        // Base64解码为字节数组
        byte[] fileBytes;
        try {
            fileBytes = Base64.getDecoder().decode(pureBase64);
        } catch (IllegalArgumentException e) {
            logger.error("Base64解码失败", e);
            throw new Exception("文件格式错误，Base64解析失败");
        }

        // 真实文件大小校验（修复：Base64长度≠文件大小）
        long realFileSize = fileBytes.length;
        tencentFileModel.setFileSize(realFileSize);
        if (realFileSize > MAX_SIZE) {
            throw new IllegalArgumentException("文件大小不能超过10MB，当前大小：" + realFileSize / 1024 + "KB");
        }

        // ===================== 3. 生成COS存储路径 =====================
        String sceneDir = getSceneDirByFlag(req.getSceneFlag());
        String uniqueId = UUID.randomUUID().toString().replace("-", "");
        String cosFilePath = StrUtil.join("/", sceneDir, uniqueId + "." + extension);

        // ===================== 4. Base64 → 临时文件 =====================
        File tempFile = null;
        try {
            // 创建临时文件
            tempFile = File.createTempFile("upload_", "." + extension);
            // 将解码后的字节写入临时文件（替代原file.transferTo）
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(fileBytes);
            }

            // ===================== 5. 上传COS =====================
            String bucketName = bucket + "-" + sdkAppId;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, cosFilePath, tempFile);
            cosClient.putObject(putObjectRequest);

            // 拼接访问URL
            String fileAccessUrl = StrUtil.join("/", cosBaseUrl, cosFilePath);
            tencentFileModel.setFilePath(fileAccessUrl);

            // 返回成功
            response.setCode(200);
            response.setMessage("文件上传成功");
            response.setData(tencentFileModel);

        } catch (CosServiceException e) {
            logger.error("COS服务端上传失败 - 存储桶：{}, 文件路径：{}", bucket + "-" + sdkAppId, cosFilePath, e);
            throw new Exception("COS服务错误：" + e.getMessage());
        } catch (CosClientException e) {
            logger.error("COS客户端上传失败", e);
            throw new Exception("网络异常，文件上传失败");
        } catch (IOException e) {
            logger.error("临时文件写入失败", e);
            throw new Exception("文件处理失败");
        } finally {
            // 清理临时文件（不变）
            if (tempFile != null && tempFile.exists()) {
                boolean deleteSuccess = tempFile.delete();
                if (!deleteSuccess) {
                    logger.warn("临时文件删除失败：{}", tempFile.getAbsolutePath());
                }
            }
        }

        return response;
    }

    /**
     * 根据场景标识获取COS存储目录（适配Java 8的传统switch）
     * @param sceneFlag 场景标识（比如1=头像，2=证件照，3=商品图片等）
     * @return 场景目录名
     */
    private String getSceneDirByFlag(Integer sceneFlag) {
        if (sceneFlag == null) {
            return "default"; // 默认目录
        }
        // 替换增强switch为Java 8支持的传统switch
        String sceneDir;
        switch (sceneFlag) {
            case 1:
                sceneDir = "avatar"; // 头像场景
                break;
            case 2:
                sceneDir = "certificate"; // 证件照场景
                break;
            default:
                sceneDir = "default"; // 其他场景
                break;
        }
        return sceneDir;
    }

    public static void main(String[] args) throws IOException{
//        String fileBase64Str = getFileBase64File("D:\\test.png");
//        FileUploadDto fileUploadDto = minioFileStoreService.uploadFile("test.png", fileBase64Str,
//                FileContentTypeEnum.PNG.getDesc());
//        log.info(JSON.toJSONString(fileUploadDto));
//
//        fileBase64Str = getFileBase64File("D:\\test.jpg");
//        fileUploadDto = minioFileStoreService.uploadFile("test.jpg", fileBase64Str,
//                FileContentTypeEnum.JPG.getDesc());
//        log.info(JSON.toJSONString(fileUploadDto));
//
//        fileBase64Str = getFileBase64File("D:\\test.jpeg");
//        fileUploadDto = minioFileStoreService.uploadFile("test.jpeg", fileBase64Str,
//                FileContentTypeEnum.JPEG.getDesc());
//        log.info(JSON.toJSONString(fileUploadDto));
//
//        fileBase64Str = getFileBase64File("D:\\test.bmp");
//        fileUploadDto = minioFileStoreService.uploadFile("test.bmp", fileBase64Str,
//                FileContentTypeEnum.BMP.getDesc());
//        log.info(JSON.toJSONString(fileUploadDto));
    }


    public static String getFileBase64File(String filePath) throws IOException {
        File file = new File(filePath);
        int fileLen = (int) file.length();
        byte[] buff = new byte[fileLen];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(buff, 0, fileLen);
        return Base64.getEncoder().encodeToString(buff);
    }


}