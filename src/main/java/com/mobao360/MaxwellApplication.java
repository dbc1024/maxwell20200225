package com.mobao360;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author yanghongquan
 * @email 842592135@qq.com
 * @date 2018/10/11 14:32
 */
@Configuration
@EnableHystrix
@EnableFeignClients
@EnableDiscoveryClient
@EnableWebMvc
@SpringBootApplication
@EnableDistributedTransaction
@MapperScan("com.mobao360.mapper")
//@ImportResource(locations = {"classpath:spring-mvc.xml"})
public class MaxwellApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaxwellApplication.class, args);
    }



}
