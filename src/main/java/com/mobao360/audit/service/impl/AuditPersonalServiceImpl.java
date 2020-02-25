package com.mobao360.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.mobao360.api.feign.PersonalFeign;
import com.mobao360.api.feign.TradeFeign;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventFlow;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.audit.service.IAuditPersonalService;
import com.mobao360.system.constant.*;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/4/9 11:15
 */
@Service
@Log4j2
public class AuditPersonalServiceImpl implements IAuditPersonalService {

    @Autowired
    private IAuditEventFlowService auditEventFlowService;
    @Autowired
    private IAuditEventService auditEventService;
    @Autowired
    private ConfigConstant config;
    @Autowired
    private PersonalFeign personalFeign;
    @Autowired
    private TradeFeign tradeFeign;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void lock(Map<String, String> params) {

        String now = DateUtil.now();
        String customerNo = params.get("customerNo");
        if (customerNo == null) {
            throw new MobaoException("客户号不能为空");
        }

        //检查审核事件的唯一性
        //onlyCheck(accountNo);

        //查询商户账户锁定审核事件 初始节点
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.PERSONAL_LOCK, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        //生成审核事件
        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：个人锁定
        auditEvent.setAuditEventType(CAuditEventType.PERSONAL_LOCK);
        // 审核主体：客户号
        auditEvent.setSubject(customerNo);
        //事件主体编码：客户号
        auditEvent.setSubjectCode(customerNo);
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
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void unlock(Map<String, String> params) {

        String now = DateUtil.now();
        String customerNo = params.get("customerNo");
        if (customerNo == null) {
            throw new MobaoException("客户号不能为空");
        }

        //检查审核事件的唯一性
        //onlyCheck(accountNo);

        //查询商户账户锁定审核事件 初始节点
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.PERSONAL_UNLOCK, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        //生成审核事件
        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：个人解锁
        auditEvent.setAuditEventType(CAuditEventType.PERSONAL_UNLOCK);
        // 审核主体：客户号
        auditEvent.setSubject(customerNo);
        //事件主体编码：客户号
        auditEvent.setSubjectCode(customerNo);
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
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void logout(Map<String, String> params) {

        String now = DateUtil.now();
        String customerNo = params.get("customerNo");
        if (customerNo == null) {
            throw new MobaoException("客户号不能为空");
        }

        //检查审核事件的唯一性
        //onlyCheck(accountNo);
        //检查商户余额，商户余额>0不允许注销
        checkBalance(customerNo);

        //查询商户账户锁定审核事件 初始节点
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.PERSONAL_LOGOUT, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        //生成审核事件
        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：个人解锁
        auditEvent.setAuditEventType(CAuditEventType.PERSONAL_LOGOUT);
        // 审核主体：客户号
        auditEvent.setSubject(customerNo);
        //事件主体编码：客户号
        auditEvent.setSubjectCode(customerNo);
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
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void updatePersonalStatus(String customerNo, String status) {

        HashMap<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("custNo", customerNo);
        paramMap.put("status", status);

//        String resultStr;
//        try {
//            resultStr = HttpUtil.post(config.getPersonalIp() + "/purse-1.5.1/app/updateCustomerStatus", JSON.toJSONString(paramMap));
//            log.info("个人中心返回："+ resultStr);
//        }catch (Exception e){
//            throw new MobaoException("个人中心调用失败");
//        }
//
//        JSONObject result = JSONObject.parseObject(resultStr);
//
//        if(!Constants.YES.equals(result.getString("retCode"))){
//            throw new MobaoException("调用个人中心修改个人状态,返回异常："+ result.getString("retMsg"));
//        }

        Result result;
        try {
            result = personalFeign.updateCustomerStatus(paramMap);
        }catch (Exception e){
            throw new MobaoException("个人中心调用失败", e);
        }

        log.info("个人中心系统返回："+ JSON.toJSONString(result));
        if(!Constants.YES.equals(result.getRetCode())){
            throw new MobaoException("调用个人中心修改客户状态,返回异常："+ result.getRetMsg());
        }
        log.info("调用个人中心修改个人状态成功");
    }


    /**
     * 检查商户余额，商户余额>0不允许注销
     */
    private void checkBalance(String customerNo){

        Map<String, String> param = new HashMap<>(2);
        param.put("custNo", customerNo);

        Result result;
        try {
            result = tradeFeign.queryBal(param);
        }catch (Exception e){
            throw new MobaoException("交易系统调用失败", e);
        }
        log.info("商户注销，余额检查,交易系统返回结果："+ JSON.toJSONString(result));

        if(!Constants.YES.equals(result.getRetCode())){
            throw new MobaoException("商户注销，余额检查,交易系统返回异常："+ result.getRetMsg());
        }

        Map<String,Object> retData = (Map<String,Object>)result.getRetData();
        List<Map<String, Object>> balanceList = (List<Map<String, Object>>)retData.get("list");
        for (Map<String, Object> balance : balanceList) {
            Object bal = balance.get("bal");
            BigDecimal decBal = new BigDecimal(bal.toString());
            if(decBal.compareTo(BigDecimal.valueOf(0))==1){
                throw new MobaoException("该客户支付账户"+ balance.get("ccy") +"余额大于0，不可注销");
            }
        }
    }


}
