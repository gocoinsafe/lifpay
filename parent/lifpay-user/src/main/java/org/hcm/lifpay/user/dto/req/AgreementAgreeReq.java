package org.hcm.lifpay.user.dto.req;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

/**
 * @program: aa
 * @description: 协议同意请求
 * @author: xinzhe
 * @create: 2021-12-09 15:16
 **/
@Data
public class AgreementAgreeReq extends BaseRequest {

    @Schema(description = "协议类型:0- 隐私协议，1- 用户协议 2-服务协议 3-其他")
    private Integer type;

    @Schema(description = "协议版本")
    private String agreementVersion;
}
