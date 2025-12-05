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

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Erwin Feng
 * @since 2025-12-05
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_user_info")
public class UserInfoDo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 昵称
     */

    @TableField("name")
    private String name;

    /**
     * 手机号
     */

    @TableField("telephone")
    private String telephone;

    /**
     * 邮箱
     */

    @TableField("email")
    private String email;

    /**
     * 登录用户名
     */

    @TableField("login_name")
    private String loginName;

    /**
     * 登录密码（密文存储）
     */

    @TableField("password")
    private String password;

    /**
     * 头像URL
     */

    @TableField("icon_url")
    private String iconUrl;

    /**
     * 状态：0正常，1冻结，2注销，3未激活
     */

    @TableField("status")
    private Integer status;

    /**
     * 用户类型：1个人，2企业
     */

    @TableField("user_type")
    private Integer userType;

}
