package org.hcm.lifpay.user.dto.resp;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hcm.lifpay.user.dao.entity.BoltCardDo;

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
    private Long id;

    @ApiModelProperty(value = "card id")
    private String uid;

    @ApiModelProperty(value = "card_name")
    private String cardName;


    @ApiModelProperty(value = "lifpay.com")
    private String lnurlwBase;

    @ApiModelProperty(value = "protocol_name")
    private String protocolName;

    @ApiModelProperty(value = "protocol_version")
    private String protocolVersion;

    @ApiModelProperty(value = "如果 privacy=1 则为 \"Y\"")
    private String uidPrivacy;


    public CreateBoltCardResp(BoltCardDo boltCardDo){
        this.k0 = boltCardDo.getK0();
        this.k1 = boltCardDo.getK1();
        this.k2 = boltCardDo.getK2();
        this.k3 = boltCardDo.getK1();
        this.k4 = boltCardDo.getK2();

        this.id = boltCardDo.getId();
        this.uid = boltCardDo.getCardUid();
        this.cardName = boltCardDo.getCardName();
        this.lnurlwBase = "lnurlw://card.lifpay.com/ln";
        if (null != boltCardDo.getPrivacy()){
            this.uidPrivacy = boltCardDo.getPrivacy().toString();
        }
    }


}
