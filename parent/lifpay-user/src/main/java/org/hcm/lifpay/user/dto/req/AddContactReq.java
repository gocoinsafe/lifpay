package org.hcm.lifpay.user.dto.req;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;
import org.hcm.lifpay.common.BaseResponse;

@Data
public class AddContactReq extends BaseRequest {

    @Schema(description = "联系人Id，创建时不传")
    private Long id;

    @Schema(description = "联系人姓名")
    private String contactName;


    @Schema(description = "Lightning地址")
    private String address;


    @Schema(description = "联系人邮箱")
    private String email;

    @Schema(description = "nostr账号")
    private String nostr;

    @Schema(description = "备注")
    private String note;

}
