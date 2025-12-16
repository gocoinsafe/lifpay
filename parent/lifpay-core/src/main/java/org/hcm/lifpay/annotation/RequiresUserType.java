package org.hcm.lifpay.annotation;

import org.hcm.lifpay.common.enums.AdminUserTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证：必须是指定的用户类型才能进入该方法
 *
 * @author xinzhe
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface RequiresUserType
{
    /**
     * 需要校验的用户类型名
     */
    AdminUserTypeEnum[] value() default {};
}

