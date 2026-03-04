package org.hcm.lifpay.data.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hcm.lifpay.data.dao.entity.UserTransactionDo;

import java.math.BigDecimal;

@Data
public class TransactionListResp {


    @Schema(description = "id")
    Long id;

    @Schema(description = "user id")
    Long userId;

    @Schema(description = "用户公钥")
    String userPrimaryKey;

    @Schema(description = "外部交易订单的id号")
    String tradeId;

    @Schema(description = "交易哈希（全局唯一）")
    String tradeHash;



    @Schema(description = "交易金额")
    Long amount;

    @Schema(description = "交易手续费")
    String fee;

    @Schema(description = "支付方式：1:lightning,2:其他")
    Integer method;

    @Schema(description = "交易类型：1:发送，2:接收")
    Integer tradeType;

    @Schema(description = "订单状态：0-待处理 1-交易成功 2-交易失败 3-已撤销")
    Integer status;

    @Schema(description = "法币（如USD/CNY）")
    String fiatCurrency;

    @Schema(description = "法币金额")
    String fiatPrice;

    @Schema(description = "交易时间（毫秒级时间戳）")
    Long tradeTime;

    @Schema(description = "交易描述")
    String remark;

    @Schema(description = "闪电网络发票(invoice字符串)")
    String invoice;

    @Schema(description = "创建时间")
    Long createTime;

    @Schema(description = "更新时间")
    Long updateTime;


    public TransactionListResp(){

    }

    public TransactionListResp(UserTransactionDo data){
        this.setId(data.getId());
        this.setUserId(data.getUserId());
        this.setUserPrimaryKey(data.getUserPrimaryKey());
        this.setTradeId(data.getTradeId());
        this.setTradeHash(data.getTradeHash());
        this.setAmount(data.getAmount());
        this.setFee(data.getFees());
        this.setMethod(data.getMethod());
        this.setTradeType(data.getTradeType());
        this.setStatus(data.getStatus());
        this.setFiatCurrency(data.getFiatCurrency());
        if (data != null) {
            // 先获取BigDecimal类型的法币金额
            BigDecimal fiatPriceBig = data.getFiatPrice();
            // 若为null则赋值为null（或空字符串，按业务需求），否则转String
            this.setFiatPrice(fiatPriceBig != null ? fiatPriceBig.toString() : null);
        } else {
            // data为null时，直接赋值为null（或根据业务设置默认值，比如""）
            this.setFiatPrice(null);
        }
        this.setTradeTime(data.getTradeTime());
        this.setRemark(data.getRemark());
        this.setInvoice(data.getInvoice());

        this.setCreateTime(data.getCreateTime());
        this.setUpdateTime(data.getUpdateTime());
    }

}
