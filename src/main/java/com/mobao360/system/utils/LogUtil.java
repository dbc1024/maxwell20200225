package com.mobao360.system.utils;

import com.alibaba.fastjson.JSON;
import com.mobao360.base.entity.LogEntity;
import com.mobao360.system.kafka.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/3/18 16:03
 */
@Component
public class LogUtil {

    private static LogUtil logUtil;

    @Autowired
    private Provider provider;

    @PostConstruct
    public void init(){
        logUtil = this;
        logUtil.provider = this.provider;
    }

    /**
     * kafka发送用户操作日志
     * @param module
     * @param operation
     * @param before
     * @param after
     * @param <T>
     */
    public static <T> void insert(String module, String operation, T before, T after){


        String accessIp = null;
        String username = null;
        if("nothing".equals(before)){
            accessIp = NetUtil.getLocalIp();
        }else {
            username = LoginUserInfoUtil.getUsername();
            accessIp = NetUtil.getAccessIp();
        }

        LogEntity log = LogEntity.builder()
                .system("运营管理系统")
                .module(module)
                .operation(operation)
                .operator(username)
                .operatorIp(accessIp)
                .beforeInfo(JSON.toJSONString(before))
                .afterInfo(JSON.toJSONString(after))
                .operatorTime(new Date())
                .build();

        logUtil.provider.send("log","", log);
    }
}
