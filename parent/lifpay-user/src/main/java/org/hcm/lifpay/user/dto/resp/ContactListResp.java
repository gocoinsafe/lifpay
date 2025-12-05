package org.hcm.lifpay.user.dto.resp;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ContactListResp {


    @ApiModelProperty(value = "联系人姓名")
    String contactName;


    @ApiModelProperty(value = "Lightning地址")
    String address;


    @ApiModelProperty(value = "联系人邮箱")
    String email;

    @ApiModelProperty(value = "nostr账号")
    String nostr;

    @ApiModelProperty(value = "备注")
    String note;




}
