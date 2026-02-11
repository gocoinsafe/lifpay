package org.hcm.lifpay.user.dto;


/**
 * 该枚举类 主要是用来处理User服务模块登录信息的返回结果
 *
 * @author xinzhe
 */
public enum UserResultEnum {

    SUCCESS(200, "success"),
    SYSTEM_INTERNAL_ERROR(9000, "System internal error"),
    BAD_INPUT(9001, "Missing mandatory attributes."),
    BAD_COMBINATION(9002, "Bad account or password."),
    USER_NOT_EXISTS(9003, "用户不存在"),
    USER_PHONE_EXISTS(9004, "手机号已存在"),
    MISS_ROLE_AND_APP(9005, "非应用用户必须指定角色"),
    INVALID_APP(9006, "错误的应用"),
    MISSING_APP_ROLE(9007, "未配置默认角色"),
    ROLE_NOT_EXISTS(9008, "角色不存在"),
    USER_IDENTITY_NOT_EXISTS(9010, "用户身份不存在"),
    INSERT_RECORD_FAIL(9011, "数据保存失败"),
    MSG_REMOTE_SERVICE_ERR(9012, "Message服务调用失败"),
    CONTACT_USER_NAME_ERR(9013, "联系人名称不能为空"),
    CONTACT_ADDRESS_ERR(9014, "联系人钱包地址不能为空"),
    ADD_CONTACT_USER_ERR(9015, "添加联系人异常"),
    DELETE_CONTACT_USER_ERR(9016, "联系人不存在，请确认后再试"),
    DELETE_CONTACT_USER_SUCCESS(9017, "删除联系人成功"),
    DELETE_CONTACT_USER_EXCEPTION_ERR(9018, "删除联系人异常"),
    USER_FROZEN(9019, "用户已被冻结"),
    USER_DELETE(9020, "用户已被注销"),
    AGREEMENT_EXIT(9021,"无效的协议类型"),
    AGREED_REPEATED_AGREED(9022,"该用户已同意过协议，现为反复同意"),



    BOLT_CARD_UID_IS_NULL_ERROR(8000, "Bolt card Uid 不能不为空"),
    BOLT_CARD_ALREADY_BOUND_ERROR(8001, "BoltCard already bound"),






    INVALID_REFRESH_TOKEN(6050, "无效的refresh Token")
    ;

    private int code;
    private Object data;
    private String msg;

    UserResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
