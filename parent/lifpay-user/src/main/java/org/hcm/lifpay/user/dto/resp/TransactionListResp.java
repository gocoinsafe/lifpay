package org.hcm.lifpay.user.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionListResp {


    @ApiModelProperty(value = "id")
    Long id;

    @ApiModelProperty(value = "user id")
    Long userId;

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

    @ApiModelProperty(value = "创建时间")
    Long createTime;

    @ApiModelProperty(value = "更新时间")
    Long updateTime;


}
