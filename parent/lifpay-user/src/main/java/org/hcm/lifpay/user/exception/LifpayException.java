package org.hcm.lifpay.user.exception;


import lombok.*;
import org.apache.catalina.User;
import org.hcm.lifpay.user.dto.UserResultEnum;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class LifpayException extends RuntimeException {

    private static final long serialVersionUID = -1460415671287872090L;
    private int code;
    private String message;

    public LifpayException (UserResultEnum resultEnum){
        this.setCode(resultEnum.getCode());
        this.setMessage(resultEnum.getMsg());
    }
}
