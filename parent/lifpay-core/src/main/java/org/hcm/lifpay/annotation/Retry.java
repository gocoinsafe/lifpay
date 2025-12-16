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
public @interface Retry {
    /**
     * 重试次数
     * @return 重试次数
     */
    int tryTimes() default 3;

    /**
     * 重试间隔时间
     * -1: 随机等待1-1000毫秒
     * 0: 不等待(默认)
     * 指定: 指定毫秒数
     * @return 重试间隔时间(毫秒)
     */
    int waitTime() default 0;

    /**
     * 需要重试的异常类型
     * @return 需要重试的异常类型列表
     */
    Class<? extends Exception>[] retry();
}
