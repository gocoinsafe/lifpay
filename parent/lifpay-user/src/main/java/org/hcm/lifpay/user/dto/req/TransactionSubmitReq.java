package org.hcm.lifpay.user.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hcm.lifpay.common.BaseRequest;

import java.math.BigDecimal;

@Data
public class TransactionSubmitReq extends BaseRequest {


    @ApiModelProperty(value = "交易哈希（全局唯一）")
    String transactionHash;

    @ApiModelProperty(value = "转账地址")
    String fromAddress;

    @ApiModelProperty(value = "收款地址")
    String toAddress;

    @ApiModelProperty(value = "交易金额")
    BigDecimal amount;

    @ApiModelProperty(value = "交易手续费")
    BigDecimal fee;

    @ApiModelProperty(value = "订单状态：0-待处理 1-交易成功 2-交易失败 3-已撤销")
    Integer status;



}
