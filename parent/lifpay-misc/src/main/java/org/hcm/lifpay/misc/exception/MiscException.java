package org.hcm.lifpay.misc.exception;


import org.hcm.lifpay.misc.common.MiscResultEnum;

/**
 * 基础运行时异常
 *
 * @author xinzhe
 */
public class MiscException extends RuntimeException{

    private static final long serialVersionUID = 5955772260286592309L;
    private int errorCode = -1;
    private String errorMessage;

    public MiscException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public MiscException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public MiscException(int errorCode, String errorMessage) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;

    }

    public MiscException(MiscResultEnum messageResult) {
        super();
        this.errorCode = messageResult.getCode();
        this.errorMessage = messageResult.getDesc();

    }

    public MiscException(int errorCode, String errorMessage, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }



}
