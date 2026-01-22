package org.hcm.lifpay.user.dto.req;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

import java.math.BigDecimal;

@Data
public class TransactionSubmitReq extends BaseRequest {


    @Schema(description = "交易哈希（全局唯一）")
    String transactionHash;

    @Schema(description = "转账地址")
    String fromAddress;

    @Schema(description = "收款地址")
    String toAddress;

    @Schema(description = "交易金额")
    BigDecimal amount;

    @Schema(description = "交易手续费")
    BigDecimal fee;

    @Schema(description = "订单状态：0-待处理 1-交易成功 2-交易失败 3-已撤销")
    Integer status;



}
