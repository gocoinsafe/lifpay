package org.hcm.lifpay.misc.dto.resp;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TencentFileModel {


    @Schema(description = "文件大小")
    private long fileSize;

    @Schema(description = "文件名称")
    private String fileName;

    @Schema(description = "文件URL")
    private String url;

    @Schema(description = "云存储中的路径")
    private String filePath;

    @Schema(description = "文件类型")
    private String fileType;


}