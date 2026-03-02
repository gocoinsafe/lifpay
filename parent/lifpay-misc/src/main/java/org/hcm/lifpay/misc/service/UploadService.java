package org.hcm.lifpay.misc.service;

import org.hcm.lifpay.common.BaseResponse;
import org.hcm.lifpay.misc.dto.resp.TencentFileModel;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {



    /**
     * 上传头像到COS
     * @param file 前端上传的头像文件
     * @return 头像在COS上的访问URL
     * @throws Exception 上传异常
     */
    BaseResponse<TencentFileModel> uploadFile(MultipartFile file, Integer sceneFlag) throws Exception;

}
