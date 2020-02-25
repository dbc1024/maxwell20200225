package com.mobao360.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventDetail;
import com.mobao360.audit.entity.AuditEventFlow;
import com.mobao360.audit.entity.AuditMerchantCentreAccount;
import com.mobao360.audit.mapper.AuditMerchantCentreAccountMapper;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.audit.service.IAuditMerchantCentreAccountService;
import com.mobao360.customer.entity.MerchantCentreAccount;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.service.IMerchantCentreAccountService;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.system.constant.*;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.BeanChangeCompareUtil;
import com.mobao360.system.utils.LoginUserInfoUtil;
import com.mobao360.task.InitTaskData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * service实现类
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2018-12-28
 */
@Service
public class AuditMerchantCentreAccountServiceImpl extends ServiceImpl<AuditMerchantCentreAccountMapper, AuditMerchantCentreAccount> implements IAuditMerchantCentreAccountService {

    @Autowired
    private IAuditEventService auditEventService;

    @Autowired
    private IAuditEventDetailService auditEventDetailService;

    @Autowired
    private IMerchantCentreAccountService merchantCentreAccountService;

    @Autowired
    private AuditMerchantCentreAccountMapper auditMerchantCentreAccountMapper;

    @Autowired
    private IAuditEventFlowService auditEventFlowService;

    @Autowired
    private IMerchantInfoService merchantInfoService;


    /**
     * 新增
     *
     * @param merchantCentreAccount
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean save(AuditMerchantCentreAccount merchantCentreAccount) {

        return super.save(merchantCentreAccount);
    }

    /**
     * 修改
     *
     * 1.生成一条明细日志
     * 2.修改草稿内容
     *
     * @param merchantCentreAccount
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean updateDraft(AuditMerchantCentreAccount merchantCentreAccount) {
        //重复校验
        checkInfoRepeat(merchantCentreAccount);

        String currTime = DateUtil.now();
        /**
         * 生成一条明细日志
         */
        AuditEvent event = auditEventService.getById(merchantCentreAccount.getAuditEventId());
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

        AuditEventDetail detail = new AuditEventDetail();
        //日志记录
        detail.setIfLog(Constants.YES);
        detail.setAuditEventId(event.getId());
        detail.setOperation(CAuditOperation.UPDATE);
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setNodeCode(event.getNodeCode());
        detail.setNodeName(flow.getNodeName());
        detail.setUpdateRecord(JSON.toJSONString(merchantCentreAccount));
        detail.setDescription("商户中心账号草稿修改");
        detail.setCreateTime(currTime);

        merchantCentreAccount.setUpdateTime(currTime);
        //保存日志
        auditEventDetailService.save(detail);

        return super.updateById(merchantCentreAccount);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {

        return super.removeById(id);
    }


    @Override
    public void changeUpdatePass(Long eventId){

        /** 1.记录修改内容 */
        //根据{审核事件ID}从审核表中获取刚审核通过的记录。
        AuditMerchantCentreAccount auditMerchantCentreAccount = getByAuditEventId(eventId);
        //根据{客户号}从正式表中获取正式数据
        MerchantCentreAccount formalData = merchantCentreAccountService.getByCustomerNo(auditMerchantCentreAccount.getCustomerNo());
        formalData.setUpdateTime(DateUtil.now());
        //对比最终修改内容并记录
        //对比之前先将不需要修改的数据统一成正式数据
        MerchantCentreAccount tempData = new MerchantCentreAccount();
        BeanUtils.copyProperties(auditMerchantCentreAccount,tempData);

        //不需要修改的内容
        tempData.setId(formalData.getId());
        tempData.setCreateTime(formalData.getCreateTime());
        tempData.setUpdateTime(formalData.getUpdateTime());
        tempData.setTask1(Constants.NO);
        //获取修改内容，并将修改内容保存到草稿表
        String updateRecord = BeanChangeCompareUtil.compare(formalData, tempData);
        auditMerchantCentreAccount.setUpdateRecord(updateRecord);
        updateById(auditMerchantCentreAccount);

        /** 2.将通过审核的数据转存入正式表 */
        merchantCentreAccountService.updateById(tempData);

        //触发推送商户中心账号定时任务执行
//        InitTaskData.merchantCentreAccount.incrementAndGet();
    }

    /**
     * 入网新增
     *
     * 1.生成一条明细日志记录
     * 2.保存日志记录
     * 3.保存草稿信息
     *
     * @param auditMerchantCentreAccount
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean accessNetworkCreate(AuditMerchantCentreAccount auditMerchantCentreAccount) {
        //重复调用校验
        checkCallRepeat(auditMerchantCentreAccount);
        //重复要素校验
        checkInfoRepeat(auditMerchantCentreAccount);

        String currTime = DateUtil.now();
        AuditEvent auditEvent = auditEventService.getById(auditMerchantCentreAccount.getAuditEventId());
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(auditEvent.getAuditEventType(), auditEvent.getNodeCode());
        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        auditMerchantCentreAccount.setCreateTime(currTime);
        auditMerchantCentreAccount.setUpdateTime(currTime);

        /**
         * 1.生成一条明细日志记录
         */
        AuditEventDetail auditEventDetail = new AuditEventDetail();
        auditEventDetail.setIfLog(Constants.YES);
        auditEventDetail.setAuditEventId(auditEvent.getId());
        auditEventDetail.setOperation(CAuditOperation.CREATE);
        auditEventDetail.setOperator(LoginUserInfoUtil.getUsername());
        auditEventDetail.setNodeCode(auditEvent.getNodeCode());
        auditEventDetail.setNodeName(flow.getNodeName());
        auditEventDetail.setUpdateRecord(auditMerchantCentreAccount.toString());
        auditEventDetail.setDescription("入网新增商户中心账号");
        auditEventDetail.setCreateTime(currTime);

        /**
         * 2.保存日记录
         */
        auditEventDetailService.save(auditEventDetail);

        /**
         * 3.保存草稿记录
         */
        return save(auditMerchantCentreAccount);
    }

    /**
     * 变更修改
     *
     * 1.清空id
     * 2.生成审核事件
     * 3.保存审核事件
     * 4.生成日志记录
     * 5.保存日志记录
     * 6.把审核id回填到日志记录当中
     * 7.保存草稿信息
     *
     * @param auditMerchantCentreAccount
     */
    @Override
    public boolean changeUpdate(AuditMerchantCentreAccount auditMerchantCentreAccount) {

        String customerNo = auditMerchantCentreAccount.getCustomerNo();
        //需保证变更修改事件 同时只能存在一个  前提：商户未注销
        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
        if (merchantInfo != null) {
            if (CMerchantStatus.LOGOUT.equals(merchantInfo.getStatus())) {
                throw new MobaoException("商户已注销，不能进行修改操作");
            }
        }else {
            throw new MobaoException("当前商户不存在，请检查商户号");
        }
        int updateCount = auditMerchantCentreAccountMapper.countUpdateByCustomerAndAuditEventType(customerNo);
        if (updateCount >= 1){
            throw new MobaoException("商户中心账号资料修改审核正在进行中，请于审核通过之后再提交新的修改审核");
        }

        //重复校验
        checkInfoRepeat(auditMerchantCentreAccount);

        /**
         * 清空id
         */
        auditMerchantCentreAccount.setId(null);

        String currTime = DateUtil.now();
        auditMerchantCentreAccount.setCreateTime(currTime);
        auditMerchantCentreAccount.setUpdateTime(currTime);
        /**
         * 2.生成审核事件
         */
        // 查询“商户基本资料修改”事件初始节点信息
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.MERCHANT_CENTRE_ACCOUNT_UPDATE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：商户入网
        auditEvent.setAuditEventType(CAuditEventType.MERCHANT_CENTRE_ACCOUNT_UPDATE);
        // 审核主体：商户名称
        auditEvent.setSubject(merchantInfoService.getOne(new LambdaQueryWrapper<MerchantInfo>().eq(MerchantInfo::getCustomerNo,auditMerchantCentreAccount.getCustomerNo()),true).getName());
        //事件主体编码
        auditEvent.setSubjectCode(auditMerchantCentreAccount.getCustomerNo());
        // 事件状态：草稿
        auditEvent.setStatus(CAuditEventStatus.DRAFT);
        // 节点信息赋值
        auditEvent.setNodeCode(flow.getNodeCode());
        auditEvent.setCreateTime(currTime);
        auditEvent.setUpdateTime(currTime);

        /**
         * 3.保存审核事件
         */
        auditEventService.save(auditEvent);

        /**
         * 4.生成日志记录
         */
        AuditEventDetail detail = new AuditEventDetail();
        detail.setIfLog(Constants.YES);
        detail.setAuditEventId(auditEvent.getId());
        detail.setOperation(CAuditOperation.CREATE);
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setNodeCode(flow.getNodeCode());
        detail.setNodeName(flow.getNodeName());
        detail.setUpdateRecord(auditMerchantCentreAccount.toString());
        detail.setDescription("变更修改商户中心账号");
        detail.setCreateTime(currTime);
        /**
         * 5.保存日志记录
         */
        auditEventDetailService.save(detail);
        /**
         * 6.把审核事件id 回填到草稿信息中
         */
        auditMerchantCentreAccount.setAuditEventId(auditEvent.getId());
        /**
         * 7.保存草稿信息
         */
        return save(auditMerchantCentreAccount);
    }

    @Override
    public AuditMerchantCentreAccount getByEventId(Long eventId) {

        LambdaQueryWrapper<AuditMerchantCentreAccount> wapper = new LambdaQueryWrapper<>();
        wapper.eq(AuditMerchantCentreAccount::getAuditEventId, eventId);

        AuditMerchantCentreAccount one = super.getOne(wapper);

        return one;
    }


    /**
     * 同一商户重复调用新增接口校验
     * @param auditMerchantCentreAccount
     */
    private void checkCallRepeat(AuditMerchantCentreAccount auditMerchantCentreAccount) {

        AuditMerchantCentreAccount aca = getOne(new LambdaQueryWrapper<AuditMerchantCentreAccount>()
                .eq(AuditMerchantCentreAccount::getCustomerNo,auditMerchantCentreAccount.getCustomerNo())
                .eq(AuditMerchantCentreAccount::getAuditEventId,auditMerchantCentreAccount.getAuditEventId())
        );

        if (aca != null){
            throw new MobaoException("当前商户已存在管理员，如需更改请调用修改接口。");
        }

    }

    /**
     * 商户信息重复要素校验
     * @param auditMerchantCentreAccount
     */
    private void checkInfoRepeat(AuditMerchantCentreAccount auditMerchantCentreAccount){

        List<AuditMerchantCentreAccount> repeat = auditMerchantCentreAccountMapper.checkInfoRepeat(auditMerchantCentreAccount);
        if (repeat!=null && repeat.size()>0){
            AuditMerchantCentreAccount oneRepeat = repeat.get(0);

            if(oneRepeat.getManagerEmail().equals(auditMerchantCentreAccount.getManagerEmail())){
                throw new MobaoException("管理员邮箱已绑定到其它待审核商户。");
            }
            if(oneRepeat.getManagerTel().equals(auditMerchantCentreAccount.getManagerTel())){
                throw new MobaoException("管理员手机号已绑定到其它待审核商户。");
            }
        }


        LambdaQueryWrapper<MerchantCentreAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(MerchantCentreAccount::getCustomerNo, auditMerchantCentreAccount.getCustomerNo());
        wrapper.and(i->i.eq(MerchantCentreAccount::getManagerEmail, auditMerchantCentreAccount.getManagerEmail())
                .or().eq(MerchantCentreAccount::getManagerTel, auditMerchantCentreAccount.getManagerTel()));

        MerchantCentreAccount formal = merchantCentreAccountService.getOne(wrapper);
        if (formal != null){
            if(formal.getManagerEmail().equals(auditMerchantCentreAccount.getManagerEmail())){
                throw new MobaoException("管理员邮箱已绑定到其它商户。");
            }
            if(formal.getManagerTel().equals(auditMerchantCentreAccount.getManagerTel())){
                throw new MobaoException("管理员手机号已绑定到其它商户。");
            }
        }


    }

    private AuditMerchantCentreAccount getByAuditEventId(Long auditEventId) {
        LambdaQueryWrapper<AuditMerchantCentreAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditMerchantCentreAccount::getAuditEventId,auditEventId);
        return getOne(wrapper);
    }


}