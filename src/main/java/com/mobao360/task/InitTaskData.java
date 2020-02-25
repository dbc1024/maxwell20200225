package com.mobao360.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mobao360.customer.entity.MerchantCentreAccount;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.service.IMerchantCentreAccountService;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.system.constant.ConfigConstant;
import com.mobao360.system.constant.Constants;
import com.mobao360.system.utils.NetUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/5/31 09:54
 */
@Component
@Log4j2
public class InitTaskData implements InitializingBean {

    @Autowired
    private ConfigConstant config;
    @Autowired
    private IMerchantInfoService merchantInfoService;
    @Autowired
    private IMerchantCentreAccountService merchantCentreAccountService;

    public static AtomicInteger merchantCentreAccount = new AtomicInteger(0);
    public static AtomicInteger tradeAccountData = new AtomicInteger(0);

    public static Boolean isTaskServer = false;


    @Override
    public void afterPropertiesSet(){
        log.info("[本机IP]:{}", NetUtil.getLocalIp());
        log.info("[配置文件中定时任务IP]:{}", config.getTaskIp());
        log.info("[当前运行环境]:{}", config.getEnv());

        if("local".equals(config.getTaskIp())){
            isTaskServer = true;
        }else {
            if(NetUtil.getLocalIp().equals(config.getTaskIp())){
                isTaskServer = true;
            }
        }
        log.info("[定时任务可执行标识初始化]isTaskServer: "+ isTaskServer);


//        tradeAccountData.addAndGet(countTradeAccountData());
//        merchantCentreAccount.addAndGet(countMerchantCentreAccount());
    }


    private Integer countTradeAccountData(){
        LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(MerchantInfo::getTask1, Constants.YES);
        wrapper.isNull(MerchantInfo::getOldCustomerNo);

        int count = merchantInfoService.count(wrapper);
        log.info("初始化待推送交易系统开户数据{}条", count);

        return count;
    }



    private Integer countMerchantCentreAccount(){
        LambdaQueryWrapper<MerchantCentreAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(MerchantCentreAccount::getTask1, Constants.YES);

        int count = merchantCentreAccountService.count(wrapper);
        log.info("初始化待推送商户中心账号数据{}条", count);

        return count;
    }
}
