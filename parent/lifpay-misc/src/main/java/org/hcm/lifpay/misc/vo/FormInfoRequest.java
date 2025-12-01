package org.hcm.lifpay.misc.vo;


import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 表单提交请求
 */
@Data
public class FormInfoRequest {
    /**
     * 名字
     */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 公司名称
     */
    private String company;

    /**
     * Email
     */
    @NotBlank(message = "邮件")
    private String email;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 是否定制：0=否, 1=是
     */
    private Integer isCustom;

    /**
     * 消息
     */
    private String message;


    /**
     * 国家
     */
    private String country;

    /**
     * 邮政编码
     */
    private String postCode;

    /**
     * hp 有值：判定为机器人
     */
    private String hp;

    /**
     * 时间戳 有值：判定为机器人
     */
    private Long ts;



}
