package com.luckysj.demo.annotation;

import java.lang.annotation.*;

/**
 * @className: OperationLog日志注解
 * @author 牵着猫散步的鼠鼠-LiuShiJie
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    /**
     * @return 操作描述
     */
    String value() default "";

}
