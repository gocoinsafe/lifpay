package org.hcm.lifpay.data.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;


@Data
public class TransactionListReq extends BaseRequest {


    @Schema(description = "用户公钥")
    String userPrimaryKey;


    @Schema(description = "页码")
    Long pageNo;

    @Schema(description = "页数")
    Long pageSize;


}
