package org.hcm.lifpay.common;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @Author: jerry
 * @Description lifpay响应基础类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseResponse<T> {
    /**
     * 服务器时间
     */
    private Long serverTime = System.currentTimeMillis();
    private int code;
    private String message;
    private T data;

    public BaseResponse() {
        super();
    }

    public BaseResponse(int code, String msg) {
        this.serverTime = getServerTime();
        this.setCode(code);
        this.setMessage(msg);
    }

    public BaseResponse(int code, String msg, T data) {
        this.serverTime = getServerTime();
        this.setCode(code);
        this.setMessage(msg);
        this.setData(data);
    }


    public static <T> BaseResponse<T> fail(int code, String message) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setData(null);
        baseResponse.setCode(code);
        baseResponse.setMessage(message);
        baseResponse.serverTime = System.currentTimeMillis();
        return baseResponse;
    }

    public static <T> BaseResponse<T> fail(DigitalResultEnum resultEnum) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setData(null);
        baseResponse.setCode(resultEnum.getCode());
        baseResponse.setMessage(resultEnum.getDesc());
        baseResponse.serverTime = System.currentTimeMillis();
        return baseResponse;
    }

    public static <T> BaseResponse<T> fail(int code, String message, T data) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setData(data);
        baseResponse.setCode(code);
        baseResponse.setMessage(message);
        baseResponse.serverTime = System.currentTimeMillis();
        return baseResponse;
    }


    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setData(data);
        baseResponse.setCode(DigitalResultEnum.SUCCESS.getCode());
        baseResponse.setMessage(DigitalResultEnum.SUCCESS.getDesc());
        baseResponse.serverTime = System.currentTimeMillis();
        return baseResponse;
    }

    public static <T> BaseResponse<T> success(int code, String message) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setData(null);
        baseResponse.setCode(code);
        baseResponse.setMessage(message);
        baseResponse.serverTime = System.currentTimeMillis();
        return baseResponse;
    }

    public static <T> BaseResponse<T> success(int code, String message, T data) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setData(data);
        baseResponse.setCode(code);
        baseResponse.setMessage(message);
        baseResponse.serverTime = System.currentTimeMillis();
        return baseResponse;
    }


}