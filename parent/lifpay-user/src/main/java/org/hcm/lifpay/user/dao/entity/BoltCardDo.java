package org.hcm.lifpay.user.dao.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hcm.lifpay.common.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * Boltcard表
 * </p>
 *
 * @author Erwin Feng
 * @since 2026-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_bolt_card")
public class BoltCardDo extends BaseEntity implements Serializable {

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
     * bolt cardUUid
     */

    @TableField("card_uid")
    private String cardUid;

    /**
     * bolt card名称
     */

    @TableField("card_name")
    private String cardName;

    /**
     * authroization key
     */

    @TableField("k0")
    private String k0;

    /**
     * AES key1 for decrypt uid and counter
     */

    @TableField("k1")
    private String k1;

    /**
     * AES key2 for CMAC
     */

    @TableField("k2")
    private String k2;

    /**
     * 最新计数器
     */

    @TableField("counter")
    private Integer counter;

    /**
     * 隐私模式：0=禁用，1=启用（可选）
     */

    @TableField("privacy")
    private Integer privacy;

    /**
     * 卡最小支付额度
     */

    @TableField("min_Amount")
    private BigDecimal minAmount;

    /**
     * 卡最大支付额度
     */

    @TableField("max_Amount")
    private BigDecimal maxAmount;

    /**
     * 钱包id
     */

    @TableField("wallet_id")
    private String walletId;

    /**
     * 钱包提供渠道
     */

    @TableField("wallet_provider")
    private String walletProvider;

    /**
     * boltcard状态：0-使用中 1-删除
     */

    @TableField("status")
    private Integer status;

    /**
     * 最新使用时间
     */

    @TableField("last_used_time")
    private Long lastUsedTime;

}