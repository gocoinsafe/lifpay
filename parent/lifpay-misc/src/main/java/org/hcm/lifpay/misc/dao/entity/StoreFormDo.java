package org.hcm.lifpay.misc.dao.entity;

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
 * lifpay商城表单
 * </p>
 *
 * @author Erwin Feng
 * @since 2025-11-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("s_store_form")
public class StoreFormDo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名字
     */

    @TableField("name")
    private String name;

    /**
     * email
     */

    @TableField("email")
    private String email;

    /**
     * 公司
     */

    @TableField("company")
    private String company;

    /**
     * 联系方式
     */

    @TableField("contact")
    private String contact;

    /**
     * 是否定制：0=否, 1=是
     */

    @TableField("is_custom")
    private Integer isCustom;

    /**
     * 内容
     */

    @TableField("message")
    private String message;

    /**
     * 状态：0=未处理, 1=已处理
     */

    @TableField("status")
    private Integer status;

    /**
     * 身份:0:guest,1:系统用户,2:其他
     */

    @TableField("identity")
    private Integer identity;

    /**
     * 国家
     */

    @TableField("country")
    private String country;

    /**
     * 邮政编码
     */

    @TableField("post_code")
    private String postCode;

    /**
     * 备注
     */

    @TableField("remark")
    private String remark;


}
