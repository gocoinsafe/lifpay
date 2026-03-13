//package org.hcm.lifpay.misc.service;
//
//import com.alibaba.fastjson.JSON;
//import lombok.extern.slf4j.Slf4j;
//import org.hcm.lifpay.common.BaseResponse;
//import org.hcm.lifpay.common.enums.FileContentTypeEnum;
//import org.hcm.lifpay.misc.dto.req.UploadFileReq;
//import org.hcm.lifpay.misc.dto.resp.TencentFileModel;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.junit.Ignore;
//import org.junit.runner.RunWith;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Base64;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ActiveProfiles("junit")
//@Slf4j
//class UploadServiceTest {
//
//
//    @Autowired
//    UploadService uploadFile;
//
//    @Test
//    @Ignore
//    public void uploadFileTest() throws Exception {
//        String fileBase64Str = getFileBase64File("/Users/dev/Documents/2dhuihua.jpg");
//        UploadFileReq uploadFileReq = new UploadFileReq();
//        uploadFileReq.setFileBase64Str(fileBase64Str);
//        uploadFileReq.setFileName("2dhuihua.jpg");
//        uploadFileReq.setContentType(FileContentTypeEnum.JPG);
//
//
//        BaseResponse<TencentFileModel> fileUploadDto = uploadFile.uploadFile(uploadFileReq);
//        log.info(JSON.toJSONString(fileUploadDto));
//
////        fileBase64Str = getFileBase64File("D:\\test.jpg");
////        fileUploadDto = minioFileStoreService.uploadFile("test.jpg", fileBase64Str,
////                FileContentTypeEnum.JPG.getDesc());
////        log.info(JSON.toJSONString(fileUploadDto));
////
////        fileBase64Str = getFileBase64File("D:\\test.jpeg");
////        fileUploadDto = minioFileStoreService.uploadFile("test.jpeg", fileBase64Str,
////                FileContentTypeEnum.JPEG.getDesc());
////        log.info(JSON.toJSONString(fileUploadDto));
////
////        fileBase64Str = getFileBase64File("D:\\test.bmp");
////        fileUploadDto = minioFileStoreService.uploadFile("test.bmp", fileBase64Str,
////                FileContentTypeEnum.BMP.getDesc());
//        log.info(JSON.toJSONString(fileUploadDto));
//    }
//
//
//
//    public String getFileBase64File(String filePath) throws IOException {
//        File file = new File(filePath);
//        int fileLen = (int) file.length();
//        byte[] buff = new byte[fileLen];
//        FileInputStream fileInputStream = new FileInputStream(file);
//        fileInputStream.read(buff, 0, fileLen);
//        return Base64.getEncoder().encodeToString(buff);
//    }
//
//}