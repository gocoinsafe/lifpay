package org.hcm.lifpay.user.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @program: aa
 * @description: 协议查询返回内容
 * @author: xinzhe
 * @create: 2021-12-09 14:55
 **/
@Data
public class AgreementQueryDto {

    @Schema(description = "协议名称")
    private String name;

    @Schema(description = "协议地址")
    private String url;

    @Schema(description = "path路径")
    private String path;

    @Schema(description = "协议版本")
    private String agreementVersion;

    @Schema(description = "协议类型")
    private String type;

    @Schema(description = "是否已同意(协议列表获取时忽略此字段)")
    private boolean isAgree;

}
