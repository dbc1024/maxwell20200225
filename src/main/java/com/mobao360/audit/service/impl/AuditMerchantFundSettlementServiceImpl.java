package com.mobao360.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventDetail;
import com.mobao360.audit.entity.AuditEventFlow;
import com.mobao360.audit.entity.AuditMerchantFundSettlement;
import com.mobao360.audit.mapper.AuditMerchantFundSettlementMapper;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.audit.service.IAuditMerchantFundSettlementService;
import com.mobao360.customer.entity.MerchantFundSettlement;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.service.IMerchantFundSettlementService;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.system.constant.*;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.BeanChangeCompareUtil;
import com.mobao360.system.utils.DateUtils;
import com.mobao360.system.utils.LoginUserInfoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 *  service实现类
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-17
 */
@Service
public class AuditMerchantFundSettlementServiceImpl extends ServiceImpl<AuditMerchantFundSettlementMapper, AuditMerchantFundSettlement> implements IAuditMerchantFundSettlementService {

	@Autowired
	private IAuditEventService auditEventService;
	@Autowired
	private IAuditEventDetailService auditEventDetailService;
	@Autowired
	private IMerchantFundSettlementService merchantFundSettlementService;
	@Autowired
	private IAuditEventFlowService auditEventFlowService;
	@Autowired
	private IMerchantInfoService merchantInfoService;
	@Autowired
	private AuditMerchantFundSettlementMapper auditMerchantFundSettlementMapper;

/**
     * 新增
     * @param auditMerchantFundSettlement
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(AuditMerchantFundSettlement auditMerchantFundSettlement){
	
		return super.save(auditMerchantFundSettlement);
	}
	
	/**
     * 修改
     * @param auditMerchantFundSettlement
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateById(AuditMerchantFundSettlement auditMerchantFundSettlement){
	
		return super.updateById(auditMerchantFundSettlement);
	}
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean removeById(Serializable id){
	
		return super.removeById(id);
	}

	/**
	 * 商户垫资结算修改
	 * 	   1.生成审核事件
	 * 	   2.生成一条日志详细记录
	 * 	   3.将审核事件id回填到草稿信息中
	 * 	   4.保存变更草稿信息
	 * @param auditMerchantFundSettlement
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
	public boolean changeUpdate(AuditMerchantFundSettlement auditMerchantFundSettlement) {
		String customerNo = auditMerchantFundSettlement.getCustomerNo();
		//获取客户信息
		MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
		//需确保商户当前只存在一个正在进行中的修改审核事件 前提是商户是未注销状态
		if (merchantInfo != null){
			if (CMerchantStatus.LOGOUT.equals(merchantInfo.getStatus())){
				throw new MobaoException("商户已被注销，不能修改垫资结算信息。");
			}
		}else {
			throw new MobaoException("商户不存在，请检查商户号。");
		}
		int updateCount = auditMerchantFundSettlementMapper.countUpdateByCustomerAndAuditEventType(customerNo);
		if (updateCount >= 1){
			throw new MobaoException("商户垫资结算变更正在审核中，请于审核通过后再提交新的审核");
		}

		String now = DateUtil.now();
		auditMerchantFundSettlement.setCreateTime(now);
		auditMerchantFundSettlement.setUpdateTime(now);
		//保证id为空
		auditMerchantFundSettlement.setId(null);
		//获取审核事件流
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.MERCHANT_ADVANCE_SETTLEMENT_CHANGE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

		//流程权限校验
		auditEventService.checkFlowPermission(flow);

		/** 1.生成审核事件 */
		AuditEvent auditEvent = new AuditEvent();
		// 审核模块：运营管理-客户
		auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
		//审核类型 : 垫资结算资料变更
		auditEvent.setAuditEventType(CAuditEventType.MERCHANT_ADVANCE_SETTLEMENT_CHANGE);
		//审核主体 : 商户名称
		auditEvent.setSubject(merchantInfo.getName());
		//事件主体编码
		auditEvent.setSubjectCode(auditMerchantFundSettlement.getCustomerNo());
		//事件状态
		auditEvent.setStatus(CAuditEventStatus.DRAFT);
		//节点信息赋值
		auditEvent.setNodeCode(flow.getNodeCode());
		auditEvent.setCreateTime(now);
		auditEvent.setUpdateTime(now);
		//保存审核事件
		auditEventService.save(auditEvent);
		/** 2.生成一条日志详细记录 */
		AuditEventDetail auditEventDetail = new AuditEventDetail();
		//是日志记录，非审核流程记录。
		auditEventDetail.setIfLog(Constants.YES);
		auditEventDetail.setAuditEventId(auditEvent.getId());
		auditEventDetail.setOperator(LoginUserInfoUtil.getUsername());
		auditEventDetail.setOperation(CAuditOperation.CREATE);
		auditEventDetail.setNodeCode(flow.getNodeCode());
		auditEventDetail.setNodeName(flow.getNodeName());
		auditEventDetail.setUpdateRecord(auditMerchantFundSettlement.toString());
		auditEventDetail.setDescription("商户垫资结算变更新增");
		auditEventDetail.setCreateTime(now);
		//保存日志记录
		auditEventDetailService.save(auditEventDetail);
		/** 4.保存变更草稿信息 */
		auditMerchantFundSettlement.setAuditEventId(auditEvent.getId());
		return save(auditMerchantFundSettlement);
	}

	/**
	 * 商户垫资结算新增
	 *    1.生成审核事件
	 *    2.生成一条日志详细记录
	 *    3.将审核事件id回填到草稿信息中
	 *    4.保存草稿信息
	 * @param auditMerchantFundSettlement
	 *
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
	public boolean changeCreate(AuditMerchantFundSettlement auditMerchantFundSettlement) {
		String now = DateUtil.now();
		auditMerchantFundSettlement.setCreateTime(now);
		auditMerchantFundSettlement.setUpdateTime(now);
		auditMerchantFundSettlement.setId(null);

		MerchantFundSettlement formal = merchantFundSettlementService.getByCustomerNo(auditMerchantFundSettlement.getCustomerNo());
		if(formal != null){
			throw new MobaoException("商户已存在垫资结算配置");
		}

		//获取客户信息
		MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(auditMerchantFundSettlement.getCustomerNo());
		//获取审核事件流
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(CAuditEventType.MERCHANT_ADVANCE_SETTLEMENT_CHANGE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

		//流程权限校验
		auditEventService.checkFlowPermission(flow);

		/** 1.生成审核事件 */
		AuditEvent auditEvent = new AuditEvent();
		// 审核模块：运营管理-客户
		auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
		// 事件类型：垫资结算新增
		auditEvent.setAuditEventType(CAuditEventType.MERCHANT_ADVANCE_SETTLEMENT_CHANGE);
		//事件主体: 商户名称
		auditEvent.setSubject(merchantInfo.getName());
		//事件主体编码
		auditEvent.setSubjectCode(auditMerchantFundSettlement.getCustomerNo());
		// 事件状态：草稿
		auditEvent.setStatus(CAuditEventStatus.DRAFT);
		// 节点信息赋值
		auditEvent.setNodeCode(flow.getNodeCode());
		auditEvent.setCreateTime(now);
		auditEvent.setUpdateTime(now);
		// 保存审核事件
		auditEventService.save(auditEvent);

		/** 2.生成一条详细详细记录 */
		AuditEventDetail detail = new AuditEventDetail();
		//是日志记录，非审核流程记录。
		detail.setIfLog(Constants.YES);
		detail.setAuditEventId(auditEvent.getId());
		detail.setOperation(CAuditOperation.CREATE);
		detail.setOperator(LoginUserInfoUtil.getUsername());
		detail.setNodeCode(flow.getNodeCode());
		detail.setNodeName(flow.getNodeName());
		detail.setUpdateRecord(auditMerchantFundSettlement.toString());
		detail.setDescription("商户垫资结算新增");
		detail.setCreateTime(now);
		//保存详细日志记录
		auditEventDetailService.save(detail);

		/** 3.将审核事件id添加到草稿信息中*/
		auditMerchantFundSettlement.setAuditEventId(auditEvent.getId());

		/** 4.保存代理商资料审核草稿*/
		return save(auditMerchantFundSettlement);
	}

	/**
	 * 修改草稿审核信息
	 *
	 * 1.生成一条日志记录，作为记录
	 * @param auditMerchantFundSettlement
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
	public boolean updateDraft(AuditMerchantFundSettlement auditMerchantFundSettlement) {
		String now = DateUtil.now();
		//准备工作
		AuditEvent event = auditEventService.getById(auditMerchantFundSettlement.getAuditEventId());
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

		/** 1.生成一条日志记录，作为记录 */
		AuditEventDetail detail = new AuditEventDetail();

		detail.setIfLog(Constants.YES);
		detail.setAuditEventId(event.getId());
		detail.setNodeName(flow.getNodeName());
		detail.setNodeCode(event.getNodeCode());
		detail.setOperation(CAuditOperation.UPDATE);
		detail.setOperator(LoginUserInfoUtil.getUsername());
		detail.setUpdateRecord(JSON.toJSONString(auditMerchantFundSettlement));
		detail.setDescription("商户垫资结算草稿修改");
		detail.setCreateTime(now);
		//保存明细日志记录
		auditEventDetailService.save(detail);

		auditMerchantFundSettlement.setUpdateTime(now);
		/** 修改草稿信息 */
		return updateById(auditMerchantFundSettlement);
	}


	@Override
	public void changePass(Long eventId){

		AuditMerchantFundSettlement auditMerchantFundSettlement = getByAuditEventId(eventId);
		Long formalId = auditMerchantFundSettlement.getFormalId();
		String now = DateUtil.now();
		//垫资结算资料新增
		if (formalId == null) {
			auditMerchantFundSettlement.setCreateTime(now);
			auditMerchantFundSettlement.setUpdateTime(now);
			auditMerchantFundSettlement.setId(null);
			MerchantFundSettlement merchantFundSettlement = new MerchantFundSettlement();
			BeanUtils.copyProperties(auditMerchantFundSettlement,merchantFundSettlement);
			merchantFundSettlementService.save(merchantFundSettlement);
		}else {
			//资料变更
			MerchantFundSettlement FormalData = merchantFundSettlementService.getByCustomerNo(auditMerchantFundSettlement.getCustomerNo());
			FormalData.setUpdateTime(now);
			MerchantFundSettlement newFormalData = new MerchantFundSettlement();
			BeanUtils.copyProperties(auditMerchantFundSettlement,newFormalData);
			//不变的部分
			newFormalData.setId(FormalData.getId());
			newFormalData.setCreateTime(FormalData.getCreateTime());
			newFormalData.setUpdateTime(FormalData.getUpdateTime());
			//获取修改内容
			String compare = BeanChangeCompareUtil.compare(FormalData, newFormalData);
			auditMerchantFundSettlement.setUpdateRecord(compare);
			//保存审核事件
			updateById(auditMerchantFundSettlement);
			merchantFundSettlementService.updateById(newFormalData);
		}

	}

	private AuditMerchantFundSettlement getByAuditEventId(Long auditEventId) {
		LambdaQueryWrapper<AuditMerchantFundSettlement> wrapper = new LambdaQueryWrapper<AuditMerchantFundSettlement>().eq(AuditMerchantFundSettlement::getAuditEventId, auditEventId);
		return getOne(wrapper);
	}

	/**
	 * T0判定(产品经理又取消此判定逻辑了，保留代码，免得她又反悔)
	 * 商户入网90天后才可开通T0
	 * @param customerNo
	 */
	private void allowT0(String customerNo){
		MerchantInfo info = merchantInfoService.getByCustomerNo(customerNo);
		String createDate = DateUtils.offsetDay(info.getCreateTime(), 90);
		if(createDate.compareTo(DateUtil.today())>=0){
			throw new MobaoException("商户入网90天内不可开通T0");
		}
	}

}