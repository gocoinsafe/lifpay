package org.hcm.lifpay.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证：必须具有指定权限才能进入该方法
 *
 * @author xinzhe
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface RequiresPermission
{
    /**
     * 需要校验的权限码
     */
    String value() default "";
}

