package org.hcm.lifpay.user.dto.resp;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoResp {


    @ApiModelProperty(value = "用户id")
    Long userId;

    @ApiModelProperty(value = "用户名")
    String username;

    @ApiModelProperty(value = "手机号")
    private String telephone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头像URL")
    private String iconUrl;

    @ApiModelProperty(value = "状态：0正常，1冻结，2注销，3未激活")
    private Integer status;

    @ApiModelProperty(value = "用户类型：1个人，2企业")
    private Integer userType;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;
}
