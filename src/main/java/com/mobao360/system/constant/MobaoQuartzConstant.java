package com.mobao360.system.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author yanghongquan
 * @email 842592135@qq.com
 * @date 2018/10/11 9:23
 */
@PropertySource(value = "classpath:properties/mobao.properties",ignoreResourceNotFound = true,encoding="utf-8")
@Component
@Data
public class MobaoQuartzConstant {
    @Value("${mobao.quartz.cron}")
    private String cron;
    @Value("${mobao.quartz.name}")
    private String name;
    @Value("${mobao.quartz.flag}")
    private Boolean flag;

}
