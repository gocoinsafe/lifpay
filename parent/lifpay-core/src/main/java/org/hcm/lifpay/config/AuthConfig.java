package org.hcm.lifpay.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author xinzhe
 */
@Configuration
@Data
public class AuthConfig {
    @Value("${admin.noAuth:false}")
    boolean noAuth;
}
