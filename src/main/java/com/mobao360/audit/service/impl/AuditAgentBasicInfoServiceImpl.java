package com.mobao360.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.audit.entity.AuditAgentBasicInfo;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventDetail;
import com.mobao360.audit.entity.AuditEventFlow;
import com.mobao360.audit.mapper.AuditAgentBasicInfoMapper;
import com.mobao360.audit.service.IAuditAgentBasicInfoService;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.customer.entity.AgentBasicInfo;
import com.mobao360.customer.service.IAgentBasicInfoService;
import com.mobao360.system.constant.*;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.BeanChangeCompareUtil;
import com.mobao360.system.utils.EndeUtil;
import com.mobao360.system.utils.LoginUserInfoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * service实现类
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-15
 */
@Service
public class AuditAgentBasicInfoServiceImpl extends ServiceImpl<AuditAgentBasicInfoMapper, AuditAgentBasicInfo> implements IAuditAgentBasicInfoService {

    @Autowired
    private IAuditEventService auditEventService;
    @Autowired
    private IAuditEventDetailService auditEventDetailService;
    @Autowired
    private IAgentBasicInfoService agentBasicInfoService;
    @Autowired
    private AuditAgentBasicInfoMapper auditAgentBasicInfoMapper;
    @Autowired
    private IAuditEventFlowService auditEventFlowService;


    /**
     * 新增
     *
     * @param auditAgentBasicInfo
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean save(AuditAgentBasicInfo auditAgentBasicInfo) {

        return super.save(auditAgentBasicInfo);
    }

    /**
     * 新增
     *  1.生成审核事件
     *  2.生成一条详细日志记录
     *  3.将审核事件id添加到草稿信息中
     *  4.保存草信息
     * @param auditAgentBasicInfo
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean changeCreate(AuditAgentBasicInfo auditAgentBasicInfo){
        //敏感信息加
        encrypt(auditAgentBasicInfo);

        //重复要素校验
        checkInfoRepeat(auditAgentBasicInfo);

        String currTime = DateUtil.now();
        auditAgentBasicInfo.setCreateTime(currTime);
        auditAgentBasicInfo.setUpdateTime(currTime);

        /** 清空id */
        auditAgentBasicInfo.setId(null);
        //设置代理商户号
        auditAgentBasicInfo.setAgentNo(generateAgentNo());
        /** 1.生成审核事件 */
        //获取审核事件流 节点类型
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.AGENT_CHANGE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        // 事件类型：代理商资料新增
        auditEvent.setAuditEventType(CAuditEventType.AGENT_CHANGE);
        //事件主体: 代商户名称
        auditEvent.setSubject(auditAgentBasicInfo.getName());
        //事件主体编码
        auditEvent.setSubjectCode(auditAgentBasicInfo.getAgentNo());
        // 事件状态：草稿
        auditEvent.setStatus(CAuditEventStatus.DRAFT);
        // 节点信息赋值
        auditEvent.setNodeCode(flow.getNodeCode());
        auditEvent.setCreateTime(currTime);
        auditEvent.setUpdateTime(currTime);
        // 保存审核事件
        auditEventService.save(auditEvent);

        /** 2.生成一条详细日志记录 */
        AuditEventDetail detail = new AuditEventDetail();
        //是日志记录，非审核流程记录。
        detail.setIfLog(Constants.YES);
        detail.setAuditEventId(auditEvent.getId());
        detail.setOperation(CAuditOperation.CREATE);
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setNodeCode(flow.getNodeCode());
        detail.setNodeName(flow.getNodeName());
        detail.setUpdateRecord(auditAgentBasicInfo.toString());
        detail.setDescription("代理商户基本资料新增");
        detail.setCreateTime(currTime);
        //保存明细日志记录
        auditEventDetailService.save(detail);

        /** 3.将审核事件id添加到草稿信息中*/
        auditAgentBasicInfo.setAuditEventId(auditEvent.getId());

        /** 4.保存代理商资料审核草稿*/
        return 	save(auditAgentBasicInfo);
    }

    /**
     * 重复要素校验
     * @param auditAgentBasicInfo
     */
    private void checkInfoRepeat(AuditAgentBasicInfo auditAgentBasicInfo) {

        List<AuditAgentBasicInfo> repeat = auditAgentBasicInfoMapper.checkInfoRepeat(auditAgentBasicInfo);
        if (repeat != null && repeat.size()>0){
            AuditAgentBasicInfo oneRepeat = repeat.get(0);

            if (oneRepeat.getName().equals(auditAgentBasicInfo.getName())){
                throw new MobaoException("全称已被其它待审核代理商使用");
            }
            if (oneRepeat.getBusinessLicenceNo().equals(auditAgentBasicInfo.getBusinessLicenceNo())){
                throw new MobaoException("营业执照号已绑定到其它待审核代理商");
            }
        }


        LambdaQueryWrapper<AgentBasicInfo> wrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(auditAgentBasicInfo.getAgentNo())){
            wrapper.ne(AgentBasicInfo::getAgentNo, auditAgentBasicInfo.getAgentNo());
        }
        wrapper.and(i->i.eq(AgentBasicInfo::getName, auditAgentBasicInfo.getName())
                .or().eq(AgentBasicInfo::getBusinessLicenceNo, auditAgentBasicInfo.getBusinessLicenceNo()));

        AgentBasicInfo formal = agentBasicInfoService.getOne(wrapper);
        if (formal != null){

            if (formal.getName().equals(auditAgentBasicInfo.getName())){
                throw new MobaoException("全称已被其它代理商使用");
            }
            if (formal.getBusinessLicenceNo().equals(auditAgentBasicInfo.getBusinessLicenceNo())){
                throw new MobaoException("营业执照号已绑定到其它代理商");
            }
        }


    }

    /**
     * 代理基本信息变更
     *
     *  1.生成审核事件
     *  2.生成一条详细日志记录
     *  3.将审核事件id 回填到草稿信息中
     *  4.保存变更草稿信息
     * @param auditAgentBasicInfo
     * @return
     */
    @Override
    public boolean changeUpdate(AuditAgentBasicInfo auditAgentBasicInfo) {
        //需要保证代理商当前同时只有一个变更修改审核事件 前提：代理商未被注销
        String agentNo = auditAgentBasicInfo.getAgentNo();
        AgentBasicInfo agentBasicInfo = agentBasicInfoService.getByAgentNo(agentNo);
        if (agentBasicInfo != null){
            //暂时使用商户状态常量
            if (CMerchantStatus.LOGOUT.equals(agentBasicInfo.getAgentStatus())){
                throw new MobaoException("代理商已被注销，不能进行修改操作");
            }
        }else {
            throw new MobaoException("代理商不存在，请检查代理商号");
        }
        //尽管代理商变更新增和变更修改时审核事件类型相同，但一个商户只有在正式数据通过之后才能进行变更修改操作
        //检查修改审核事件数量
        int updateCount = auditAgentBasicInfoMapper.selectByAgentNoAndAuditEventType(agentNo);
        if (updateCount >= 1){
            throw new MobaoException("当前代理商已在变更审核中，请在审核通过之后再提交新的变更审核。");
        }

        //敏感信息加
        encrypt(auditAgentBasicInfo);

        //重复要素校验
        checkInfoRepeat(auditAgentBasicInfo);

        String now = DateUtil.now();
        auditAgentBasicInfo.setCreateTime(now);
        auditAgentBasicInfo.setUpdateTime(now);
        /** 清空id */
        auditAgentBasicInfo.setId(null);
        /** 1.生成审核事件 */
        //获取审核事件流 节点类型
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.AGENT_CHANGE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

        //流程权限校验
        auditEventService.checkFlowPermission(flow);

        AuditEvent auditEvent = new AuditEvent();
        // 审核模块：运营管理-客户
        auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
        //审核类型 : 代理资料变更
        auditEvent.setAuditEventType(CAuditEventType.AGENT_CHANGE);
        //审核主体 : 代商户名称
        auditEvent.setSubject(auditAgentBasicInfo.getName());
        //事件主体编码
        auditEvent.setSubjectCode(auditAgentBasicInfo.getAgentNo());
        //事件状态
        auditEvent.setStatus(CAuditEventStatus.DRAFT);
        //节点信息赋值
        auditEvent.setNodeCode(flow.getNodeCode());
        auditEvent.setCreateTime(now);
        auditEvent.setUpdateTime(now);
        //保存审核事件
        auditEventService.save(auditEvent);

        /** 2.生成一条详细日志记录 */
        AuditEventDetail auditEventDetail = new AuditEventDetail();
        //是日志记录，非审核流程记录。
        auditEventDetail.setIfLog(Constants.YES);
        auditEventDetail.setAuditEventId(auditEvent.getId());
        auditEventDetail.setOperator(LoginUserInfoUtil.getUsername());
        auditEventDetail.setOperation(CAuditOperation.CREATE);
        auditEventDetail.setNodeCode(flow.getNodeCode());
        auditEventDetail.setNodeName(flow.getNodeName());
        auditEventDetail.setUpdateRecord(auditAgentBasicInfo.toString());
        auditEventDetail.setDescription("变更修改代理基本资料");
        auditEventDetail.setCreateTime(now);
        //保存日志记录
        auditEventDetailService.save(auditEventDetail);

        /** 3.将审核事件id 回填到草稿信息中*/
        auditAgentBasicInfo.setAuditEventId(auditEvent.getId());

        /**  4.保存变更草稿信息 */
        return save(auditAgentBasicInfo);
    }

    /**
     * 修改
     * 1.生成一条日志记录，作为记录
     *
     * @param auditAgentBasicInfo
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean updateDraft(AuditAgentBasicInfo auditAgentBasicInfo) {

        //敏感信息加
        encrypt(auditAgentBasicInfo);

        //重复要素校验
        checkInfoRepeat(auditAgentBasicInfo);

        String now = DateUtil.now();
        //准备工作
        AuditEvent event = auditEventService.getById(auditAgentBasicInfo.getAuditEventId());
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

        /** 1.生成一条日志记录，作为记录 */
        AuditEventDetail detail = new AuditEventDetail();
        /**  对比获取具体修改内容 */
        //不需要改变的内容
        auditAgentBasicInfo.setUpdateTime(now);
        detail.setIfLog(Constants.YES);
        detail.setAuditEventId(event.getId());
        detail.setUpdateRecord(JSON.toJSONString(auditAgentBasicInfo));
        detail.setNodeName(flow.getNodeName());
        detail.setNodeCode(event.getNodeCode());
        detail.setOperation(CAuditOperation.UPDATE);
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setDescription("代理基本资料草稿修改");
        detail.setCreateTime(now);
        //保存明细日志记录
        auditEventDetailService.save(detail);

        /** 修改草稿信息 */
        return updateById(auditAgentBasicInfo);
    }

    /**
     * 加密敏感信息
     * @param auditAgentBasicInfo
     */
    private void encrypt(AuditAgentBasicInfo auditAgentBasicInfo){

        //法人证件号--必填(证件类型：0-身份证 1-护照)
        String legalPersonCertNum = auditAgentBasicInfo.getLegalPersonCertNum();
        if(StringUtils.isNotBlank(legalPersonCertNum)){
            String certType = auditAgentBasicInfo.getLegalPersonCertType();
            if("0".equals(certType) && 18!=legalPersonCertNum.length()){
                throw new MobaoException("法人身份证号必须为18位");
            }
            auditAgentBasicInfo.setLegalPersonCertNum(EndeUtil.encrypt(legalPersonCertNum));
        }else {
            throw new MobaoException("法人证件号必填");
        }


        //联系人身份证号码--必填
        String contactCertNum = auditAgentBasicInfo.getContactCertNum();
        if(StringUtils.isNotBlank(contactCertNum)){
            if(18!=contactCertNum.length()){
                throw new MobaoException("联系人身份证号必须为18位");
            }
            auditAgentBasicInfo.setContactCertNum(EndeUtil.encrypt(contactCertNum));
        }else {
            throw new MobaoException("联系人身份证号必填");
        }
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
    public AuditAgentBasicInfo getByAuditEventId(Long auditEventId) {

        return getOne(new LambdaQueryWrapper<AuditAgentBasicInfo>().eq(AuditAgentBasicInfo::getAuditEventId, auditEventId));
    }


    @Override
    public void changePass(Long eventId){

        AuditAgentBasicInfo auditAgentBasicInfo = getByAuditEventId(eventId);
        Long formalId = auditAgentBasicInfo.getFormalId();
        String now = DateUtil.now();

        //新增代理资料
        if (formalId == null) {
            AgentBasicInfo agentBasicInfo = new AgentBasicInfo();
            BeanUtils.copyProperties(auditAgentBasicInfo, agentBasicInfo);
            agentBasicInfo.setId(null);
            agentBasicInfo.setCreateTime(now);
            agentBasicInfo.setUpdateTime(now);
            agentBasicInfoService.save(agentBasicInfo);
            //更新代理资料
        } else  {
            /** 1.记录修改内容 */
            //根据{代商号}从正式表中获取正式数据
            AgentBasicInfo formalData = agentBasicInfoService.getByAgentNo(auditAgentBasicInfo.getAgentNo());
            formalData.setUpdateTime(now);
            AgentBasicInfo tempData = new AgentBasicInfo();
            BeanUtils.copyProperties(auditAgentBasicInfo, tempData);
            //不需要改变的字段
            tempData.setId(formalData.getId());
            tempData.setCreateTime(formalData.getCreateTime());
            tempData.setUpdateTime(formalData.getUpdateTime());
            //获取修改内容
            String compareResult = BeanChangeCompareUtil.compare(formalData, tempData);
            auditAgentBasicInfo.setUpdateRecord(compareResult);
            //保存审核事件
            updateById(auditAgentBasicInfo);

            //将草稿信息修改为正式代理资料
            agentBasicInfoService.updateById(tempData);
        }
    }

    /**
     * 生成代理商号
     * 代理商号生成规则：9打头 共11位 累加
     * 如：90000000001
     */
    private String generateAgentNo(){

        String agentNo = auditAgentBasicInfoMapper.getMaxAgentNo();
        if(agentNo == null){
            agentNo = "90000000001";
        }else {
            agentNo = Long.toString(Long.parseLong(agentNo) + 1);
        }
        return agentNo;
    }

}