package com.mobao360.api.ours;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventDetail;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.base.entity.BankCode;
import com.mobao360.base.service.IBankCodeService;
import com.mobao360.channel.service.IChannelFeeRateService;
import com.mobao360.customer.entity.*;
import com.mobao360.customer.service.*;
import com.mobao360.system.constant.CAuditEventStatus;
import com.mobao360.system.constant.CMerchantStatus;
import com.mobao360.system.constant.COperation;
import com.mobao360.system.constant.Constants;
import com.mobao360.system.dto.LastTradeOutDto;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.LogUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/1/19 11:08
 */
public class BaseOpenApi {

    @Autowired
    IMerchantInfoService merchantInfoService;
    @Autowired
    IMerchantConfigService merchantConfigService;
    @Autowired
    IAuditEventService auditEventService;
    @Autowired
    IAuditEventFlowService auditEventFlowService;
    @Autowired
    IAuditEventDetailService auditEventDetailService;
    @Autowired
    IMerchantFeeRateService merchantFeeRateService;
    @Autowired
    IBankCodeService bankCodeService;
    @Autowired
    IMerchantCentreAccountService merchantCentreAccountService;
    @Autowired
    IMerchantSettlementInfoService merchantSettlementInfoService;
    @Autowired
    IMerchantFundSettlementService merchantFundSettlementService;
    @Autowired
    IChannelFeeRateService channelFeeRateService;



    /**
     * 根据客户号获取商户信息(包含商户配置，商户结算，即时结算)
     * @param customerNo
     * @return
     */
    public Map<String, Object> getMerchantInfoByCustomerNo(String customerNo){

        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
        MerchantCentreAccount centreAccount = merchantCentreAccountService.getByCustomerNo(customerNo);
        MerchantSettlementInfo settlement = merchantSettlementInfoService.getByCustomerNo(customerNo);

        Map<String, Object> aboutMerchantInfo = BeanUtil.beanToMap(merchantInfo);
        //老系统数据没有商户中心账号信息
        if(centreAccount!=null){
            aboutMerchantInfo.put("managerTel", centreAccount.getManagerTel());
            aboutMerchantInfo.put("managerEmail", centreAccount.getManagerEmail());
        }
        aboutMerchantInfo.put("accountType", settlement.getAccountType());

        return aboutMerchantInfo;
    }


    public Map<String, Object> settlementInfoBase(String customerNo){

        MerchantConfig config = merchantConfigService.getByCustomerNo(customerNo);
        MerchantSettlementInfo settlement = merchantSettlementInfoService.getByCustomerNo(customerNo);
        MerchantFundSettlement fundSettlement = merchantFundSettlementService.getByCustomerNo(customerNo);


        Map<String, Object> aboutSettlement = new HashMap<>(16);
        aboutSettlement.put("settlementPeriod", config.getSettlementPeriod());
        aboutSettlement.put("consume", config.getConsume());
        aboutSettlement.put("refund", config.getRefund());
        aboutSettlement.put("settlement", config.getSettlement());
        aboutSettlement.put("crossBorder", config.getCrossBorder());
        aboutSettlement.put("payType", config.getPayType());
        aboutSettlement.put("minWithdrawCashMoney", config.getMinWithdrawCashMoney());
        aboutSettlement.put("paymentAccount", config.getPaymentAccount());
        aboutSettlement.put("exchangeDepositRate", config.getExchangeDepositRate());
        aboutSettlement.put("purchasePaymentBankChannel", config.getPurchasePaymentBankChannel());
        aboutSettlement.put("receiveSettBankChannel", config.getReceiveSettBankChannel());
        aboutSettlement.put("openedCurrencyAccount", config.getOpenedCurrencyAccount());



        aboutSettlement.put("bankName", settlement.getBankName());
        aboutSettlement.put("bankNo", settlement.getBankNO());
        aboutSettlement.put("branchBankName", settlement.getBranchBankName());
        aboutSettlement.put("branchBankNo", settlement.getBranchBankNo());
        aboutSettlement.put("bankAccountName", settlement.getBankAccountName());
        aboutSettlement.put("accountNo", settlement.getAccountNo());
        aboutSettlement.put("accountType", settlement.getAccountType());
        aboutSettlement.put("payeeIdNum", settlement.getPayeeIdNum());
        aboutSettlement.put("reserveMobileNo", settlement.getReserveMobileNo());


        if(fundSettlement != null){
            aboutSettlement.put("openFundSettlement", fundSettlement.getOpenFundSettlement());
            if(!Constants.NO.equals(fundSettlement.getOpenFundSettlement())){
                aboutSettlement.put("feeType", fundSettlement.getFeeType());
                aboutSettlement.put("feeRate", fundSettlement.getFeeRate());
                aboutSettlement.put("fixedAmount", fundSettlement.getFixedAmount());
            }
            aboutSettlement.put("singleDayMaxFund", fundSettlement.getSingleDayMaxFund());
            aboutSettlement.put("fundSettlementScale", fundSettlement.getFundSettlementScale());
        }

        return aboutSettlement;
    }


    /**
     * 根据客户号获取开通的产品及支付方式
     * @param customerNo
     * @return
     */
    public Map<String, Object> getOpendProductsAndPayTypesByCustomerNo(String customerNo){

        MerchantConfig merchantConfig = merchantConfigService.getByCustomerNo(customerNo);
        String payTypes = merchantConfig.getPayType();
        String[] payTypeArr = payTypes.split(",");

        Set<String> productKindOpendSet = new HashSet<>();
        Set<String> payTypeOpendSet = new HashSet<>();
        for (String payType : payTypeArr) {

            if(StringUtils.isNotBlank(payType)){
                payTypeOpendSet.add(payType);
                productKindOpendSet.add(payType.substring(0,2) + "0");
            }
        }

        List<Map<String, String>> productKindAll = DictionaryUtil.getKeyValueListByCode("product_kind");
        List<Map<String, String>> productKindOpend = new LinkedList<>();
        for (Map<String, String> productKindMap : productKindAll) {

            String value = productKindMap.get("value");
            if(productKindOpendSet.contains(value)){
                productKindOpend.add(productKindMap);
            }
        }

        List<Map<String, String>> payTypeAll = DictionaryUtil.getKeyValueListByCode("pay_type");
        List<Map<String, String>> payTypeOpend = new LinkedList<>();
        for (Map<String, String> payTypeMap : payTypeAll) {

            String value = payTypeMap.get("value");
            if(payTypeOpendSet.contains(value)){
                payTypeOpend.add(payTypeMap);
            }

        }


        Map<String, Object> productsAndPayTypes = new HashMap<>(2);
        productsAndPayTypes.put("product", productKindOpend);
        productsAndPayTypes.put("payType", payTypeOpend);

        return productsAndPayTypes;
    }


    /**
     * 回滚新增的事件
     * @param auditEventId
     */
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public void rollbackCreate (Long auditEventId){

        /**删除审核事件*/
        auditEventService.removeById(auditEventId);

        /**删除审核事件明细*/
        auditEventDetailService.remove(new LambdaQueryWrapper<AuditEventDetail>().eq(AuditEventDetail::getAuditEventId, auditEventId));
    }


    /**
     * 事件审核
     * 入参：{审核事件ID}，{审核操作}，{明细描述}
     * @param params
     * @return
     */
    public Map<String, String> eventAudit(Map<String, String> params){

        boolean pass = auditEventService.audit(params);
        if(pass){
            Map<String, String> result = new HashMap<>(8);
            result.put("pass", Constants.YES);
            return result;
        }else {
            return null;
        }

    }


    /**
     * 根据审核事件ID回滚审核事件
     * 只有审核通过的事件才能回滚
     * @param auditEventId
     */
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public void rollbackAudit (Long auditEventId){

        AuditEvent event = auditEventService.getById(auditEventId);
        String status = event.getStatus();
        if(!CAuditEventStatus.PASS.equals(status)){
            throw new MobaoException("此事件未审核通过，不可回滚");
        }

        /**删除审核通过明细*/
        LambdaQueryWrapper<AuditEventDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditEventDetail::getAuditEventId, event.getId());
        wrapper.eq(AuditEventDetail::getNodeCode, event.getNodeCode());
        auditEventDetailService.remove(wrapper);


        /**将审核事件状态置为审核中*/
        event.setStatus(CAuditEventStatus.AUDITING);
        event.setUpdateTime(DateUtil.now());
        auditEventService.updateById(event);

    }


    /**
     * 订单系统获取商户相关信息
     * 入参：custNo, bankCode, payType
     * @param params
     * @return
     */
    Map<String, Object> orderQueryMerchantInfoBase(Map<String, String> params){

        String custNo = params.get("custNo");
        String bankCode = params.get("bankCode");
        String payType = params.get("payType");

        //查询商户信息
        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(custNo);
        if(merchantInfo == null){
            throw new MobaoException("该商户号不存在");
        }
        //查询配置信息
        MerchantConfig merchantConfig = merchantConfigService.getByCustomerNo(custNo);
        //查询当前可用费率信息
        MerchantFeeRate feeRate = merchantFeeRateService.getByCustomerNoAndPayType(custNo, payType);
        if(feeRate == null){
            throw new MobaoException("该商户未配置此费率["+ payType +"]");
        }

        Map<String, Object> aboutMerchant = new HashMap<>(16);
        aboutMerchant.put("custNo", merchantInfo.getCustomerNo());
        aboutMerchant.put("branchNo", merchantInfo.getBranchNo());
        aboutMerchant.put("shortName", merchantInfo.getShortName());
        aboutMerchant.put("custName", merchantInfo.getName());
        aboutMerchant.put("status", merchantInfo.getStatus());
        aboutMerchant.put("ecommerceWebsiteName", merchantInfo.getEcommerceWebsiteName());
        aboutMerchant.put("customs", merchantInfo.getCustoms());
        aboutMerchant.put("customsRegisterNo", merchantInfo.getCustomsRegisterNo());
        aboutMerchant.put("tradeCheck", merchantInfo.getTradeCheck());

        aboutMerchant.put("consume", merchantConfig.getConsume());
        aboutMerchant.put("refund", merchantConfig.getRefund());
        aboutMerchant.put("settlement", merchantConfig.getSettlement());
        aboutMerchant.put("crossBorder", merchantConfig.getCrossBorder());
        aboutMerchant.put("bolPayTypeAlive", merchantConfig.getPayType().contains(payType));
        aboutMerchant.put("lowerAmt", merchantConfig.getMinWithdrawCashMoney());

        aboutMerchant.put("bolFeeAlive", feeRate != null);
        aboutMerchant.put("feeType", feeRate.getFeeType());
        aboutMerchant.put("feeRate", feeRate.getFeeRate());
        aboutMerchant.put("fixedAmount", feeRate.getFixedAmount());
        aboutMerchant.put("minFee", feeRate.getMinFee());
        aboutMerchant.put("maxFee", feeRate.getMaxFee());

        if(bankCode != null){
            BankCode bankCodeObject = bankCodeService.getByBankCode(bankCode);
            if(bankCodeObject == null){
                throw new MobaoException("系统暂未配置该银行编码"+ bankCode);
            }

            aboutMerchant.put("bolBankSupport", bankCodeObject.supportPayType(payType));
            aboutMerchant.put("bankName", bankCodeObject.getBankName());
        }

        //订单系统需要在提现时获取以下字段值
        if("311".equals(payType)){
            MerchantSettlementInfo settlementInfo = merchantSettlementInfoService.getByCustomerNo(custNo);
            BankCode bankCodeObject = bankCodeService.getCodeByBranchBankNo(settlementInfo.getBranchBankNo());
            aboutMerchant.put("bankCode", bankCodeObject.getBankCode());
            aboutMerchant.put("branchBankNo", settlementInfo.getBranchBankNo());
            aboutMerchant.put("accountNo", settlementInfo.getAccountNo());
            aboutMerchant.put("bankAccountName", settlementInfo.getBankAccountName());
            aboutMerchant.put("accountType", settlementInfo.getAccountType());
        }

        return aboutMerchant;
    }



    List<BankCode> supportBanksBase(String payType) {

        LambdaQueryWrapper<BankCode> wrapper = new LambdaQueryWrapper<>();

        if(payType != null){

            switch (payType) {
                case "111": {
                    wrapper.eq(BankCode::getInternetBankDebit, Constants.YES);
                    break;
                }
                case "112": {
                    wrapper.eq(BankCode::getInternetBankCredit, Constants.YES);
                    break;
                }
                case "113": {
                    wrapper.eq(BankCode::getEnterpriseInternetBank, Constants.YES);
                    break;
                }
                case "114": {
                    wrapper.eq(BankCode::getInternetBankMix, Constants.YES);
                    break;
                }
                case "311": {
                    wrapper.eq(BankCode::getWithdrawCash, Constants.YES);
                    break;
                }
                case "312": {
                    wrapper.eq(BankCode::getEntrustPay, Constants.YES);
                    break;
                }
                case "141": {
                    wrapper.eq(BankCode::getAgreementDebit, Constants.YES);
                    break;
                }
                case "142": {
                    wrapper.eq(BankCode::getAgreementCredit, Constants.YES);
                    break;
                }
                case "131": {
                    wrapper.eq(BankCode::getQuickDebit, Constants.YES);
                    break;
                }
                case "132": {
                    wrapper.eq(BankCode::getQuickCredit, Constants.YES);
                    break;
                }

                default: {
                    throw new MobaoException("没有此支付方式");
                }

            }
        }


        List<BankCode> list = bankCodeService.list(wrapper);

        return list;
    }


    Map<String, String> getBankCodeListBase(){


        Map<String, String> keyValue = bankCodeService.getKeyValue();

        return keyValue;
    }


    Map<String, String> getBusinessBase(String customerNo){

        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);

        //(数据字典 merchant_business_type：1-普通业务，2-跨境进口，3-跨境出口，4-跨境通关)
        String businessType = merchantInfo.getBusinessType();
        Map<String, String> result = new HashMap<>(4);
        result.put("commonType", businessType.contains("1")?"1":"0");
        result.put("importType", businessType.contains("2")?"1":"0");
        result.put("exportType", businessType.contains("3")?"1":"0");
        result.put("passType", businessType.contains("4")?"1":"0");
        result.put("merchantProperty", merchantInfo.getMerchantProperty());

        return result;
    }




    @LcnTransaction
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    void updateLastTradeDateBase(LastTradeOutDto params){
        LogUtil.insert("订单系统更新商户最后交易日期", COperation.UPDATE, null, JSON.toJSONString(params));

        String lastDate = params.getLastDate();
        if(StringUtils.isNotBlank(lastDate)){

            List<String> tradeCustomerNoList = params.getTradeCustomerNoList();
            if(null!=tradeCustomerNoList && tradeCustomerNoList.size()>0){
                LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(MerchantInfo::getCustomerNo, tradeCustomerNoList);

                MerchantInfo merchantInfo = new MerchantInfo();
                merchantInfo.setLastTradeDate(lastDate);

                merchantInfoService.update(merchantInfo, wrapper);
            }


            List<String> outCustomerNoList = params.getOutCustomerNoList();
            if(null!=outCustomerNoList && outCustomerNoList.size()>0){

                LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(MerchantInfo::getCustomerNo, outCustomerNoList);

                MerchantInfo merchantInfo = new MerchantInfo();
                merchantInfo.setLastOutDate(lastDate);

                merchantInfoService.update(merchantInfo, wrapper);
            }

        }
    }


    @LcnTransaction
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    void setBlacklistBase(Map<String, List<String>> params){
        LogUtil.insert("风控将商户置为黑名单商户", COperation.UPDATE, null, JSON.toJSONString(params));

        List<String> merchantName = params.get("merchantName");
        List<String> legalPersonCertNum = params.get("legalPersonCertNum");
        List<String> businessLicenceNo = params.get("businessLicenceNo");

        LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MerchantInfo::getName, merchantName);
        wrapper.or().in(MerchantInfo::getLegalPersonCertNum, legalPersonCertNum);
        wrapper.or().in(MerchantInfo::getBusinessLicenceNo, businessLicenceNo);
//        wrapper.or(i->i.in(MerchantInfo::getLegalPersonCertNum, legalPersonCertNum).in(MerchantInfo::getBusinessLicenceNo, businessLicenceNo));

        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setTradeCheck(Constants.YES);

        merchantInfoService.update(merchantInfo, wrapper);

    }


    List<Map<String, Object>> getByCustomerNoListBase(List<String> customerNoList){
        LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MerchantInfo::getCustomerNo, customerNoList);

        List<MerchantInfo> list = merchantInfoService.list(wrapper);

        if(list!= null && list.size()>0){
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (MerchantInfo merchantInfo : list) {
                mapList.add(DictionaryUtil.keyValueHandle(merchantInfo));
            }

            return mapList;
        }

        return null;
    }

    Integer countOpenedAmountBase(){
        LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(MerchantInfo::getStatus, CMerchantStatus.LOGOUT);

        return merchantInfoService.count(wrapper);
    }

}
