package org.hcm.lifpay.misc.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.enums.FileContentTypeEnum;


@Data
public class UploadFileReq extends BaseRequest {


    @Schema(description = "文件内容(Base64)",requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileBase64Str;


    @Schema(description = "文件名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;

    @Schema(description = "类型",requiredMode = Schema.RequiredMode.REQUIRED)
    private FileContentTypeEnum contentType;


    @Schema(description = "1: 头像 2：其他")
    private Integer sceneFlag;


}
