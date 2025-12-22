package org.hcm.lifpay.user.dao.entity;


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
@TableName("t_user_transaction")
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
     * 交易哈希（全局唯一）
     */

    @TableField("transaction_hash")
    private String transactionHash;

    /**
     * 转账地址
     */

    @TableField("from_address")
    private String fromAddress;

    /**
     * 收款地址
     */

    @TableField("to_address")
    private String toAddress;

    /**
     * 交易金额
     */

    @TableField("amount")
    private BigDecimal amount;

    /**
     * 交易手续费
     */

    @TableField("fee")
    private BigDecimal fee;

    /**
     * 订单状态：0-待处理 1-交易成功 2-交易失败 3-已撤销
     */

    @TableField("status")
    private Integer status;



}
