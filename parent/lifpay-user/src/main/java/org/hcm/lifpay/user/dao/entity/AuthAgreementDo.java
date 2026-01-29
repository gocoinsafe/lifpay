package org.hcm.lifpay.user.dao.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hcm.lifpay.common.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户授权协议表
 * </p>
 *
 * @author Erwin Feng
 * @since 2026-01-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_auth_agreement")
public class AuthAgreementDo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户协议id
     */

    @TableField("service_terms_id")
    private Long serviceTermsId;

    /**
     * 用户id
     */

    @TableField("user_id")
    private Long userId;

    /**
     * 是否同意:0-未同意,1-同意
     */

    @TableField("is_agree")
    private Integer isAgree;

    /**
     * 设备id
     */

    @TableField("device_id")
    private String deviceId;

}
