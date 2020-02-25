package com.mobao360.system.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Component
 * @PropertySource("classpath:/application.yml")
 * public class ConfigConstant {
 *
 *     public static String PERSONAL_IP;
 *
 *     public static String ACTIVE;
 *
 *     public static String TASK_IP;
 *
 *
 *     @Value("${personal.ip}")
 *     public void setBankCode(String personalIp) {
 *         PERSONAL_IP = personalIp;
 *     }
 *
 *
 *     @Value("${spring.profiles.active}")
 *     public void setActive(String active) {
 *         ACTIVE = active;
 *     }
 *
 *
 *     @Value("${task.ip}")
 *     public void setTaskIp(String taskIp) {
 *         TASK_IP = taskIp;
 *     }
 * }
 * 采用以上方式，在服务器上发现一个莫名的问题：
 * 获取到对应的属性值为null
 * 重新打包又能获取到属性值
 * 再打包可能又为null
 * 找不到原因，不得已弃用
 *
 *
 * @author: CSZ 991587100@qq.com
 * @date: 2019/4/9 09:38
 */
@Data
@Component
@ConfigurationProperties(prefix = "dbc")
public class ConfigConstant {

    private String env;

    private String taskIp;


}
