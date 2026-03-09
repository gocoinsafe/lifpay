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
 * 协议表
 * </p>
 *
 * @author Erwin Feng
 * @since 2026-01-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_service_terms")
public class ServiceTermsDo extends BaseEntity implements Serializable  {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 协议名称
     */

    @TableField("name")
    private String name;

    /**
     * 协议类型:0- 隐私协议，1- 用户协议 2-服务协议 3-其他
     */

    @TableField("type")
    private Integer type;

    /**
     * 协议地址
     */

    @TableField("url")
    private String url;


    /**
     * path路径
     */

    @TableField("path")
    private String path;

    /**
     * 版本号
     */

    @TableField("version")
    @Version
    private String version;

    /**
     * 协议状态 0:已停止 1:使用中
     */

    @TableField("status")
    private Integer status;

}
