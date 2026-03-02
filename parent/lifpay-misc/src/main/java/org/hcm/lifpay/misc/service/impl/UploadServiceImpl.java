package org.hcm.lifpay.misc.service.impl;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.dto.resp.TencentFileModel;
import org.hcm.lifpay.misc.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
    public BaseResponse<TencentFileModel> uploadFile(MultipartFile file, Integer sceneFlag) throws Exception {
        // 优化：打印文件关键信息，而非整个对象
        logger.info("UploadServiceImpl.uploadFile - 文件名：{}, 文件大小：{}Bytes, 场景标识：{}",
                file.getOriginalFilename(), file.getSize(), sceneFlag);

        BaseResponse<TencentFileModel> response = new BaseResponse();
        TencentFileModel tencentFileModel = new TencentFileModel();
        tencentFileModel.setFileName(file.getOriginalFilename());
        tencentFileModel.setFileSize(file.getSize());

        // 1. 校验文件是否为空
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传的文件不能为空");
        }

        // 2. 校验文件大小
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("文件大小不能超过10MB");
        }

        // 3. 校验文件格式
        String originalFilename = file.getOriginalFilename();
        String extension = FileUtil.extName(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("仅支持上传jpg、jpeg、png、webp格式的文件");
        }

        // 4. 生成唯一文件名 + 按场景分目录（核心优化）
        // 4.1 按sceneFlag区分存储目录，避免根目录混乱
        String sceneDir = getSceneDirByFlag(sceneFlag);
        // 4.2 生成无"-"的UUID，缩短URL
        String uniqueId = UUID.randomUUID().toString().replace("-", "");
        // 4.3 最终COS存储路径：场景目录/唯一ID.后缀
        String cosFilePath = StrUtil.join("/", sceneDir, uniqueId + "." + extension);

        // 5. 将MultipartFile转为File（临时文件）
        File tempFile = null;
        try {
            tempFile = File.createTempFile("upload_", "." + extension);
            file.transferTo(tempFile);

            String bucketName = bucket + "-" + sdkAppId;
            // 6. 上传文件到COS
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, cosFilePath, tempFile);
            PutObjectResult result = cosClient.putObject(putObjectRequest);

            // 7. 拼接文件访问URL（核心修复：解决硬编码分隔符问题）
            // 使用StrUtil.join自动处理分隔符，避免双斜杠
            String fileAccessUrl = StrUtil.join("/", cosBaseUrl, cosFilePath);
            tencentFileModel.setFilePath(fileAccessUrl);

            // 8. 完善响应信息（核心补充：设置成功状态）
            response.setCode(200); // 假设200是成功码，根据你的BaseResponse定义调整
            response.setMessage("文件上传成功");
            response.setData(tencentFileModel);
        } catch (CosServiceException e) {
            logger.error("COS服务端上传失败 - 存储桶：{}, 文件路径：{}", bucket + "-" + sdkAppId, cosFilePath, e);
            throw new Exception("COS服务端错误：" + e.getMessage(), e);
        } catch (CosClientException e) {
            logger.error("COS客户端上传失败 - 网络/客户端异常", e);
            throw new Exception("COS客户端上传失败：" + e.getMessage(), e);
        } catch (IOException e) {
            logger.error("临时文件处理失败", e);
            throw new Exception("文件处理失败：" + e.getMessage(), e);
        } finally {
            // 删除临时文件
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

}