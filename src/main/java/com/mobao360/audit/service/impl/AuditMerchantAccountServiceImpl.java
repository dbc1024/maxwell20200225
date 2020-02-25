package com.mobao360.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.mobao360.api.feign.TradeFeign;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventFlow;
import com.mobao360.audit.mapper.AuditEventMapper;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.audit.service.IAuditMerchantAccountService;
import com.mobao360.system.constant.*;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Miskai
 * @date 2019/3/7
 */
@Service
public class AuditMerchantAccountServiceImpl implements IAuditMerchantAccountService {
    private Logger logger = LoggerFactory.getLogger(AuditMerchantAccountServiceImpl.class);

    @Autowired
    private IAuditEventFlowService auditEventFlowService;
    @Autowired
    private IAuditEventService auditEventService;
    @Autowired
    private AuditEventMapper auditEventMapper;
    @Autowired
    private TradeFeign tradeFeign;

    /**
     * 商户账户锁定
     *
     * @param params accountNo：账户号，description：申请意见
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void lock(Map<String, String> params) {
        String now = DateUtil.now();
        String accountNo = params.get("accountNo");
        if (accountNo == null) {
            throw new MobaoException("账户号不能为空");
        }
        //检查审核事件的唯一性
        onlyCheck(accountNo);
        //查询商户账户锁定审核事件 初始节点
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.MERCHANT_ACCOUNT_LOCK, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        //生成审核事件
        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：商户账户锁定
        auditEvent.setAuditEventType(CAuditEventType.MERCHANT_ACCOUNT_LOCK);
        // 审核主体：商户名称
        auditEvent.setSubject(accountNo);
        //事件主体编码：商户账号
        auditEvent.setSubjectCode(accountNo);
        // 事件状态：草稿
        auditEvent.setStatus(CAuditEventStatus.DRAFT);
        // 节点信息赋值
        auditEvent.setNodeCode(flow.getNodeCode());
        auditEvent.setCreateTime(now);
        auditEvent.setUpdateTime(now);

        // 保存审核事件
        auditEventService.save(auditEvent);
        params.put("auditEventId", auditEvent.getId().toString());
        params.put("operation", CAuditOperation.COMMIT_AUDIT);
        auditEventService.audit(params);
    }

    /**
     * 商户账户解锁
     *
     * @param params accountNo：账户号，description：申请意见
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void unlock(Map<String, String> params) {
        String now = DateUtil.now();
        String accountNo = params.get("accountNo");
        if (accountNo == null) {
            throw new MobaoException("账户号不能为空");
        }
        //检查审核事件的唯一性
        onlyCheck(accountNo);
        //查询商户账户解锁审核事件 初始节点
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.MERCHANT_ACCOUNT_UNLOCK, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        //生成审核事件
        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：商户账户锁定
        auditEvent.setAuditEventType(CAuditEventType.MERCHANT_ACCOUNT_UNLOCK);
        // 审核主体：商户名称
        auditEvent.setSubject(accountNo);
        //事件主体编码：商户账号
        auditEvent.setSubjectCode(accountNo);
        // 事件状态：草稿
        auditEvent.setStatus(CAuditEventStatus.DRAFT);
        // 节点信息赋值
        auditEvent.setNodeCode(flow.getNodeCode());
        auditEvent.setCreateTime(now);
        auditEvent.setUpdateTime(now);

        // 保存审核事件
        auditEventService.save(auditEvent);
        params.put("auditEventId", auditEvent.getId().toString());
        params.put("operation", CAuditOperation.COMMIT_AUDIT);
        auditEventService.audit(params);
    }

    /**
     * 商户账注销
     *
     * @param params accountNo：账户号，description：申请意见
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void logout(Map<String, String> params) {
        String now = DateUtil.now();
        String accountNo = params.get("accountNo");
        if (accountNo == null) {
            throw new MobaoException("账户号不能为空");
        }
        //检查审核事件的唯一性
        onlyCheck(accountNo);
        //查询商户账户锁定审核事件 初始节点
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.MERCHANT_ACCOUNT_LOGOUT, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        //生成审核事件
        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：商户账户锁定
        auditEvent.setAuditEventType(CAuditEventType.MERCHANT_ACCOUNT_LOGOUT);
        // 审核主体：商户名称
        auditEvent.setSubject(accountNo);
        //事件主体编码：商户账号
        auditEvent.setSubjectCode(accountNo);
        // 事件状态：草稿
        auditEvent.setStatus(CAuditEventStatus.DRAFT);
        // 节点信息赋值
        auditEvent.setNodeCode(flow.getNodeCode());
        auditEvent.setCreateTime(now);
        auditEvent.setUpdateTime(now);

        // 保存审核事件
        auditEventService.save(auditEvent);
        params.put("auditEventId", auditEvent.getId().toString());
        params.put("operation", CAuditOperation.COMMIT_AUDIT);
        auditEventService.audit(params);
    }


    @Override
    public void updateAccountStatus(String accountNo, String status){
        Map<String, String> params = new HashMap<>(2);
        params.put("acctNo", accountNo);
        params.put("status", status);

        Result result;
        try {
            result = tradeFeign.updateAccountStatus(params);
        }catch (Exception e){
            throw new MobaoException("交易服务调用失败", e);
        }

        logger.info("交易系统返回："+ JSON.toJSONString(result));
        if(!Constants.YES.equals(result.getRetCode())){
            throw new MobaoException("交易服务返回异常："+ result.getRetMsg());
        }
    }

    /**
     * 审核事件统计
     *
     * @param accountNo 账户号
     */
    private void onlyCheck(String accountNo) {
        List<String> auditEventTypes = new ArrayList<>();
        auditEventTypes.add(CAuditEventType.MERCHANT_ACCOUNT_LOCK);
        auditEventTypes.add(CAuditEventType.MERCHANT_ACCOUNT_UNLOCK);
        auditEventTypes.add(CAuditEventType.MERCHANT_ACCOUNT_LOGOUT);
        int count = auditEventMapper.countBySubjectCodeAndCAuditCAuditEventType(accountNo, auditEventTypes);
        if (count > 0) {
            throw new MobaoException("账户已在审核中，请在审核通过之后，再提交新的审核申请。");
        }
    }
}
