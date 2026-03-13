package org.hcm.lifpay.misc.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.enums.FileContentTypeEnum;

import javax.validation.constraints.NotBlank;

@Data
public class UploadFileReq extends BaseRequest {


    @Schema(description = "文件内容(Base64)")
    private String fileBase64Str;


    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "类型")
    private FileContentTypeEnum contentType;


    @Schema(description = "1: 头像 2：其他")
    private Integer sceneFlag;


}
