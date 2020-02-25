package com.mobao360.system.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2018/10/18 15:17
 * @description: demo常量类。
 * 用这种注入方式在使用常量的时候可以用DemoConstant.BANK_CODE方式获取常量值，比较推荐。
 */
@Component
@PropertySource("classpath:/properties/demo.properties")
public class DemoConstant {

    public static String BANK_CODE;


    /**
     * set方法不要用static修饰，否则无法注入值
     * @param bankCode
     */
    @Value("${demo.bankCode}")
    public void setBankCode(String bankCode) {
        BANK_CODE = bankCode;
    }
}
