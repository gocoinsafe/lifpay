package org.hcm.lifpay.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;



/**
 * @author xinzhe
 */
@Documented
@Component
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CostDurationLog {
}
