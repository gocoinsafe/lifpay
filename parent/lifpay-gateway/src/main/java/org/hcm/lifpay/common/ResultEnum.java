package org.hcm.lifpay.common;


/**
 * 该枚举类是 处理在网关层发现错误的请求后，给出提示的信息
 *
 * @author xinzhe
 */
public enum ResultEnum {
    /**
     * 成功
     */
    SUCCESS(0, "success"),
    /**
     * 未认证（签名错误）
     */
    UNAUTHORIZED(401, "未认证（签名错误）"),
    /**
     * 接口不存在
     */
    NOT_FOUND(404, "接口不存在,查找不到数据"),
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    /**
     * 参数异常
     */
    DEFECT(400, "参数异常");

    private int code;
    private Object data;
    private String msg;

    private ResultEnum(int code, String msg) {
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
