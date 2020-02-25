package com.mobao360.system.annotation;

import java.lang.annotation.*;

/**
 * 用于标记需要数据字典转换的字段
 * @author: CSZ 991587100@qq.com
 * @date: 2018/11/15 14:25
 */
@Documented
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DictionaryCode {

    String value() default "";
}
