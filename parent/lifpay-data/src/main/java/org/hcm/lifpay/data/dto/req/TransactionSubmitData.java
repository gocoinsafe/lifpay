package org.hcm.lifpay.data.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class TransactionSubmitData {


    /**
     * 用户ID（必填）
     */

    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户公钥
     */
    @Schema(description = "用户公钥", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userPrimaryKey;

    /**
     * 外部交易订单ID（必填）
     */
    @NotBlank(message = "外部交易订单ID不能为空")
    @Schema(description = "外部交易订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tradeId;

    /**
     * 交易哈希（64位，必填）
     */
    @NotBlank(message = "交易哈希不能为空")
    @Schema(description = "交易哈希", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tradeHash;

    /**
     * 交易金额（sats，必填，正数）
     */
    @NotNull(message = "交易金额不能为空")
    @Positive(message = "交易金额必须为正数")
    @Schema(description = "交易金额(单位：sats，聪)", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long amount;

    /**
     * 交易手续费（sats）
     */
    @Schema(description = "交易手续费(单位：sats，聪)")
    private String fees;

    /**
     * 支付方式：1=lightning，2=其他（必填）
     */
    @NotNull(message = "支付方式不能为空")
    @Schema(description = "支付方式：lightning, spark, token, deposit, withdraw, unknown", requiredMode = Schema.RequiredMode.REQUIRED)
    private String method;

    /**
     * 交易类型：1=发送，2=接收（必填）
     */
    @NotNull(message = "交易类型不能为空")
    @Schema(description = "交易类型：send, receive", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tradeType;

    /**
     * 支付状态：1=完成，2=处理中，3=失败（必填）
     */
    @NotNull(message = "支付状态不能为空")
    @Schema(description = "支付状态：completed, pending, failed", requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

    /**
     * 法币类型（如USD/CNY）
     */
    @Schema(description = "法币类型（如USD/CNY）")
    private String fiatCurrency;

    /**
     * 法币金额
     */
    @Schema(description = "法币金额")
    private BigDecimal fiatPrice;

    /**
     * 交易预镜像（64位）
     */
    @Schema(description = "交易预镜像（固定64位）")
    private String preimage;

    /**
     * 闪电网络发票
     */
    @Schema(description = "闪电网络发票")
    private String invoice;

    /**
     * 收款方公钥
     */
    @Schema(description = "收款方公钥")
    private String payeePubkey;

    /**
     * 交易描述
     */
    @Schema(description = "交易描述")
    private String remark;

    /**
     * 交易时间（毫秒级时间戳，必填）
     */
    @NotNull(message = "交易时间不能为空")
    @Schema(description = "交易时间（毫秒级时间戳）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long tradeTime;

}
