package org.hcm.lifpay.user.dto.resp;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserInfoResp {


    @Schema(description = "用户id")
    Long userId;

    @Schema(description = "用户名")
    String username;

    @Schema(description = "手机号")
    private String telephone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "lightning地址")
    private String lightning;

    @Schema(description = "头像URL")
    private String iconUrl;

    @Schema(description = "状态：0正常，1冻结，2注销，3未激活")
    private Integer status;

    @Schema(description = "用户类型：1个人，2企业")
    private Integer userType;

    @Schema(description = "创建时间")
    private Long createTime;
}
