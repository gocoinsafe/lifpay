package org.hcm.lifpay.exception;

public class CommonRuntimeException extends RuntimeException{

    public CommonRuntimeException() {
        super();
    }

    public CommonRuntimeException(String message) {
        super(message);
    }

    public CommonRuntimeException(Throwable cause) {
        super(cause);
    }
}
