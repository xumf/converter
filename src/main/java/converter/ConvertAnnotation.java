package com.gf.jdp.jira.pms.convert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertAnnotation {
    String value() default ""; // 对应转换对象的属性名
    boolean ingore() default false; // 对应转换对象的属性名
}
