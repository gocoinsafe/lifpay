package org.hcm.lifpay.misc.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.dto.req.UploadFileReq;
import org.hcm.lifpay.misc.dto.resp.TencentFileModel;
import org.hcm.lifpay.misc.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@Api(tags = "REST - 上传文件相关")
@RestController
@RequestMapping("/api/misc")
@Slf4j
public class UploadController {


    @Autowired
    UploadService service;


    /**
     * 上传文件的公共接口  <br/>
     *
     * @param req
     * @param
     * @Author jerry Song
     * @Date
     */
    @PostMapping(value = "/upload")
    @Operation(summary = "上传文件")
    public BaseResponse<TencentFileModel> upload(@RequestBody UploadFileReq req) {
        log.info("upload.file:{}",req.getFileName());
        try {
            return service.uploadFile(req);
        }catch (Exception e){
            log.error("upload.error:{}",e.getMessage());
        }
        return null;
    }







}