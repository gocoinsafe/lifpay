package org.hcm.lifpay.user.dto.req;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hcm.lifpay.common.BaseRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoginRequest extends BaseRequest {

    String token;
}
