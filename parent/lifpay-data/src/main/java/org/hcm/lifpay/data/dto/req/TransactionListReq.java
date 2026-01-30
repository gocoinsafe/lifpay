package org.hcm.lifpay.data.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;


@Data
public class TransactionListReq extends BaseRequest {

    @Schema(description = "页码")
    Integer pageNo;

    @Schema(description = "页数")
    Integer pageSize;


}
