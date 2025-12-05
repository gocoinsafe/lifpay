package org.hcm.lifpay.user.dao.entity;


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
 * 联系人表
 * </p>
 *
 * @author Erwin Feng
 * @since 2025-12-05
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_user_contact")
public class UserContactDo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID（关联t_user_info.id）
     */

    @TableField("user_id")
    private Long userId;

    /**
     * 联系人姓名
     */

    @TableField("contact_name")
    private String contactName;

    /**
     * Lightning地址
     */

    @TableField("address")
    private String address;

    /**
     * 邮箱
     */

    @TableField("email")
    private String email;

    /**
     * Nostr社交账号
     */

    @TableField("nostr")
    private String nostr;

    /**
     * 备注说明
     */

    @TableField("note")
    private String note;

    /**
     * 状态：0正常，1删除
     */

    @TableField("status")
    private Integer status;



}
