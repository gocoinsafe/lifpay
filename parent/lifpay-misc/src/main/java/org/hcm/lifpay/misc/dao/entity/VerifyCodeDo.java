package org.hcm.lifpay.misc.dao.entity;




import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hcm.lifpay.common.BaseEntity;

/**
 * <p>
 * 短信/邮箱验证码表
 * </p>
 *
 * @author Erwin Feng
 * @since 2025-12-11
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_verify_code")
public class VerifyCodeDo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 联系方式：邮箱/手机号（二选一）
     */

    @TableField("contact")
    private String contact;

    /**
     * 消息类型：0-邮箱，1-手机号
     */

    @TableField("type")
    private Integer type;

    /**
     * 验证码
     */

    @TableField("verify_code")
    private String verifyCode;

    /**
     * 验证码状态：0-正常 1-过期
     */

    @TableField("status")
    private Integer status;

}
