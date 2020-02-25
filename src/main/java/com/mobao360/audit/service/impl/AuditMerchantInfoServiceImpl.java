package com.mobao360.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.api.feign.RiskFeign;
import com.mobao360.api.feign.TradeFeign;
import com.mobao360.api.feign.UserFeign;
import com.mobao360.audit.entity.*;
import com.mobao360.audit.mapper.AuditMerchantInfoMapper;
import com.mobao360.audit.service.*;
import com.mobao360.customer.entity.*;
import com.mobao360.customer.service.*;
import com.mobao360.system.constant.*;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.*;
import com.mobao360.task.InitTaskData;
import com.mobao360.task.PushDataTask;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-03
 */
@Service
public class AuditMerchantInfoServiceImpl extends ServiceImpl<AuditMerchantInfoMapper, AuditMerchantInfo> implements IAuditMerchantInfoService {
    private Logger logger = LoggerFactory.getLogger(AuditMerchantInfoServiceImpl.class);

    @Autowired
    private AuditMerchantInfoMapper auditMerchantInfoMapper;
    @Autowired
    private IAuditEventService auditEventService;
    @Autowired
    private IAuditEventFlowService auditEventFlowService;
    @Autowired
    private IMerchantInfoService merchantInfoService;
    @Autowired
    private IAuditEventDetailService auditEventDetailService;
    @Autowired
    private IAuditMerchantElectronicDataService auditMerchantElectronicDataService;

    @Autowired
    private IMerchantElectronicDataService merchantElectronicDataService;

    @Autowired
    private IAuditMerchantCentreAccountService auditMerchantCentreAccountService;

    @Autowired
    private IMerchantCentreAccountService merchantCentreAccountService;
    
    @Autowired
    private IAuditMerchantConfigService auditMerchantConfigService;

    @Autowired
    private IMerchantConfigService merchantConfigService;

    @Autowired
    private IAuditMerchantSettlementInfoService auditMerchantSettlementInfoService;

    @Autowired
    private IMerchantSettlementInfoService merchantSettlementInfoService;

    @Autowired
    private IAuditMerchantFeeRateService auditMerchantFeeRateService;

    @Autowired
    private IMerchantFeeRateService merchantFeeRateService;

    @Autowired
    private RiskFeign riskFeign;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private TradeFeign tradeFeign;




    @Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(AuditMerchantInfo auditMerchantInfo) {

        return super.save(auditMerchantInfo);
    }

    /**
     * 入网新增商户草稿信息
     *
     * 1.赋值分支机构号
     * 2.生成客户号
     * 3.生成审核事件
     * 4.生成一条明细日志记录
     * 5.将“审核事件ID”回填到商户信息中
     * 6.新增商户基本信息草稿
     *
     * @param auditMerchantInfo
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean accessNetworkCreate(AuditMerchantInfo auditMerchantInfo) {
        //加密敏感信息
        encrypt(auditMerchantInfo);
        //黑名单校验
        blacklistCheck(auditMerchantInfo);
        //重复要素校验
        checkInfoRepeat(auditMerchantInfo);

        String currTime = DateUtil.now();
        auditMerchantInfo.setCreateTime(currTime);
        auditMerchantInfo.setUpdateTime(currTime);

        /** 1.赋值分支机构号 */
        //todo 现阶段没有分支机构，所以写死。若存在分支机构，应先做判断，再赋值对应的分支机构号。
        auditMerchantInfo.setBranchNo(CBranchNo.MOBAO);

        /** 2.生成客户号 */
        String customerNo = generateCustomerNo();
        auditMerchantInfo.setCustomerNo(customerNo);

        /** 3.生成审核事件 */
        // 查询“入网事件”初始节点信息，以备后续赋值
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
                CAuditEventType.MERCHANT_ACCESS_NETWORK, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

        //流程权限校验
        auditEventService.checkFlowPermission(flow);


        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：商户入网
        auditEvent.setAuditEventType(CAuditEventType.MERCHANT_ACCESS_NETWORK);
        // 审核主体：商户名称
        auditEvent.setSubject(auditMerchantInfo.getName());
        //事件主体编码
        auditEvent.setSubjectCode(auditMerchantInfo.getCustomerNo());
        // 事件状态：草稿
        auditEvent.setStatus(CAuditEventStatus.DRAFT);
        // 节点信息赋值
        auditEvent.setNodeCode(flow.getNodeCode());
        auditEvent.setCreateTime(currTime);
        auditEvent.setUpdateTime(currTime);

        // 保存审核事件
        auditEventService.save(auditEvent);

        /** 4.生成一条明细日志记录 */
        AuditEventDetail detail = new AuditEventDetail();
        //是日志记录，非审核流程记录。
        detail.setIfLog(Constants.YES);
        detail.setAuditEventId(auditEvent.getId());
        detail.setOperation(CAuditOperation.CREATE);
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setNodeCode(flow.getNodeCode());
        detail.setNodeName(flow.getNodeName());
        detail.setUpdateRecord(auditMerchantInfo.toString());
        detail.setDescription("入网新增商户基本资料");
        detail.setCreateTime(currTime);
        //保存明细日志记录
        auditEventDetailService.save(detail);

        /** 5.将“审核事件ID”回填到商户信息中 */
        auditMerchantInfo.setAuditEventId(auditEvent.getId());


        /** 6.新增商户基本信息草稿 */
        return save(auditMerchantInfo);
    }

    /**
     * 此方法用于：商户中心创建“平台商户”下的“合作商户”
     *
     * 复用已存在的草稿表来存储，
     * 由于商户中心入网的平台商户的审核没有审核流程，只是复核改一下状态
     * 所以不生成审核事件，这样也不破坏运营审核流程
     * 由于原始审核流程中的草稿表是通过事件ID关联的，所以运营审核流程不会感知商户中心新增在草稿表中的数据
     * @param auditMerchantInfo
     * @return
     */
    @Override
    public boolean merchantCentreCreate(AuditMerchantInfo auditMerchantInfo) {

        //加密敏感信息
        encrypt(auditMerchantInfo);
        //黑名单校验
        blacklistCheck(auditMerchantInfo);

        String currTime = DateUtil.now();
        //updateTime作为提交时间，在提交审核时赋值
        auditMerchantInfo.setCreateTime(currTime);
        //草稿状态 {01-草稿, 02-待审核, 99-通过, 98-拒绝}
        auditMerchantInfo.setStatus("01");

        /** 1.赋值分支机构号 */
        //todo 现阶段没有分支机构，所以写死。若存在分支机构，应先做判断，再赋值对应的分支机构号。
        auditMerchantInfo.setBranchNo(CBranchNo.MOBAO);
        //赋值平台商户号
        String platformCustomerNo = NetUtil.getCustomerNoFromHeader();
        if(StringUtils.isBlank(platformCustomerNo)){
            throw new MobaoException("平台客户号不能为空");
        }
        auditMerchantInfo.setPlatformCustomerNo(platformCustomerNo);


        /** 2.生成客户号 */
        String customerNo = generateCustomerNo();
        auditMerchantInfo.setCustomerNo(customerNo);

        /** 3.新增商户基本信息草稿 */
        return save(auditMerchantInfo);
    }

    @Override
    public boolean merchantCentreUpdate(AuditMerchantInfo auditMerchantInfo) {
        //加密敏感信息
        encrypt(auditMerchantInfo);
        //黑名单校验
        blacklistCheck(auditMerchantInfo);
        //草稿状态
        auditMerchantInfo.setStatus("01");

        //赋值平台商户号
        String platformCustomerNo = NetUtil.getCustomerNoFromHeader();
        if(StringUtils.isBlank(platformCustomerNo)){
            throw new MobaoException("平台客户号不能为空");
        }
        auditMerchantInfo.setPlatformCustomerNo(platformCustomerNo);

        return super.updateById(auditMerchantInfo);
    }

    /**
     * 商户重复要素判断
     * @param accessMerchant
     * @return
     */
    private void checkInfoRepeat(AuditMerchantInfo accessMerchant){

        List<AuditMerchantInfo> repeat = auditMerchantInfoMapper.checkInfoRepeat(accessMerchant);

        if(repeat!=null && repeat.size()>0){
            AuditMerchantInfo oneRepeat = repeat.get(0);
            if(accessMerchant.getName().equals(oneRepeat.getName())){
                throw new MobaoException("已有待审商户使用此商户名称");
            }
            if(accessMerchant.getBusinessLicenceNo().equals(oneRepeat.getBusinessLicenceNo())){
                throw new MobaoException("已有待审商户使用此营业执照号");
            }
        }


        LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(accessMerchant.getCustomerNo())){
            wrapper.ne(MerchantInfo::getCustomerNo, accessMerchant.getCustomerNo());
        }
        wrapper.isNull(MerchantInfo::getPlatformCustomerNo);
        wrapper.and(i->i.eq(MerchantInfo::getName, accessMerchant.getName())
                .or().eq(MerchantInfo::getBusinessLicenceNo, accessMerchant.getBusinessLicenceNo()));

        MerchantInfo formal = merchantInfoService.getOne(wrapper);
        if(formal!=null){
            if(accessMerchant.getName().equals(formal.getName())){
                throw new MobaoException("已有商户使用此商户名称");
            }
            if(accessMerchant.getBusinessLicenceNo().equals(formal.getBusinessLicenceNo())){
                throw new MobaoException("已有商户使用此营业执照号");
            }
        }

    }


    /**
     * "商户基本资料修改"时，新增一条草稿数据。
     * 数据来源于正式表中的数据，需要将id清空。
     *
     * 1.清空id
     * 2.生成审核事件
     * 3.生成一条明细日志记录
     * 4.将“审核事件ID”回填到商户信息中
     * 5.新增商户基本草稿信息
     *
     * @param auditMerchantInfo
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean changeUpdate(AuditMerchantInfo auditMerchantInfo) {

        //加密敏感信息
        encrypt(auditMerchantInfo);

        //黑名单校验
        blacklistCheck(auditMerchantInfo);

        //状态检查（只有在商户处于非注销的状态下才能进行资料变更事件）
        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(auditMerchantInfo.getCustomerNo());
        if (merchantInfo != null) {
            if (merchantInfo.getStatus().equals(CMerchantStatus.LOGOUT)) {
                throw new MobaoException("商户已注销，不能对其资料修改。");
            }
        }else {
            throw new MobaoException("商户不存在，请检查商户号。");
        }

        //若为商户资料变更操作，需要确保一个商户只能存在一个正在审核的商户资料变更事件
        ArrayList<String> cAuditEventTypes = new ArrayList<>();
        cAuditEventTypes.add(CAuditEventType.MERCHANT_INFO_UPDATE);
        int updateCount = auditMerchantInfoMapper.countByCustomerNoAndCAuditCAuditEventType(auditMerchantInfo.getCustomerNo(),cAuditEventTypes);

        if (updateCount >= 1){
            throw new MobaoException("商户已处于修改审核中，请与审核通过后再提交新的修改审核");
        }

        //重复要素校验
        checkInfoRepeat(auditMerchantInfo);

        String currTime = DateUtil.now();
        auditMerchantInfo.setCreateTime(currTime);
        auditMerchantInfo.setUpdateTime(currTime);

        /** 1.清空id */
        auditMerchantInfo.setId(null);

        /** 2.生成审核事件 */
        // 查询“商户基本资料修改”事件初始节点信息，以备后续赋值
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
                CAuditEventType.MERCHANT_INFO_UPDATE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：商户入网
        auditEvent.setAuditEventType(CAuditEventType.MERCHANT_INFO_UPDATE);
        // 审核主体：商户名称
        auditEvent.setSubject(auditMerchantInfo.getName());
        //事件主体编码
        auditEvent.setSubjectCode(auditMerchantInfo.getCustomerNo());
        // 事件状态：草稿
        auditEvent.setStatus(CAuditEventStatus.DRAFT);
        // 节点信息赋值
        auditEvent.setNodeCode(flow.getNodeCode());
        auditEvent.setCreateTime(currTime);
        auditEvent.setUpdateTime(currTime);

        // 保存审核事件
        auditEventService.save(auditEvent);

        /** 3.生成一条明细日志记录 */
        AuditEventDetail detail = new AuditEventDetail();
        //是日志记录，非审核流程记录。
        detail.setIfLog(Constants.YES);
        detail.setAuditEventId(auditEvent.getId());
        detail.setOperation(CAuditOperation.CREATE);
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setNodeCode(flow.getNodeCode());
        detail.setNodeName(flow.getNodeName());
        detail.setUpdateRecord(auditMerchantInfo.toString());
        detail.setDescription("变更修改商户基本资料");
        detail.setCreateTime(currTime);
        //保存明细日志记录
        auditEventDetailService.save(detail);

        /** 4.将“审核事件ID”回填到商户信息中 */
        auditMerchantInfo.setAuditEventId(auditEvent.getId());

        /** 5.新增商户基本草稿信息 */
        return save(auditMerchantInfo);
    }

    /**
     * 商户锁定
     * 传入参数：客户号customerNo，明细描述description
     * @param params
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public void lock(Map<String, String> params) {

        String currTime = DateUtil.now();
        String customerNo = params.get("customerNo");
        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);

        //商户状态检查
        if (merchantInfo.getStatus().equals(CMerchantStatus.LOCKED)){
            throw new MobaoException("商户已经锁定。");
        }else if (merchantInfo.getStatus().equals(CMerchantStatus.LOGOUT)){
            throw new MobaoException("商户已经注销，不能进行此操作。");
        }
        //若为商户锁定操作，需要确保当前商户不处于解锁,注销审核事件的审核中，且商户只存在一个锁定事件
        mutexCheck(customerNo);

        /** 1.生成审核事件 */
        // 查询“商户锁定”事件初始节点信息，以备后续赋值
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
                CAuditEventType.MERCHANT_LOCK, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：商户锁定
        auditEvent.setAuditEventType(CAuditEventType.MERCHANT_LOCK);
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

        // 保存审核事件
        auditEventService.save(auditEvent);

        params.put("auditEventId", auditEvent.getId().toString());
        params.put("operation", CAuditOperation.COMMIT_AUDIT);

        /** 调用审核公共方法 */
        auditEventService.audit(params);


    }

    /**
     * 商户解锁
     * 传入参数：客户号customerNo，明细描述description
     * @param params
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public void unlock(Map<String, String> params) {

        String currTime = DateUtil.now();
        String customerNo = params.get("customerNo");
        //解锁事件必需保证当前商户处于锁定并且不为注销状态
        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
        //商户状态检查
        if (merchantInfo.getStatus().equals(CMerchantStatus.ACTIVITY)){
            throw new MobaoException("商户处于活动状态不需要解锁。");
        }else if (merchantInfo.getStatus().equals(CMerchantStatus.LOGOUT)){
            throw new MobaoException("商户已经注销，不能进行解锁。");
        }

        //若为商户解锁操作，需要确保当前商户不处于锁定,注销审核事件的审核中，且商户只存在一个解锁事件
        mutexCheck(customerNo);

        /** 1.生成审核事件 */
        // 查询“商户锁定”事件初始节点信息，以备后续赋值
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
                CAuditEventType.MERCHANT_UNLOCK, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：商户锁定
        auditEvent.setAuditEventType(CAuditEventType.MERCHANT_UNLOCK);
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

        // 保存审核事件
        auditEventService.save(auditEvent);

        params.put("auditEventId", auditEvent.getId().toString());
        params.put("operation", CAuditOperation.COMMIT_AUDIT);

        /** 调用审核公共方法 */
        auditEventService.audit(params);

    }

    /**
     * 商户注销
     * 传入参数：客户号customerNo，明细描述description
     * @param params
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public void logout(Map<String, String> params) {

        String currTime = DateUtil.now();
        String customerNo = params.get("customerNo");
        //注销事件必需保证当前商户是非注销状态
        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
        //商户状态检查
        if (merchantInfo.getStatus().equals(CMerchantStatus.LOGOUT)){
            throw new MobaoException("商户已经注销。");
        }

        //若为商户注销操作，需要确保商户不处于解锁，锁定审核事件中，并且当前只存在一个注销事件
        mutexCheck(customerNo);

        //检查商户余额，商户余额>0不允许注销
        checkBalance(customerNo);

        /** 1.生成审核事件 */
        // 查询“商户锁定”事件初始节点信息，以备后续赋值
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
                CAuditEventType.MERCHANT_LOGOUT, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：商户锁定
        auditEvent.setAuditEventType(CAuditEventType.MERCHANT_LOGOUT);
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

        // 保存审核事件
        auditEventService.save(auditEvent);

        params.put("auditEventId", auditEvent.getId().toString());
        params.put("operation", CAuditOperation.COMMIT_AUDIT);

        /** 调用审核公共方法 */
        auditEventService.audit(params);

    }




    /**
     * 草稿修改
     * 1.获取当前审核事件信息（供后续赋值）
     * 2.生成一条明细日志记录
     * 3.保存基本信息
     *
     * @param auditMerchantInfo
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateDraft(AuditMerchantInfo auditMerchantInfo){
        //加密敏感信息
        encrypt(auditMerchantInfo);

        //黑名单校验
        blacklistCheck(auditMerchantInfo);

        //重复要素校验
        checkInfoRepeat(auditMerchantInfo);

        String currTime = DateUtil.now();
        Long auditEventId = auditMerchantInfo.getAuditEventId();

        /** 1.获取当前审核事件信息（供后续赋值） */
        AuditEvent event = auditEventService.getById(auditEventId);
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

        /** 2.生成一条明细日志记录 */
        AuditEventDetail detail = new AuditEventDetail();
        //是日志记录，非审核流程记录。
        detail.setIfLog(Constants.YES);
        detail.setAuditEventId(event.getId());
        detail.setOperation(CAuditOperation.UPDATE);
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setNodeCode(event.getNodeCode());
        detail.setNodeName(flow.getNodeName());
        detail.setUpdateRecord(JSON.toJSONString(auditMerchantInfo));
        detail.setDescription("商户基本资料草稿修改");
        detail.setCreateTime(currTime);
        //保存明细日志记录
        auditEventDetailService.save(detail);

        /** 3.保存基本信息 */
        auditMerchantInfo.setUpdateTime(currTime);
        return super.updateById(auditMerchantInfo);
    }


    @Override
    public AuditMerchantInfo getByEventId(Long eventId) {

        LambdaQueryWrapper<AuditMerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditMerchantInfo::getAuditEventId, eventId);

        AuditMerchantInfo auditMerchantInfo = getOne(wrapper);

        return auditMerchantInfo;
    }

    @Override
    public AuditMerchantInfo getByCustomerNo(String customerNo) {
        LambdaQueryWrapper<AuditMerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditMerchantInfo::getCustomerNo, customerNo);

        String platformCustomerNo = NetUtil.getCustomerNoFromHeader();
        if(StringUtils.isNotBlank(platformCustomerNo)){
            wrapper.eq(AuditMerchantInfo::getPlatformCustomerNo, platformCustomerNo);
        }

        AuditMerchantInfo auditMerchantInfo = getOne(wrapper);

        return auditMerchantInfo;
    }

    /**
     * 商户中心合作商户入网通过后续处理
     * @param customerNo
     */
    @Override
    public void merchantCentreAuditPass(String customerNo) {

        AuditMerchantInfo oldMerchantInfo = getByCustomerNo(customerNo);
        AuditMerchantElectronicData oldData = auditMerchantElectronicDataService.getByCustomerNo(customerNo);
        AuditMerchantSettlementInfo oldSettlementInfo = auditMerchantSettlementInfoService.getByCustomerNo(customerNo);

        String currTime = DateUtil.now();

        /********************************************* "商户基本信息"处理 begin begin begin ********************/
        //将通过审核的数据转存入正式表
        MerchantInfo merchantInfo = new MerchantInfo();
        BeanUtils.copyProperties(oldMerchantInfo, merchantInfo);
        merchantInfo.setId(null);
        //商户审核通过，初始状态为“活动”
        merchantInfo.setStatus(CMerchantStatus.ACTIVITY);
        merchantInfo.setCreateTime(currTime);
        merchantInfo.setUpdateTime(currTime);
        merchantInfo.setTask1(Constants.NO);
        merchantInfo.setRiskTask(Constants.NO);
        merchantInfo.setTradeCheck(Constants.NO);
        merchantInfoService.save(merchantInfo);
        /********************************************* "商户基本信息"处理 end end end **************************/

        /********************************************* "商户结算信息"处理 begin begin begin ********************/
        //将通过审核的数据转存入正式表
        MerchantSettlementInfo merchantSettlementInfo = new MerchantSettlementInfo();
        BeanUtils.copyProperties(oldSettlementInfo, merchantSettlementInfo);
        merchantSettlementInfo.setId(null);
        merchantSettlementInfo.setCreateTime(currTime);
        merchantSettlementInfo.setUpdateTime(currTime);
        merchantSettlementInfoService.save(merchantSettlementInfo);
        /********************************************* "商户结算信息"处理 end end end **************************/

        /********************************************* "商户电子资料"处理 begin begin begin ********************/
        //将通过审核的数据转存入正式表
        MerchantElectronicData merchantElectronicData = new MerchantElectronicData();
        BeanUtils.copyProperties(oldData, merchantElectronicData);
        merchantElectronicData.setId(null);
        merchantElectronicData.setCreateTime(currTime);
        merchantElectronicData.setUpdateTime(currTime);
        merchantElectronicDataService.save(merchantElectronicData);
        /********************************************* "商户电子资料"处理 end end end **************************/

        /********************************************* "商户配置"处理 begin begin begin ********************/
        MerchantConfig merchantConfig = new MerchantConfig();
        merchantConfig.setCustomerNo(customerNo);
        merchantConfig.setSettlementPeriod("T+1");
        merchantConfig.setSettlement("1");
        merchantConfig.setMinWithdrawCashMoney("100.00");
        merchantConfig.setPaymentAccount("1");
        merchantConfig.setOpenedCurrencyAccount(",CNY,");
        merchantConfig.setOpenedCurrencyAccountDic("人民币");

        merchantConfig.setCreateTime(currTime);
        merchantConfig.setUpdateTime(currTime);
        merchantConfigService.save(merchantConfig);
        /********************************************* "商户配置"处理 end end end **************************/

        //触发推送数据定时任务执行
        InitTaskData.tradeAccountData.incrementAndGet();

    }

    /**
     * 商户入网事件，审核通过后续流程
     * @param auditEventId
     */
    @Override
    public void accessNetworkPass(Long auditEventId) {

        String currTime = DateUtil.now();

        /********************************************* "商户基本信息"处理 begin begin begin ********************/
        //根据{审核事件ID}从审核表中获取刚审核通过的记录
        AuditMerchantInfo auditMerchantInfo = getByEventId(auditEventId);

        //将通过审核的数据转存入正式表
        MerchantInfo merchantInfo = new MerchantInfo();
        BeanUtils.copyProperties(auditMerchantInfo, merchantInfo);
        merchantInfo.setId(null);
        //商户审核通过，初始状态为“活动”
        merchantInfo.setStatus(CMerchantStatus.ACTIVITY);
        merchantInfo.setCreateTime(currTime);
        merchantInfo.setUpdateTime(currTime);
        merchantInfo.setTask1(Constants.NO);
        merchantInfo.setRiskTask(Constants.NO);
        merchantInfo.setTradeCheck(Constants.NO);
        merchantInfoService.save(merchantInfo);
        /********************************************* "商户基本信息"处理 end end end **************************/


        /********************************************* "商户中心账号"处理 begin begin begin ********************/
        //根据{审核事件ID}从审核表中获取刚审核通过的记录
        AuditMerchantCentreAccount auditMerchantCentreAccountServiceOne = auditMerchantCentreAccountService.getByEventId(auditEventId);

        //将通过审核的数据转存入正式表
        MerchantCentreAccount merchantCentreAccount = new MerchantCentreAccount();
        BeanUtils.copyProperties(auditMerchantCentreAccountServiceOne, merchantCentreAccount);
        merchantCentreAccount.setId(null);
        merchantCentreAccount.setCreateTime(currTime);
        merchantCentreAccount.setUpdateTime(currTime);
        merchantCentreAccount.setTask1(Constants.NO);
        merchantCentreAccountService.save(merchantCentreAccount);

        /********************************************* "商户中心账号"处理 end end end **************************/


        /********************************************* "商户结算信息"处理 begin begin begin ********************/
        //根据{审核事件ID}从审核表中获取刚审核通过的记录
        AuditMerchantSettlementInfo auditMerchantSettlementInfo = auditMerchantSettlementInfoService.getByEventId(auditEventId);
        //将通过审核的数据转存入正式表
        MerchantSettlementInfo merchantSettlementInfo = new MerchantSettlementInfo();
        BeanUtils.copyProperties(auditMerchantSettlementInfo, merchantSettlementInfo);
        merchantSettlementInfo.setId(null);
        merchantSettlementInfo.setCreateTime(currTime);
        merchantSettlementInfo.setUpdateTime(currTime);
        merchantSettlementInfoService.save(merchantSettlementInfo);
        /********************************************* "商户结算信息"处理 end end end **************************/


        /********************************************* "商户电子资料"处理 begin begin begin ********************/
        //根据{审核事件ID}从审核表中获取刚审核通过的记录
        AuditMerchantElectronicData auditMerchantElectronicDataServiceOne = auditMerchantElectronicDataService.getByEventId(auditEventId);

        //将通过审核的数据转存入正式表
        MerchantElectronicData merchantElectronicData = new MerchantElectronicData();
        BeanUtils.copyProperties(auditMerchantElectronicDataServiceOne, merchantElectronicData);
        merchantElectronicData.setId(null);
        merchantElectronicData.setCreateTime(currTime);
        merchantElectronicData.setUpdateTime(currTime);
        merchantElectronicDataService.save(merchantElectronicData);
        /********************************************* "商户电子资料"处理 end end end **************************/


        /********************************************* "商户配置"处理 begin begin begin ********************/
        //根据{审核事件ID}从审核表中获取刚审核通过的记录
        AuditMerchantConfig auditMerchantConfig = auditMerchantConfigService.getByEventId(auditEventId);
        //将通过审核的数据转存入正式表
        MerchantConfig merchantConfig = new MerchantConfig();
        BeanUtils.copyProperties(auditMerchantConfig,merchantConfig);
        merchantConfig.setId(null);
        merchantConfig.setCreateTime(currTime);
        merchantConfig.setUpdateTime(currTime);
        merchantConfigService.save(merchantConfig);
        /********************************************* "商户配置"处理 end end end **************************/


        /********************************************* "商户费率"处理 begin begin begin ********************/
        //根据{审核事件ID}从审核表中获取刚审核通过的记录
        List<AuditMerchantFeeRate> rateList = auditMerchantFeeRateService.getByEventId(auditEventId);
        for (AuditMerchantFeeRate auditMerchantFeeRate : rateList) {
            //将通过审核的数据转存入正式表
            MerchantFeeRate merchantFeeRate = new MerchantFeeRate();
            BeanUtils.copyProperties(auditMerchantFeeRate,merchantFeeRate);
            merchantFeeRate.setId(null);
            merchantFeeRate.setCreateTime(currTime);
            merchantFeeRate.setUpdateTime(currTime);
            merchantFeeRateService.save(merchantFeeRate);
        }
        /********************************************* "商户费率"处理 end end end **************************/

        //触发推送数据定时任务执行
//        InitTaskData.merchantCentreAccount.incrementAndGet();
//        InitTaskData.tradeAccountData.incrementAndGet();

    }


    /**
     * 商户基本信息修改事件，审核通过后续流程
     *
     * @param auditEventId
     */
    @Override
    public void changeUpdatePass(Long auditEventId) {

        /** 1.记录修改内容 */
        //根据{审核事件ID}从审核表中获取刚审核通过的记录。
        AuditMerchantInfo auditMerchantInfo = getByEventId(auditEventId);

        //根据{客户号}从正式表中获取正式数据
        MerchantInfo formalData = merchantInfoService.getByCustomerNo(auditMerchantInfo.getCustomerNo());
        formalData.setUpdateTime(DateUtil.now());

        //对比最终修改内容并记录
        //对比之前先将不需要修改的数据统一成正式数据
        MerchantInfo newData = new MerchantInfo();
        BeanUtils.copyProperties(auditMerchantInfo, newData);

        newData.setId(formalData.getId());
        newData.setOldCustomerNo(formalData.getOldCustomerNo());
        newData.setLockTime(formalData.getLockTime());
        newData.setCancelTime(formalData.getCancelTime());
        newData.setUpdateTime(formalData.getUpdateTime());
        newData.setCreateTime(formalData.getCreateTime());
        newData.setTradeCheck(formalData.getTradeCheck());
        newData.setLastTradeDate(formalData.getLastTradeDate());
        newData.setLastOutDate(formalData.getLastOutDate());
        //根据交易系统反馈：已经推送过的商户数据，即使修改，也不必再次推送
        newData.setTask1(formalData.getTask1());
        newData.setRiskTask(formalData.getRiskTask());

        //获取修改内容，并将修改内容保存到草稿表中
        String updateRecord = BeanChangeCompareUtil.compare(formalData, newData);
        auditMerchantInfo.setUpdateRecord(updateRecord);
        updateById(auditMerchantInfo);

        /** 2.将通过审核的数据转存入正式表 */
        merchantInfoService.updateById(newData);
    }


    /**
     * 商户锁定事件，审核通过后续流程
     *
     * @param customerNo
     */
    @Override
    public void lockPass(String customerNo) {

        /** 1.将商户状态修改为锁定 */
        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
        merchantInfo.setStatus(CMerchantStatus.LOCKED);
        merchantInfo.setLockTime(DateUtil.now());

        /** 2.将通过审核的数据转存入正式表 */
        merchantInfoService.updateById(merchantInfo);

    }


    /**
     * 商户解锁事件，审核通过后续流程
     *
     * @param customerNo
     */
    @Override
    public void unlockPass(String customerNo) {

        /** 1.将商户状态修改为"活动" */
        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
        merchantInfo.setStatus(CMerchantStatus.ACTIVITY);

        /** 2.将通过审核的数据转存入正式表 */
        merchantInfoService.updateById(merchantInfo);

    }


    /**
     * 商户注销事件，审核通过后续流程
     *
     * @param customerNo
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public void logoutPass(String customerNo) {

        /** 1.将商户状态修改为"注销" */
        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
        merchantInfo.setStatus(CMerchantStatus.LOGOUT);
        merchantInfo.setCancelTime(DateUtil.now());

        /** 2.将通过审核的数据转存入正式表 */
        merchantInfoService.updateById(merchantInfo);

        Map logout = new HashMap(1);
        logout.put("customerNo", customerNo);
        Result result;
        try {
            result = userFeign.logout(logout);
        }catch (Exception e){
            throw new MobaoException("用户权限服务调用失败", e);
        }

        logger.info("调用用户权限系统注销商户，返回："+ JSON.toJSONString(result));
        if(!Constants.YES.equals(result.getRetCode())){
            throw new MobaoException("调用用户权限系统注销商户,返回异常："+ result.getRetMsg());
        }

    }


    /**
     * 生成客户号
     * 客户号生成规则：个人/企业客户分类（1位）+机构号（3位）+顺序号（7位）+校验码（1位）=12位（个人/企业客户分类：1-企业 5-个人）
     * 如：199900000019
     */
    private String generateCustomerNo(){

        String customerNo = auditMerchantInfoMapper.getMaxCustomerNo();
        if(customerNo == null){
            //由于迁移了部分老数据，新商户号从200开始
            customerNo = "199900001991";
        }
        customerNo = CustomerNoUtil.generateCustomerNo(customerNo);

        return customerNo;
    }


    private void encrypt(AuditMerchantInfo auditMerchantInfo){

        //法人证件号--必填(证件类型：0-身份证 1-护照)
        String legalPersonCertNum = auditMerchantInfo.getLegalPersonCertNum();
        if(StringUtils.isNotBlank(legalPersonCertNum)){
            String certType = auditMerchantInfo.getLegalPersonCertType();
            if("0".equals(certType) && 18!=legalPersonCertNum.length()){
                throw new MobaoException("法人身份证号必须为18位");
            }
            auditMerchantInfo.setLegalPersonCertNum(EndeUtil.encrypt(legalPersonCertNum));
        }else {
            throw new MobaoException("法人证件号必填");
        }

        //联系人身份证号码--非必填
        String contactCertNum = auditMerchantInfo.getContactCertNum();
        if(StringUtils.isNotBlank(contactCertNum)){
            if(18!=contactCertNum.length()){
                throw new MobaoException("联系人身份证号必须为18位");
            }
            auditMerchantInfo.setContactCertNum(EndeUtil.encrypt(contactCertNum));
        }
//        else {
//            throw new MobaoException("联系人身份证号必填");
//        }

        //股东身份证号--非必填
        String shareholderIdNum = auditMerchantInfo.getShareholderIdNum();
        if(StringUtils.isNotBlank(shareholderIdNum)){
            if(18!=shareholderIdNum.length()){
                throw new MobaoException("股东身份证号必须为18位");
            }
            auditMerchantInfo.setShareholderIdNum(EndeUtil.encrypt(shareholderIdNum));
        }

        //业务人员证件号--非必填(证件类型：0-身份证 1-护照)
        String staffCertNum = auditMerchantInfo.getStaffCertNum();
        if(StringUtils.isNotBlank(staffCertNum)){
            String certType = auditMerchantInfo.getStaffCertType();
            if("0".equals(certType) && 18!=staffCertNum.length()){
                throw new MobaoException("业务人员身份证号必须为18位");
            }
            auditMerchantInfo.setStaffCertNum(EndeUtil.encrypt(staffCertNum));
        }


    }

    /**
     * 互斥事件检查
     * @param customerNo 客户号
     */
    private void mutexCheck(String customerNo){
        ArrayList<String> cAuditEventTypes = new ArrayList<>();
        cAuditEventTypes.add(CAuditEventType.MERCHANT_LOGOUT);
        cAuditEventTypes.add(CAuditEventType.MERCHANT_UNLOCK);
        cAuditEventTypes.add(CAuditEventType.MERCHANT_LOCK);
        int allCount = auditMerchantInfoMapper.countByCustomerNoAndCAuditCAuditEventType(customerNo,cAuditEventTypes);
        if (allCount >= 1){
            throw new MobaoException("商户正在审核中，请稍后再提交新的审核");
        }
    }


    /**
     * 调用风控系统，进行黑名单校验
     * @param merchantInfo
     */
    private void blacklistCheck(AuditMerchantInfo merchantInfo){

        Map<String, String> checkInfo = new HashMap<>(3);
        checkInfo.put("merchName", merchantInfo.getName());
        checkInfo.put("legalIdcardEncpt", merchantInfo.getLegalPersonCertNum());
        checkInfo.put("creditCode", merchantInfo.getBusinessLicenceNo());

        logger.info("调用风控系统黑名单校验接口，入参："+ JSON.toJSONString(checkInfo));
        Result result;
        try {
            result = riskFeign.blacklistCheck(checkInfo);
        }catch (Exception e){
            throw new MobaoException("风控系统调用失败", e);
        }

        logger.info("黑名单校验,风控系统返回结果："+ JSON.toJSONString(result));

        if(!Constants.YES.equals(result.getRetCode())){
            throw new MobaoException("黑名单校验,风控系统返回异常："+ result.getRetMsg());
        }else {

            Map<String,Object> retData = (Map<String,Object>)result.getRetData();
            Object isBlack = retData.get("isBlack");
            if(isBlack.toString().equals(Constants.YES)){
                throw new MobaoException("黑名单校验不通过："+ retData.get("blackReason"));
            }
        }
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
        logger.info("商户注销，余额检查,交易系统返回结果："+ JSON.toJSONString(result));

        if(!Constants.YES.equals(result.getRetCode())){
            throw new MobaoException("商户注销，余额检查,交易系统返回异常："+ result.getRetMsg());
        }

        Map<String,Object> retData = (Map<String,Object>)result.getRetData();
        List<Map<String, Object>> balanceList = (List<Map<String, Object>>)retData.get("list");
        for (Map<String, Object> balance : balanceList) {
            Object bal = balance.get("bal");
            BigDecimal decBal = new BigDecimal(bal.toString());
            if(decBal.compareTo(BigDecimal.valueOf(0))==1){
                throw new MobaoException("该商户支付账户"+ balance.get("ccy") +"余额大于0，不可注销");
            }
        }
    }

    @Override
    public boolean updateStatusByCustomerNo(String customerNo, String status) {

        LambdaQueryWrapper<AuditMerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditMerchantInfo::getCustomerNo, customerNo);

        AuditMerchantInfo entity = new AuditMerchantInfo();
        entity.setStatus(status);
        //提交审核时才校验资料是否完善
        if("02".equals(status)){
            AuditMerchantElectronicData oldData = auditMerchantElectronicDataService.getByCustomerNo(customerNo);
            if(oldData == null){
                throw new MobaoException("请完善[商户电子资料]后再提交审核");
            }
            AuditMerchantSettlementInfo oldSettlementInfo = auditMerchantSettlementInfoService.getByCustomerNo(customerNo);
            if(oldSettlementInfo == null){
                throw new MobaoException("请完善[商户结算信息]后再提交审核");
            }
            entity.setUpdateTime(DateUtil.now());
        }

        return update(entity, wrapper);
    }
}
