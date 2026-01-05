package org.hcm.lifpay.user.dto.resp;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateBoltCardResp {



    @ApiModelProperty(value = "授权密钥")
    private String k0;


    @ApiModelProperty(value = "AES 密钥1")
    private String k1;

    @ApiModelProperty(value = "AES 密钥2")
    private String k2;

    @ApiModelProperty(value = "等于 k1")
    private String k3;

    @ApiModelProperty(value = "等于 k2")
    private String k4;



    @ApiModelProperty(value = "boltcard_id")
    private String id;

    @ApiModelProperty(value = "等于 k1")
    private String uid;

    @ApiModelProperty(value = "card_name")
    private String card_name;


    @ApiModelProperty(value = "等于 k1")
    private String lnurlwBase;

    @ApiModelProperty(value = "protocol_name")
    private String protocolName;

    @ApiModelProperty(value = "protocol_version")
    private String protocolVersion;

    @ApiModelProperty(value = "如果 privacy=1 则为 \"Y\"")
    private String uid_privacy;

}
