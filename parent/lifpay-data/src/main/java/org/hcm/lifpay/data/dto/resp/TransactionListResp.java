package org.hcm.lifpay.data.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionListResp {


    @Schema(description = "id")
    Long id;

    @Schema(description = "user id")
    Long userId;

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

    @Schema(description = "创建时间")
    Long createTime;

    @Schema(description = "更新时间")
    Long updateTime;


}
