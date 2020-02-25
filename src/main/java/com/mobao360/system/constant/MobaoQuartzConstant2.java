package com.mobao360.system.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author yanghongquan
 * @email 842592135@qq.com
 * @date 2018/10/11 9:23
 */
@PropertySource(value = "classpath:properties/mobao.properties",ignoreResourceNotFound = true,encoding="utf-8")
@Component
@ConfigurationProperties(prefix = "mobao.quartz2")
@Data
public class MobaoQuartzConstant2 {
    private String cron;
    private String name;
}
