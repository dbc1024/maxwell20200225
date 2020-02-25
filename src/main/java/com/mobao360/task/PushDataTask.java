package com.mobao360.task;

import com.alibaba.fastjson.JSON;
import com.mobao360.api.feign.MerchantCentreFeign;
import com.mobao360.api.feign.TradeFeign;
import com.mobao360.base.entity.BankCode;
import com.mobao360.base.service.IBankCodeService;
import com.mobao360.customer.entity.MerchantCentreAccount;
import com.mobao360.customer.entity.MerchantConfig;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.entity.MerchantSettlementInfo;
import com.mobao360.customer.service.IMerchantCentreAccountService;
import com.mobao360.customer.service.IMerchantConfigService;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.customer.service.IMerchantSettlementInfoService;
import com.mobao360.system.constant.Constants;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.Result;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 向其他服务推送资料数据
 * @author: CSZ 991587100@qq.com
 * @date: 2019/2/18 09:35
 */
@Log4j2
@Component
@EnableScheduling
public class PushDataTask {
    private Logger logger = LoggerFactory.getLogger(PushDataTask.class);

    @Autowired
    private MerchantCentreFeign merchantCentreFeign;

    @Autowired
    private TradeFeign tradeFeign;

    @Autowired
    private IMerchantCentreAccountService merchantCentreAccountService;

    @Autowired
    private IMerchantInfoService merchantInfoService;

    @Autowired
    private IMerchantSettlementInfoService merchantSettlementInfoService;

    @Autowired
    private IBankCodeService bankCodeService;

    @Autowired
    private IMerchantConfigService merchantConfigService;



    /**
     * 向商户中心推送商户中心账号
     */
    @Scheduled(cron = "0 */1 * * * *")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void merchantCentreAccountTask(){
        if(!InitTaskData.isTaskServer){
            return;
        }

//        if(InitTaskData.merchantCentreAccount.get()==0){
//            return;
//        }

        logger.info("[定时任务]推送商户中心账号开始执行");

        MerchantCentreAccount account = merchantCentreAccountService.findPushedAccount();
        if(account == null){
//            InitTaskData.merchantCentreAccount.decrementAndGet();
            logger.info("未检测到可推送账号数据");
            return;
        }


        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(account.getCustomerNo());

        Map<String, String> aboutAccount = new HashMap<>(8);
        aboutAccount.put("customerNo", account.getCustomerNo());
        aboutAccount.put("managerEmail", account.getManagerEmail());
        aboutAccount.put("managerTel", account.getManagerTel());
        aboutAccount.put("businessType", merchantInfo.getBusinessType());
        aboutAccount.put("merchantProperty", merchantInfo.getMerchantProperty());

        logger.info("检测到可推送账号数据："+ JSON.toJSONString(aboutAccount));

        //定时任务标识修改为已成功
        account.setTask1(Constants.YES);
        merchantCentreAccountService.updateById(account);

        Result result;
        try {
            result = merchantCentreFeign.createAccount(aboutAccount);
        }catch (Exception e){
            throw new MobaoException("商户中心调用失败", e);
        }

        logger.info("商户中心返回："+ JSON.toJSONString(result));
        if(!Constants.YES.equals(result.getRetCode())){
            throw new MobaoException("为商户中心推送账号,商户中心返回异常："+ result.getRetMsg());
        }

//        InitTaskData.merchantCentreAccount.decrementAndGet();

        logger.info("推送商户中心账号定时任务执行成功");
    }


    
    /**
     * 向交易服务推送开户数据
     */
    @Scheduled(cron = "0 */1 * * * *")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void tradeAccountDataTask(){
        if(!InitTaskData.isTaskServer){
            return;
        }

//        if(InitTaskData.tradeAccountData.get()==0){
//            return;
//        }
        logger.info("[定时任务]向交易服务推送开户数据开始执行");

        MerchantInfo merchantInfo = merchantInfoService.findTradePushedData();
        if(merchantInfo == null){
//            InitTaskData.merchantCentreAccount.decrementAndGet();
            logger.info("未检测到可推送开户数据");
            return;
        }


        MerchantSettlementInfo settlementInfo = merchantSettlementInfoService.getByCustomerNo(merchantInfo.getCustomerNo());
        BankCode bankCode = bankCodeService.getCodeByBranchBankNo(settlementInfo.getBranchBankNo());
        MerchantConfig merchantConfig = merchantConfigService.getByCustomerNo(merchantInfo.getCustomerNo());
        String currencyStr = merchantConfig.getOpenedCurrencyAccount();
        if(currencyStr.startsWith(",")){
            currencyStr = currencyStr.substring(1);
        }
        String[] currency = currencyStr.split(",");


        //定时任务标识修改为已成功
        merchantInfo.setTask1(Constants.YES);
        merchantInfoService.updateById(merchantInfo);


        Map<String, Object> data = new HashMap(16);
        data.put("brchNo", merchantInfo.getBranchNo());
        data.put("custNo", merchantInfo.getCustomerNo());
        data.put("custName", merchantInfo.getName());
        data.put("industryCode", merchantInfo.getIndustryCode());
        data.put("custType", "2");
        data.put("channel", "01");
        data.put("pwd", "null");
        data.put("bankAcct", settlementInfo.getAccountNo());
        data.put("bankAcctName", settlementInfo.getBankAccountName());
        data.put("bankCode", bankCode.getBankCode());
        data.put("list", currency);

        logger.info("检测到可推送开户数据："+ JSON.toJSONString(data));

        Result result;
        try {
            result = tradeFeign.createAccount(data);
        }catch (Exception e){
            throw new MobaoException("交易服务调用失败", e);
        }

        logger.info("交易系统返回："+ JSON.toJSONString(result));
        if(!Constants.YES.equals(result.getRetCode())){

            //如果返回状态码为"CTS6101",则表明该商户已开户成功
            if("CTS6101".equals(result.getRetCode())){
                logger.info("交易系统反馈该商户已开户成功,直接处理为已成功状态");
            }else {
                throw new MobaoException("为交易服务推送开户数据,交易服务返回异常："+ result.getRetMsg());
            }
        }

//        InitTaskData.tradeAccountData.decrementAndGet();

        logger.info("为交易服务推送开户数据定时任务执行成功");
    }


}
