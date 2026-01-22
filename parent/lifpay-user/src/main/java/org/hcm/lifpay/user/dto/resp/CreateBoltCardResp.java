package org.hcm.lifpay.user.dto.resp;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.user.dao.entity.BoltCardDo;

@Data
public class CreateBoltCardResp {



    @Schema(description = "授权密钥")
    private String k0;


    @Schema(description = "AES 密钥1")
    private String k1;

    @Schema(description = "AES 密钥2")
    private String k2;

    @Schema(description = "等于 k1")
    private String k3;

    @Schema(description = "等于 k2")
    private String k4;



    @Schema(description = "boltcard_id")
    private Long id;

    @Schema(description = "card id")
    private String uid;

    @Schema(description = "card_name")
    private String cardName;


    @Schema(description = "lifpay.com")
    private String lnurlwBase;

    @Schema(description = "protocol_name")
    private String protocolName;

    @Schema(description = "protocol_version")
    private String protocolVersion;

    @Schema(description = "如果 privacy=1 则为 \"Y\"")
    private String uidPrivacy;

    public CreateBoltCardResp(){

    }
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
