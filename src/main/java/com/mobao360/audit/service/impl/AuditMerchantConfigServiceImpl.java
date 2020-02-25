package com.mobao360.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.api.feign.TradeFeign;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventDetail;
import com.mobao360.audit.entity.AuditEventFlow;
import com.mobao360.audit.entity.AuditMerchantConfig;
import com.mobao360.audit.mapper.AuditMerchantConfigMapper;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.audit.service.IAuditMerchantConfigService;
import com.mobao360.base.entity.BankCode;
import com.mobao360.base.service.IBankCodeService;
import com.mobao360.customer.entity.MerchantConfig;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.entity.MerchantSettlementInfo;
import com.mobao360.customer.service.IMerchantConfigService;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.customer.service.IMerchantSettlementInfoService;
import com.mobao360.system.constant.*;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.BeanChangeCompareUtil;
import com.mobao360.system.utils.LoginUserInfoUtil;
import com.mobao360.system.utils.RegexUtil;
import com.mobao360.system.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * service实现类
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-11
 */
@Service
public class AuditMerchantConfigServiceImpl extends ServiceImpl<AuditMerchantConfigMapper, AuditMerchantConfig> implements IAuditMerchantConfigService {

    private Logger logger = LoggerFactory.getLogger(AuditMerchantConfigServiceImpl.class);

    @Autowired
    private IAuditEventService auditEventService;

    @Autowired
    private IAuditEventDetailService auditEventDetailService;

    @Autowired
    private IAuditEventFlowService auditEventFlowService;

    @Autowired
    private IMerchantConfigService merchantConfigService;

    @Autowired
    private IMerchantInfoService merchantInfoService;

    @Autowired
    private AuditMerchantConfigMapper auditMerchantConfigMapper;
    @Autowired
    private IMerchantSettlementInfoService merchantSettlementInfoService;
    @Autowired
    private IBankCodeService bankCodeService;
    @Autowired
    private TradeFeign tradeFeign;


    /**
     * 新增
     *
     * @param auditMerchantConfig
     * @return
     */
    @Override
    public boolean save(AuditMerchantConfig auditMerchantConfig) {

        return super.save(auditMerchantConfig);
    }

    /**
     * 入网新增
     *
     * @param auditMerchantConfig
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean accessNetworkCreate(AuditMerchantConfig auditMerchantConfig) {
        //根据结算状态 检查相应条件
        checkSettlementStatus(auditMerchantConfig);

        accessJudgeRepeat(auditMerchantConfig);
        String currTime = DateUtil.now();
        /** 1.取出入网审核事件信息*/
        AuditEvent event = auditEventService.getById(auditMerchantConfig.getAuditEventId());
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        /** 2.生成一条明细日志记录，记录此次的操作记录*/
        AuditEventDetail detail = new AuditEventDetail();
        //是日志记录，非审核流程记录。
        detail.setIfLog(Constants.YES);
        detail.setAuditEventId(event.getId());
        detail.setOperation(CAuditOperation.CREATE);
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setNodeCode(event.getNodeCode());
        detail.setNodeName(flow.getNodeName());
        detail.setUpdateRecord(auditMerchantConfig.toString());
        detail.setDescription("入网新增商户配置信息");
        detail.setCreateTime(currTime);
        //保存明细日志记录
        auditEventDetailService.save(detail);

        /** 3.新增商户配置草稿*/
        auditMerchantConfig.setCreateTime(currTime);
        auditMerchantConfig.setUpdateTime(currTime);
        return save(auditMerchantConfig);
    }


    /**
     * 变更修改
     * 1.生成审核事件
     * 2.生成明一条细日志记录
     * 3.将审核事件id回填到日志记录
     * 4.保存商户配置修改草稿
     *
     * @param auditMerchantConfig
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean changeUpdate(AuditMerchantConfig auditMerchantConfig) {
        //根据结算状态 检查相应条件
        checkSettlementStatus(auditMerchantConfig);

        String currTime = DateUtil.now();
        String customerNo = auditMerchantConfig.getCustomerNo();
        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);

        //需确保当前用户只存在一个正在审核的变更修改事件（商户配置没有需要去重检查的内容）前提：商户未被注销
        if (merchantInfo != null) {
            if (CMerchantStatus.LOGOUT.equals(merchantInfo.getStatus())) {
                throw new MobaoException("商户已被注销,不能进行商户配置资料修改");
            }
        }else {
            throw new MobaoException("当前商户不存在，请检查商户号");
        }
        //互斥事件检查
        mutexCheck(customerNo);

        /** 1.生成审核事件 */
        // 查询“入网事件”初始节点信息，以备后续赋值
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
                CAuditEventType.MERCHANT_CONFIG_UPDATE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：商户配置变更修改
        auditEvent.setAuditEventType(CAuditEventType.MERCHANT_CONFIG_UPDATE);
        // 审核主体：商户名称
        auditEvent.setSubject(merchantInfo.getName());
        //事件主体编码
        auditEvent.setSubjectCode(auditMerchantConfig.getCustomerNo());
        // 事件状态：草稿
        auditEvent.setStatus(CAuditEventStatus.DRAFT);
        // 节点信息赋值
        auditEvent.setNodeCode(flow.getNodeCode());
        auditEvent.setCreateTime(currTime);
        auditEvent.setUpdateTime(currTime);

        // 保存审核事件
        auditEventService.save(auditEvent);

        /** 2.生成一条明细日志记录 */
        AuditEventDetail detail = new AuditEventDetail();
        //是日志记录，非审核流程记录。
        detail.setIfLog(Constants.YES);
        detail.setAuditEventId(auditEvent.getId());
        detail.setOperation(CAuditOperation.UPDATE);
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setNodeCode(flow.getNodeCode());
        detail.setNodeName(flow.getNodeName());
        detail.setUpdateRecord(auditMerchantConfig.toString());
        detail.setDescription("变更修改商户配置信息");
        detail.setCreateTime(currTime);
        //保存明细日志记录
        auditEventDetailService.save(detail);

        /** 3.将“审核事件ID”回填到商户配置草稿信息中*/
        auditMerchantConfig.setAuditEventId(auditEvent.getId());

        /** 4.新增商户配置修改草稿信息 */
        auditMerchantConfig.setCreateTime(currTime);
        auditMerchantConfig.setUpdateTime(currTime);
        return save(auditMerchantConfig);
    }



    /**
     * 修改
     * <p>
     * 1.生成一条明细日志记录
     * 2. 修改商户配置草稿信息
     *
     * @param auditMerchantConfig
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean updateDraft(AuditMerchantConfig auditMerchantConfig) {
        //根据结算状态 检查相应条件
        checkSettlementStatus(auditMerchantConfig);

        String currTime = DateUtil.now();
        /** 1.生成一条明细日志记录*/
        AuditEventDetail detail = new AuditEventDetail();
        AuditEvent event = auditEventService.getById(auditMerchantConfig.getAuditEventId());
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

        //是日志记录，非审核流程记录。
        detail.setIfLog(Constants.YES);
        detail.setAuditEventId(event.getId());
        detail.setOperation(CAuditOperation.UPDATE);
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setNodeCode(event.getNodeCode());
        detail.setNodeName(flow.getNodeName());
        detail.setUpdateRecord(JSON.toJSONString(auditMerchantConfig));
        detail.setDescription("商户配置信息草稿修改");
        detail.setCreateTime(currTime);
        auditMerchantConfig.setUpdateTime(currTime);
        //保存明细日志记录
        auditEventDetailService.save(detail);

        /** 2. 修改商户配置草稿信息 */
        return super.updateById(auditMerchantConfig);
    }


    /**
     * 结算关闭
     * @param params
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public void settlementClose(Map<String, String> params) {
        String currTime = DateUtil.now();
        String customerNo = params.get("customerNo");

        //需保证用户同时只能存在一个结算关闭事件 前提：商户未被注销，锁定
        MerchantInfo merchantInfoByCustomerNo = merchantInfoService.getByCustomerNo(customerNo);
        String status = merchantInfoByCustomerNo.getStatus();
        if (CMerchantStatus.LOCKED.equals(status)){
            throw new MobaoException("商户已被锁定，不能进行结算关闭操作");
        }else if (CMerchantStatus.LOGOUT.equals(status)){
            throw new MobaoException("商户已被注销，不能进行结算关闭操作");
        }

        //互斥事件检查
        mutexCheck(customerNo);

        /** 1.生成审核事件 */
        // 查询“商户锁定”事件初始节点信息，以备后续赋值
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.MERCHANT_SETTLEMENT_CLOSE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：结算关闭
        auditEvent.setAuditEventType(CAuditEventType.MERCHANT_SETTLEMENT_CLOSE);
        // 审核主体：商户名称
        auditEvent.setSubject(merchantInfo.getName());
        // 审核主体编码：客户号
        auditEvent.setSubjectCode(merchantInfo.getCustomerNo());
        // 事件状态：草稿
        auditEvent.setStatus(CAuditEventStatus.DRAFT);
        // 节点信息赋值
        auditEvent.setNodeCode(flow.getNodeCode());
        auditEvent.setCreateTime(currTime);
        auditEvent.setUpdateTime(currTime);

        //保存审核事件
        auditEventService.save(auditEvent);

        params.put("auditEventId", auditEvent.getId().toString());
        params.put("operation", CAuditOperation.COMMIT_AUDIT);

        /** 调用审核公共方法 */
        auditEventService.audit(params);

    }

    /**
     * 商户交易关闭
     * 传入参数：客户号customerNo，明细描述description
     * @param params
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public void transactionClose(Map<String, String> params) {
        String currTime = DateUtil.now();
        String customerNo = params.get("customerNo");

        //需保证用户同时只能存在一个交易关闭事件 前提：商户未被注销，锁定
        MerchantInfo merchantInfoByCustomerNo = merchantInfoService.getByCustomerNo(customerNo);
        String status = merchantInfoByCustomerNo.getStatus();
        if (CMerchantStatus.LOCKED.equals(status)){
            throw new MobaoException("商户已被锁定，不能进行交易关闭操作");
        }else if (CMerchantStatus.LOGOUT.equals(status)){
            throw new MobaoException("商户已被注销，不能进行交易关闭操作");
        }
        MerchantConfig merchantConfigByCustomerNo = merchantConfigService.getByCustomerNo(customerNo);
        // 0:交易关闭状态
        if ("0".equals(merchantConfigByCustomerNo.getConsume())){
            throw new MobaoException("商户配置交易功能已关闭，不能进行交易关闭操作");
        }

        //互斥事件检查
        mutexCheck(customerNo);

        /** 1.生成审核事件 */
        // 查询“商户锁定”事件初始节点信息，以备后续赋值
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.MERCHANT_TRADE_CLOSE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：交易关闭
        auditEvent.setAuditEventType(CAuditEventType.MERCHANT_TRADE_CLOSE);
        // 审核主体：商户名称
        auditEvent.setSubject(merchantInfo.getName());
        // 审核主体编码：客户号
        auditEvent.setSubjectCode(merchantInfo.getCustomerNo());
        // 事件状态：草稿
        auditEvent.setStatus(CAuditEventStatus.DRAFT);
        // 节点信息赋值
        auditEvent.setNodeCode(flow.getNodeCode());
        auditEvent.setCreateTime(currTime);
        auditEvent.setUpdateTime(currTime);

        //保存审核事件
        auditEventService.save(auditEvent);

        params.put("auditEventId", auditEvent.getId().toString());
        params.put("operation", CAuditOperation.COMMIT_AUDIT);

        /** 调用审核公共方法 */
        auditEventService.audit(params);
    }

    @Override
    public AuditMerchantConfig getByEventId(Long eventId) {

        LambdaQueryWrapper<AuditMerchantConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditMerchantConfig::getAuditEventId, eventId);

        AuditMerchantConfig one = super.getOne(wrapper);

        return one;
    }


    @Override
    public void tradeClosePass(String customerNo) {
        MerchantConfig merchantConfig = merchantConfigService.getByCustomerNo(customerNo);
        /** 1.修改交易状态：关闭 */
        merchantConfig.setConsume(Constants.NO);
        /** 2.将通过审核的数据转存入正式表 */
        merchantConfigService.updateById(merchantConfig);
    }

    @Override
    public void settlementClosePass(String customerNo) {
        MerchantConfig merchantConfig = merchantConfigService.getByCustomerNo(customerNo);
        /** 1.修改结算状态：关闭 */
        //结算关闭
        merchantConfig.setSettlement(Constants.NO);
//        //提现关闭
//        merchantConfig.setWithdrawCash(Constants.NO);
//        //委托付款关闭
//        merchantConfig.setEntrustPayment(Constants.NO);
//        //购付汇关闭
//        merchantConfig.setPurchasePayment(Constants.NO);
//        //收结汇关闭
//        merchantConfig.setReceiveSettlement(Constants.NO);
        /** 2.将通过审核的数据转存入正式表 */
        merchantConfigService.updateById(merchantConfig);
    }

    @Override
    public void changeUpdatePass(Long auditEventId) {

        String currTime = DateUtil.now();

        /** 1.记录修改内容  */
        //根据{审核事件ID}从审核表中获取刚审核通过的记录。
        AuditMerchantConfig auditMerchantConfig = getByAuditEventId(auditEventId);
        //根据{客户号}从正式表中获取正式数据
        MerchantConfig formalData = merchantConfigService.getByCustomerNo(auditMerchantConfig.getCustomerNo());
        formalData.setUpdateTime(currTime);
        //对比最终修改内容并记录
        //对比之前先将不需要修改的数据统一成正式数据
        MerchantConfig tempData = new MerchantConfig();
        BeanUtils.copyProperties(auditMerchantConfig, tempData);
        tempData.setId(formalData.getId());
        tempData.setCreateTime(formalData.getCreateTime());
        tempData.setUpdateTime(formalData.getUpdateTime());
        //获取修改内容，并将修改内容保存到草稿表中
        String updateRecord = BeanChangeCompareUtil.compare(formalData, tempData);
        auditMerchantConfig.setUpdateRecord(updateRecord);
        updateById(auditMerchantConfig);
        /** 2.将通过审核的数据转存入正式表 */
        merchantConfigService.updateById(tempData);


        /** 3.对比是否有新开币种账户，如果有需推送交易系统开户*/
        //获取新币种
        String newCurrencyStr = auditMerchantConfig.getOpenedCurrencyAccount();
        if(newCurrencyStr.startsWith(",")){
            newCurrencyStr = newCurrencyStr.substring(1);
        }
        String[] newCurrency = newCurrencyStr.split(",");

        //获取老币种
        String oldCurrencyStr = formalData.getOpenedCurrencyAccount();
        if(oldCurrencyStr.startsWith(",")){
            oldCurrencyStr = oldCurrencyStr.substring(1);
        }
        String[] oldCurrency = oldCurrencyStr.split(",");

        //对比
        List<String> newCurrencyList = new ArrayList<>(Arrays.asList(newCurrency));
        List<String> oldCurrencyList = new ArrayList<>(Arrays.asList(oldCurrency));
        newCurrencyList.removeAll(oldCurrencyList);
        if(newCurrencyList.size()>0){
            MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(formalData.getCustomerNo());
            MerchantSettlementInfo settlementInfo = merchantSettlementInfoService.getByCustomerNo(merchantInfo.getCustomerNo());
            String bankCode;
            String branchBankNo = settlementInfo.getBranchBankNo();
            //如果包含字母，表明是老系统迁移数据，没有选择具体的开户行（或为国外的开户行），此字段直接存入的银行编码
            if(RegexUtil.containLetter(branchBankNo)){
                bankCode = branchBankNo;
            }else {
                BankCode bankCodeModel = bankCodeService.getCodeByBranchBankNo(settlementInfo.getBranchBankNo());
                bankCode = bankCodeModel.getBankCode();
            }


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
            data.put("bankCode", bankCode);
            data.put("list", newCurrencyList);

            logger.info("新开币种账户，推送交易系统："+ JSON.toJSONString(data));

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
                    throw new MobaoException("为交易服务推送新开币种,交易服务返回异常："+ result.getRetMsg());
                }
            }
            logger.info("为交易服务推送新开币种执行成功");
        }

    }


    private AuditMerchantConfig getByAuditEventId(Long auditEventId) {
        LambdaQueryWrapper<AuditMerchantConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditMerchantConfig::getAuditEventId, auditEventId);
        return getOne(wrapper);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {

        return super.removeById(id);
    }

    /**
     * 去重检查
     * @param auditMerchantConfig
     */
    private void accessJudgeRepeat(AuditMerchantConfig auditMerchantConfig) {
        AuditMerchantConfig repat = getOne(new LambdaQueryWrapper<AuditMerchantConfig>()
                .eq(AuditMerchantConfig::getAuditEventId, auditMerchantConfig.getAuditEventId())
                .eq(AuditMerchantConfig::getCustomerNo, auditMerchantConfig.getCustomerNo()));
        if (repat != null){
            throw new MobaoException("当前商户已配置商户配置,如需修改请调用商户配置修改接口");
        }
    }

    /**
     * 互斥事件检查
     * @param customerNo 客户号
     */
    private void mutexCheck(String customerNo){
        int count = auditMerchantConfigMapper.countByCustomerNoAndCAuditCAuditEventType(customerNo);
        if (count >= 1){
            throw new MobaoException("商户配置正在审核中，请稍后在提交新的审核。");
        }
    }

    /**
     * 根据商户配置结算状态 对配置做出相应的改变
     * @param auditMerchantConfig 审核配置
     */
    private void checkSettlementStatus(AuditMerchantConfig auditMerchantConfig){
        //结算
        String settlement = auditMerchantConfig.getSettlement();
        if (settlement == null || "".equals(settlement)){
            throw new MobaoException("结算字段不能为空");
        }
//        //提现
//        String withdrawCash = auditMerchantConfig.getWithdrawCash();
//        //委托付款
//        String entrustPayment = auditMerchantConfig.getEntrustPayment();
//        //购付汇
//        String purchasePayment = auditMerchantConfig.getPurchasePayment();
//        //收结汇
//        String receiveSettlement = auditMerchantConfig.getReceiveSettlement();
//
//        //结算开通时
//        if (Constants.YES.equals(settlement)) {
//            if (!(Constants.YES.equals(withdrawCash) || Constants.YES.equals(entrustPayment) || Constants.YES.equals(purchasePayment) || Constants.YES.equals(receiveSettlement))) {
//                throw new MobaoException("结算开通状态异常，请检查结算配置");
//            }
//        }else {
//            //结算关闭时
//            if (!(Constants.NO.equals(withdrawCash) && Constants.NO.equals(entrustPayment) && Constants.NO.equals(purchasePayment) && Constants.NO.equals(receiveSettlement))){
//                throw new MobaoException("结算关闭状态异常，请检查结算配置");
//            }
//        }
    }

}