package com.mobao360.system.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author yanghongquan
 * @email 842592135@qq.com
 * @date 2018/10/11 15:00
 */
@PropertySource(value = "classpath:properties/mobao.properties",ignoreResourceNotFound = true,encoding="utf-8")
@Component
@ConfigurationProperties(prefix = "mobao.intercept")
@Data
public class MoboIntercerptConstant {
    /**
     * 是否开启过滤
     */
    private Boolean flag;

}
