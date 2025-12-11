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
 * 国家区域表
 * </p>
 *
 * @author Erwin Feng
 * @since 2025-12-11
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_country_list")
public class CountryListDo implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 中文名称
     */

    @TableField("cn_name")
    private String cnName;

    /**
     * 英文名称
     */

    @TableField("en_name")
    private String enName;

    /**
     * 简称
     */

    @TableField("abbreviation")
    private String abbreviation;

    /**
     * 区域代码
     */

    @TableField("code")
    private String code;

    /**
     * 国家图标logo
     */

    @TableField("icon")
    private String icon;

    /**
     * 状态
     */

    @TableField("status")
    private String status;

}
