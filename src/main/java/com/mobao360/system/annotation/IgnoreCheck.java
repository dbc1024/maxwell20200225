package com.mobao360.system.annotation;

import java.lang.annotation.*;

/**
 * app登录效验
 * @author yanghongquan
 * @email
 * @date 2018/7/20 14:30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreCheck {
}