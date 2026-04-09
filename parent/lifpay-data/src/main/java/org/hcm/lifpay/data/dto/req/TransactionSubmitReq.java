package org.hcm.lifpay.data.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

import java.util.List;

@Data
public class TransactionSubmitReq extends BaseRequest {

    @Schema(description = "交易列表")
    private List<TransactionSubmitData> list;




}
