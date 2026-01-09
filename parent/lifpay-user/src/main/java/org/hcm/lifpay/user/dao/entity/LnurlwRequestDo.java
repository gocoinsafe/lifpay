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
 * LNURL-withdraw 请求表（BoltCard 支付会话状态表）
 * </p>
 *
 * @author Erwin Feng
 * @since 2026-01-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_lnurlw_request")
public class LnurlwRequestDo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * LNURLw 请求唯一ID（服务端生成，用于日志/追踪）
     */

    @TableField("request_id")
    private String requestId;

    /**
     * 关联t_bolt_card.id（BoltCard主键）
     */

    @TableField("card_id")
    private Long cardId;

    /**
     * 用户ID
     */

    @TableField("user_id")
    private Long userId;

    /**
     * LNURLw challenge（服务端随机生成，回调校验/防重放，全局唯一）
     */

    @TableField("k1")
    private String k1;

    /**
     * LNURLw callback url（钱包回调地址）
     */

    @TableField("callback_url")
    private String callbackUrl;

    /**
     * 最小可提取金额（单位：satoshis）
     */

    @TableField("min_amount")
    private BigDecimal minAmount;

    /**
     * 最大可提取金额（LNURLw maxWithdrawable，单位：satoshis）
     */

    @TableField("max_amount")
    private BigDecimal maxAmount;

    /**
     * 钱包（Breez SDK）生成的 Lightning Invoice（callback 时上送）
     */

    @TableField("invoice")
    private String invoice;

    /**
     * Invoice 中解析出的实际支付金额（单位：satoshis）
     */

    @TableField("invoice_amount")
    private BigDecimal invoiceAmount;

    /**
     * Lightning payment hash（Breez返回，用于对账/查支付状态）
     */

    @TableField("payment_hash")
    private String paymentHash;

    /**
     * 状态：
     0-INIT(已生成LNURL，待回调)
     1-CALLBACK_RECEIVED(收到invoice，待支付)
     2-PAID(支付成功)
     3-FAILED(支付失败)
     4-EXPIRED(链接过期)
     */

    @TableField("status")
    private Boolean status;

    /**
     * 失败原因：余额不足/invoice非法/Breez SDK错误/防重放校验失败等
     */

    @TableField("fail_reason")
    private String failReason;

    /**
     * NFC读取的BoltCard counter（防重放攻击，需匹配卡端counter）
     */

    @TableField("card_counter")
    private Integer cardCounter;

    /**
     * 请求来源IP（支持IPv4/IPv6，风控&审计）
     */

    @TableField("client_ip")
    private String clientIp;

    /**
     * LNURLw 支付过期时间戳（毫秒级）
     */

    @TableField("expire_at")
    private Long expireAt;

}

