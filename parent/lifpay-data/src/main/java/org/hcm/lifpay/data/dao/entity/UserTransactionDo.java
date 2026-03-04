package org.hcm.lifpay.data.dao.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hcm.lifpay.common.BaseEntity;

/**
 * <p>
 * 交易记录表
 * </p>
 *
 * @author Erwin Feng
 * @since 2025-12-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_transaction")
public class UserTransactionDo extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID
     */

    @TableField("user_id")
    private Long userId;

    /**
     * 用户公钥
     */

    @TableField("user_primary_key")
    private String userPrimaryKey;

    /**
     * 外部交易订单的id号
     */

    @TableField("trade_id")
    private String tradeId;

    /**
     * 交易哈希（固定64位）
     */

    @TableField("trade_hash")
    private String tradeHash;

    /**
     * 交易金额(单位：sats，聪)
     */

    @TableField("amount")
    private Long amount;

    /**
     * 交易手续费(单位：sats，聪)
     */

    @TableField("fees")
    private String fees;

    /**
     * 支付方式：1:lightning,2:其他
     */

    @TableField("method")
    private Integer method;

    /**
     * 交易类型：1:发送，2:接收
     */

    @TableField("trade_type")
    private Integer tradeType;

    /**
     * 支付状态：1:完成2:处理中3:失败
     */

    @TableField("status")
    private Integer status;

    /**
     * 法币（如USD/CNY）
     */

    @TableField("fiat_currency")
    private String fiatCurrency;

    /**
     * 法币金额
     */

    @TableField("fiat_price")
    private BigDecimal fiatPrice;

    /**
     * 交易预镜像（固定64位）
     */

    @TableField("preimage")
    private String preimage;

    /**
     * 闪电网络发票(invoice字符串)
     */

    @TableField("invoice")
    private String invoice;

    /**
     * 收款方公钥
     */

    @TableField("payee_pubkey")
    private String payeePubkey;

    /**
     * 交易描述
     */

    @TableField("remark")
    private String remark;

    /**
     * 交易时间（毫秒级时间戳）
     */

    @TableField("trade_time")
    private Long tradeTime;


}
