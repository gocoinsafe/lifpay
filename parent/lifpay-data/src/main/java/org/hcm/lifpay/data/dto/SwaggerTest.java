package org.hcm.lifpay.data.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "验证码请求参数")
public class SwaggerTest extends BaseRequest {

    @Schema(description = "联系方式：邮箱/手机号", required = true)
    @NotNull
    @NotBlank
    String contact;

    @Schema(description = "消息类型：0-邮箱，1-手机号")
    Integer type;

    @Schema(description = "区域")
    private String area;

    @Schema(description = "随机数", required = true)
    @NotNull
    private Integer random;
}
