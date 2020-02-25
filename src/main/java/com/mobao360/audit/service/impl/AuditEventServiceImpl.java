package com.mobao360.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.mobao360.audit.entity.*;
import com.mobao360.audit.mapper.AuditEventMapper;
import com.mobao360.audit.service.*;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.system.constant.*;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.LoginUserInfoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-08
 */
@Service
public class AuditEventServiceImpl extends ServiceImpl<AuditEventMapper, AuditEvent> implements IAuditEventService {

    @Autowired
    private AuditEventMapper auditEventMapper;

    @Autowired
    private IAuditEventFlowService auditEventFlowService;

    @Autowired
    private IAuditEventDetailService auditEventDetailService;

    @Autowired
    private IAuditMerchantElectronicDataService auditMerchantElectronicDataService;

    @Autowired
    private IAuditMerchantCentreAccountService auditMerchantCentreAccountService;

    @Autowired
    private IAuditMerchantSettlementInfoService auditMerchantSettlementInfoService;

    @Autowired
    private IAuditMerchantConfigService auditMerchantConfigService;

    @Autowired
    private IAuditMerchantFeeRateService auditMerchantFeeRateService;

    @Autowired
    private IAuditMerchantInfoService auditMerchantInfoService;

    @Autowired
    private IAuditAgentBasicInfoService auditAgentBasicInfoService;

    @Autowired
    private IAuditMerchantAccountService auditMerchantAccountService;

    @Autowired
    private IAuditMerchantFundSettlementService auditMerchantFundSettlementService;

    @Autowired
    private IAuditMerchantRouteService auditMerchantRouteService;

    @Autowired
    private IAuditPersonalService auditPersonalService;

    /**
     * 所有的审核操作（11-提交审核，12-通过，13-修改并通过，01-退回，02-拒绝）
     * 都可直接调用此方法完成审核流程。
     *
     * 入参：{审核事件ID}，{审核操作}，{明细描述}。
     *
     * 	1：更新审核事件表
     * 	    1.1：根据{审核事件ID}获取审核事件当前信息
     * 		1.2：更新
     * 			判断操作结果
     *
     * 				“通过”-->判断节点
     * 					不是最终节点。
     * 						根据{审核事件ID}用【公共方法M3】获取当前节点的下一节点完整信息
     * 						赋值：{节点：置为下一节点、审核事件状态：02(审核中)、操作角色：下一节点操作角色、节点名称：下一节点名称}
     * 					是最终节点。
     * 						根据{审核事件ID}用【公共方法M2】获取当前操作节点完整信息
     * 						赋值：{节点：置为当前节点、审核事件状态：99(审核通过)、操作角色：当前节点操作角色、节点名称：当前节点名称}。
     * 						【重要操作：将数据转存入正式表，并重新生成更新时间......】
     *
     * 				“退回”-->判断节点
     * 					根据{审核事件ID}用【公共方法M4】获取当前操作节点上一节点完整信息
     * 					赋值：{节点-置为上一节点、审核事件状态：02(审核中)、操作角色：上一节点操作角色、节点名称：上一节点名称}
     *
     * 				“拒绝”
     * 					根据{审核事件ID}用【公共方法M2】获取当前操作节点完整信息
     * 					赋值：{节点：置为当前节点、审核事件状态：98(审核拒绝)、操作角色：当前节点操作角色、节点名称：当前节点名称}
     *
     * 				“提交审核”
     * 					根据{审核事件ID}用【公共方法M3】获取当前节点的下一节点完整信息
     * 					赋值：{节点：置为下一节点、审核事件状态：02(审核中)、操作角色：下一节点操作角色、节点名称：下一节点名称}
     *
     * 				“修改并通过”
     * 					根据{审核事件ID}用【公共方法M3】获取当前节点的下一节点完整信息
     * 					赋值：{节点：置为下一节点、审核事件状态：02(审核中)、操作角色：下一节点操作角色、节点名称：下一节点名称}
     *
     * 					修改记录：利用反射对比获取修改记录
     *
     * 		1.2：保存
     *
     * 	2：新增一条审核事件明细
     * 		2.1：根据{审核事件ID}用【公共方法M2】获取当前操作节点完整信息
     * 		2.2：赋值：
     * 			审核事件ID：{审核事件ID}
     * 			操作：通过/退回/拒绝
     * 			操作人员：当前登录操作人
     * 			节点名称：1.1步
     * 			描述：{明细描述}
     * 			修改记录：1.1修改记录
     * 			创建时间：当前时间
     *
     * 		2.3：保存
     * @param params
     * @return 是否已通过审核
     */
    @Override
    @LcnTransaction
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean audit(Map<String, String> params) {
        boolean ifPass = false;

        //入参：{审核事件ID}，{审核操作}，{明细描述}
        String auditEventId = params.get("auditEventId");
        String operation = params.get("operation");
        String description = params.get("description");
        if(StringUtils.isBlank(auditEventId)){
            throw new MobaoException("审核事件ID[auditEventId]不能为空");
        }
        if(StringUtils.isBlank(operation)){
            throw new MobaoException("审核操作[operation]不能为空");
        }
        if(StringUtils.isBlank(description)){
            throw new MobaoException("审核描述[description]不能为空");
        }

        /** 1.更新审核事件表 */
        //获取审核事件当前信息
        AuditEvent event = getById(auditEventId);
        String status = event.getStatus();
        if(CAuditEventStatus.PASS.equals(status) || CAuditEventStatus.REFUSED.equals(status) || CAuditEventStatus.SCRAP.equals(status)){
            throw new MobaoException("当前事件已审核结束，不可继续审核");
        }

        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());
        //判断当前登录人是否有审核资格
        List<String> userRoles = LoginUserInfoUtil.getUserRoles();
        if(!userRoles.contains(flow.getOperatorRole())){
            throw new MobaoException("对不起，您无此权限");
        }

        switch (operation){

            /** 提交审核 */
            case CAuditOperation.COMMIT_AUDIT:{

                /** 特殊情况校验 */
                // 若为"商户入网"且为第一个固定节点，则必须"商户基本资料"，"商户中心账号","商户结算信息","商户电子资料"数据都已新增
                if(CAuditEventType.MERCHANT_ACCESS_NETWORK.equals(event.getAuditEventType())){

                    AuditMerchantCentreAccount account = auditMerchantCentreAccountService.getByEventId(event.getId());
                    if(account == null){
                        throw new MobaoException("请完善[商户中心账号信息]后再提交审核");
                    }

                    AuditMerchantSettlementInfo settlementInfo = auditMerchantSettlementInfoService.getByEventId(event.getId());
                    if(settlementInfo == null){
                        throw new MobaoException("请完善[商户结算信息]后再提交审核");
                    }

                    AuditMerchantElectronicData electronicData = auditMerchantElectronicDataService.getByEventId(event.getId());
                    if(electronicData == null){
                        throw new MobaoException("请完善[商户电子资料]后再提交审核");
                    }
                }

                if(!Constants.AUDIT_EVENT_BEGIN_NODE_CODE.equals(event.getNodeCode())){
                    throw new MobaoException("只有初始节点才能提交审核");
                }

                //获取下一节点信息
                AuditEventFlow nextNode = auditEventFlowService.getNextNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

                //更新节点信息
                event.setStatus(CAuditEventStatus.AUDITING);
                event.setNodeCode(nextNode.getNodeCode());

                break;
            }

            /** 通过 */
            case CAuditOperation.PASS:{

                //判断是否为最终节点
                String maxNodeCode = auditEventFlowService.getMaxNodeCodeByType(event.getAuditEventType());
                //是最终节点
                if(event.getNodeCode().equals(maxNodeCode)){
                    //更新节点信息,状态设置为"通过",其余信息保持不变
                    event.setStatus(CAuditEventStatus.PASS);
                    ifPass = true;
                    //已通过审核，相应模块后续重要流程【重要操作：将数据转存入正式表，并重新生成更新时间......】

                //不是最终节点
                }else {

                    /** 特殊情况校验 */
                    // 若为"商户入网"且为第三个固定节点，则必须"商户配置"，"商户费率"数据都已新增
                    if(Constants.AUDIT_EVENT_THIRD_NODE_CODE.equals(event.getNodeCode())
                            && CAuditEventType.MERCHANT_ACCESS_NETWORK.equals(event.getAuditEventType())){

                        AuditMerchantConfig config = auditMerchantConfigService.getByEventId(event.getId());
                        if(config == null){
                            throw new MobaoException("请完善[商户配置信息]后再通过审核");
                        }

                        List<AuditMerchantFeeRate> feeRate = auditMerchantFeeRateService.getByEventId(event.getId());
                        if(feeRate.size() == 0){
                            throw new MobaoException("请完善[商户费率]后再通过审核");
                        }
                    }

                    //获取下一节点信息
                    AuditEventFlow nextNode = auditEventFlowService.getNextNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

                    //更新节点信息
                    event.setStatus(CAuditEventStatus.AUDITING);
                    event.setNodeCode(nextNode.getNodeCode());
                }

                break;
            }

            /** 退回 */
            case CAuditOperation.RETURN:{
                //获取上一节点信息(注意：退回上一固定节点)
                AuditEventFlow lastNode = auditEventFlowService.getLastNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

                //更新节点信息
                event.setStatus(CAuditEventStatus.AUDITING);
                event.setNodeCode(lastNode.getNodeCode());

                break;
            }

            /** 拒绝 */
            case CAuditOperation.REFUSED:{
                //更新节点信息,状态设置为"拒绝",其余信息保持不变
                event.setStatus(CAuditEventStatus.REFUSED);

                break;
            }

            /** 作废 */
            case CAuditOperation.SCRAP:{
                //更新节点信息,状态设置为"作废",其余信息保持不变
                event.setStatus(CAuditEventStatus.SCRAP);

                break;
            }

            default:{
                throw new MobaoException("没有此审核操作！");
            }

        }
        //保存审核事件信息
        String currTime = DateUtil.now();
        event.setUpdateTime(currTime);
        updateById(event);

        /** 2.生成一条审核事件明细 */
        AuditEventDetail detail = new AuditEventDetail();
        //是审核事件明细，非日志记录
        detail.setIfLog(Constants.NO);
        detail.setAuditEventId(event.getId());
        detail.setOperation(operation);
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setNodeCode(flow.getNodeCode());
        detail.setNodeName(flow.getNodeName());
        detail.setDescription(description);
        //用于记录一些状态变更事件对应实体的原始状态
        detail.setUpdateRecord(params.get("originalStatus"));
        detail.setCreateTime(currTime);

        //保存明细信息
        auditEventDetailService.save(detail);

        return ifPass;
    }


    @Override
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public void auditMax(Map<String, String> params) {

        boolean pass = audit(params);
        if(pass){
            passHandle(params);
        }
    }

    /**
     *  商户中心合作商户资料审核
     *  入参：{客户号_customerNo}，{审核操作_operation}（通过-99，拒绝-98），{审核意见_auditAdvice}。
     * @param params
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public void merchantCentreAudit(Map<String, String> params) {

        String customerNo = params.get("customerNo");
        String operation = params.get("operation");
        String auditAdvice = params.get("auditAdvice");
        if(StringUtils.isBlank(customerNo)){
            throw new MobaoException("客户号[customerNo]不能为空");
        }
        if(StringUtils.isBlank(operation)){
            throw new MobaoException("审核操作[operation]不能为空");
        }
        if(StringUtils.isBlank(auditAdvice)){
            throw new MobaoException("审核意见[auditAdvice]不能为空");
        }


        LambdaQueryWrapper<AuditMerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditMerchantInfo::getCustomerNo, customerNo);
        AuditMerchantInfo entity = new AuditMerchantInfo();
        entity.setAuditAdvice(auditAdvice);

        //通过。改状态，存意见，草稿转正式(还需新增草稿中不存在的配置信息)
        if("99".equals(operation)){
            // 状态置为通过
            entity.setStatus("99");
            auditMerchantInfoService.update(entity, wrapper);

            //草稿转正式(还需新增草稿中不存在的配置信息)
            auditMerchantInfoService.merchantCentreAuditPass(customerNo);



        //拒绝。改状态，存意见。
        }else {
            // 状态置为拒绝
            entity.setStatus("98");
            auditMerchantInfoService.update(entity, wrapper);
        }


    }

    /**
     * 根据审核事件ID删除审核事件
     * 只有在草稿状态下才能删除
     *
     * @param eventId
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean removeByEventId(Long eventId) {

        //只有在草稿状态下才能删除
        AuditEvent event = getById(eventId);
        String eventStatus = event.getStatus();
        if(!CAuditEventStatus.DRAFT.equals(eventStatus)){
            throw new MobaoException("非草稿状态，不能删除");
        }
        event.setStatus(CAuditEventStatus.DELETE);
        //删除"审核事件"
        updateById(event);


        /** 2.生成一条审核事件明细 */
        AuditEventDetail detail = new AuditEventDetail();
        //删除操作，日志记录
        detail.setIfLog(Constants.YES);
        detail.setAuditEventId(event.getId());
        detail.setOperation("de");
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setCreateTime(DateUtil.now());
        auditEventDetailService.save(detail);

        return true;
    }

    @Override
    public List<Map<String, String>> pageQuery(Map<String, String> params) {

        Map<String, Object> paramsObj = new HashMap<>(8);
        paramsObj.putAll(params);

        //获取当前登录人角色集
        List<String> loginRoles = LoginUserInfoUtil.getUserRoles();
        paramsObj.put("loginRoles", loginRoles);

        //根据当前登录人角色集，获取其参与的所有事件
        List<Map<String, String>> eventList = auditEventMapper.pageQuery(paramsObj);
        if(eventList!=null && eventList.size()!=0){

            for (Map<String, String> event : eventList) {
                //根据事件状态及登录人处理按钮权限
                //判断事件是否处于终态
                String status = event.get("status");
                if(CAuditEventStatus.PASS.equals(status) ||
                        CAuditEventStatus.REFUSED.equals(status) ||
                        CAuditEventStatus.SCRAP.equals(status)){

                    //如果事件已处于终态，则操作权限只有"详情"
                    event.put("operatorPermission", "buttonEventDetails");
                    event.put("buttonPermission", null);
                }else {

                    //判断事件是否在当前的登录人角色上
                    String eventRole = event.get("operatorRole");
                    if(!loginRoles.contains(eventRole)){
                        //事件不在当前登录人角色上，则操作权限只有"详情"
                        event.put("operatorPermission", "buttonEventDetails");
                        event.put("buttonPermission", null);
                    }
                }

                /**
                 * 单独判断是否返回"补充电子资料"按钮。由于与上面的逻辑有冲突，所以单独判断
                 * 3个条件
                 * 1.状态为"审核中"（草稿状态用修改按钮处理电子资料）
                 * 2.入网事件or电子资料修改事件
                 * 3.当前登录人拥有第一节点角色
                 */
                String auditEventType = event.get("auditEventType");
                if(CAuditEventStatus.AUDITING.equals(status)
                        &&(CAuditEventType.MERCHANT_ACCESS_NETWORK.equals(auditEventType)||CAuditEventType.MERCHANT_ELECTRONIC_DATA_CHANGE.equals(auditEventType))){

                    AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(auditEventType, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
                    if(loginRoles.contains(flow.getOperatorRole())){
                        String operatorPermission = event.get("operatorPermission");
                        String nodeCode = event.get("nodeCode");
                        //由于退回第一个节点后，产品规定事件状态为“审核中”，还需单独处理这种情况
                        if(!Constants.AUDIT_EVENT_BEGIN_NODE_CODE.equals(nodeCode)){
                            event.put("operatorPermission", "buttonSupplement," + operatorPermission);
                        }
                    }
                }
            }
        }
        DictionaryUtil.keyValueHandle(eventList);

        return eventList;
    }


    /**
     * 为其他服务新增审核事件创建此方法
     * @param auditEvent
     * @return
     */
    @Override
    @LcnTransaction
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean create(AuditEvent auditEvent) {

        String currTime = DateUtil.now();

        /** 1.生成审核事件 */
        // 查询“商户锁定”事件初始节点信息，以备后续赋值
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
                auditEvent.getAuditEventType(), Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

        //流程权限校验
        checkFlowPermission(flow);

        // 事件状态：草稿
        auditEvent.setStatus(CAuditEventStatus.DRAFT);
        // 节点信息赋值
        auditEvent.setNodeCode(flow.getNodeCode());
        auditEvent.setCreateTime(currTime);
        auditEvent.setUpdateTime(currTime);

        // 保存审核事件
        save(auditEvent);

        Map<String, String> params = new HashMap<>(3);
        params.put("auditEventId", auditEvent.getId().toString());
        params.put("operation", auditEvent.getOperation());
        params.put("description", auditEvent.getDescription());

        /** 2.调用审核公共方法 */
        return audit(params);
    }

    @Override
    public int countDoing() {

        //获取当前登录人角色集
        List<String> loginRoles = LoginUserInfoUtil.getUserRoles();
        int num = auditEventMapper.countDoing(loginRoles);

        return num;
    }

    
    @Override
    public void checkFlowPermission(AuditEventFlow flow) {
        if(flow == null){
            throw new MobaoException("请配置对应的审核流程");
        }

        String operatorRole = flow.getOperatorRole();
        List<String> userRoles = LoginUserInfoUtil.getUserRoles();
        if(!userRoles.contains(operatorRole)){
            throw new MobaoException("对不起，您无此权限");
        }
    }


    private void passHandle(Map<String, String> params){

        Long eventId = Long.valueOf(params.get("auditEventId"));
        AuditEvent event = getById(eventId);
        String auditEventType = event.getAuditEventType();

        /** 判断事件类型，不同审核事件类型审核通过后有不同的后续处理流程*/
        switch (auditEventType){

            /** 商户入网 */
            case CAuditEventType.MERCHANT_ACCESS_NETWORK:{
                auditMerchantInfoService.accessNetworkPass(eventId);
                break;
            }

            /** 商户基本信息修改 */
            case CAuditEventType.MERCHANT_INFO_UPDATE:{
                auditMerchantInfoService.changeUpdatePass(eventId);
                break;
            }

            /** 商户锁定 */
            case CAuditEventType.MERCHANT_LOCK:{
                String customerNo = event.getSubjectCode();
                auditMerchantInfoService.lockPass(customerNo);
                break;
            }

            /** 商户解锁 */
            case CAuditEventType.MERCHANT_UNLOCK:{
                String customerNo = event.getSubjectCode();
                auditMerchantInfoService.unlockPass(customerNo);
                break;
            }

            /** 商户注销 */
            case CAuditEventType.MERCHANT_LOGOUT:{
                String customerNo = event.getSubjectCode();
                auditMerchantInfoService.logoutPass(customerNo);
                break;
            }

            /** 代理商资料变更 */
            case CAuditEventType.AGENT_CHANGE:{
                auditAgentBasicInfoService.changePass(eventId);
                break;
            }

            /** 支付账户锁定 */
            case CAuditEventType.MERCHANT_ACCOUNT_LOCK:{
                String accountNo = event.getSubjectCode();
                auditMerchantAccountService.updateAccountStatus(accountNo, "5");
                break;
            }

            /** 支付账户解锁 */
            case CAuditEventType.MERCHANT_ACCOUNT_UNLOCK:{
                String accountNo = event.getSubjectCode();
                auditMerchantAccountService.updateAccountStatus(accountNo, "1");
                break;
            }

            /** 支付账户注销 */
            case CAuditEventType.MERCHANT_ACCOUNT_LOGOUT:{
                String accountNo = event.getSubjectCode();
                auditMerchantAccountService.updateAccountStatus(accountNo, "2");
                break;
            }

            /** 个人锁定 */
            case CAuditEventType.PERSONAL_LOCK:{
                String customerNo = event.getSubjectCode();
                auditPersonalService.updatePersonalStatus(customerNo, "2");
                break;
            }

            /** 个人解锁 */
            case CAuditEventType.PERSONAL_UNLOCK:{
                String customerNo = event.getSubjectCode();
                auditPersonalService.updatePersonalStatus(customerNo, "1");
                break;
            }

            /** 个人注销 */
            case CAuditEventType.PERSONAL_LOGOUT:{
                String customerNo = event.getSubjectCode();
                auditPersonalService.updatePersonalStatus(customerNo, "4");
                break;
            }

            /** 商户中心账号修改 */
            case CAuditEventType.MERCHANT_CENTRE_ACCOUNT_UPDATE:{
                auditMerchantCentreAccountService.changeUpdatePass(eventId);
                break;
            }

            /** 商户配置变更 */
            case CAuditEventType.MERCHANT_CONFIG_UPDATE:{
                auditMerchantConfigService.changeUpdatePass(eventId);
                break;
            }

            /** 商户交易关闭 */
            case CAuditEventType.MERCHANT_TRADE_CLOSE:{
                String customerNo = event.getSubjectCode();
                auditMerchantConfigService.tradeClosePass(customerNo);
                break;
            }

            /** 商户结算关闭 */
            case CAuditEventType.MERCHANT_SETTLEMENT_CLOSE:{
                String customerNo = event.getSubjectCode();
                auditMerchantConfigService.settlementClosePass(customerNo);
                break;
            }

            /** 商户费率变更 */
            case CAuditEventType.MERCHANT_FEERATE_CHANGE:{
                auditMerchantFeeRateService.changePass(eventId);
                break;
            }

            /** 商户即时结算变更 */
            case CAuditEventType.MERCHANT_ADVANCE_SETTLEMENT_CHANGE:{
                auditMerchantFundSettlementService.changePass(eventId);
                break;
            }

            /** 商户指定路由变更 */
            case CAuditEventType.MERCHANT_ROUTE_CHANGE:{
                auditMerchantRouteService.changePass(eventId);
                break;
            }

            /** 商户结算信息修改 */
            case CAuditEventType.MERCHANT_SETTLEMENT_UPDATE:{
                auditMerchantSettlementInfoService.changeUpdatePass(eventId);
                break;
            }

            /** 商户电子资料修改 */
            case CAuditEventType.MERCHANT_ELECTRONIC_DATA_CHANGE:{
                auditMerchantElectronicDataService.changeUpdatePass(eventId);
                break;
            }

            default:{
                throw new MobaoException("没有此审核类型！");
            }

        }
    }








}
