package org.hcm.lifpay.user.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;

@Data
public class AddContactReq extends BaseRequest {

    @ApiModelProperty(value = "联系人Id，创建时不传")
    private Long id;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;


    @ApiModelProperty(value = "Lightning地址")
    private String address;


    @ApiModelProperty(value = "联系人邮箱")
    private String email;

    @ApiModelProperty(value = "nostr账号")
    private String nostr;

    @ApiModelProperty(value = "备注")
    private String note;

}
