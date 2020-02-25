package com.mobao360.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventDetail;
import com.mobao360.audit.entity.AuditEventFlow;
import com.mobao360.audit.entity.AuditMerchantElectronicData;
import com.mobao360.audit.mapper.AuditMerchantElectronicDataMapper;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.audit.service.IAuditMerchantElectronicDataService;
import com.mobao360.customer.entity.MerchantElectronicData;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.service.IMerchantElectronicDataService;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.system.constant.*;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.BeanChangeCompareUtil;
import com.mobao360.system.utils.LoginUserInfoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *  service实现类
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-03
 */
@Service
public class AuditMerchantElectronicDataServiceImpl extends ServiceImpl<AuditMerchantElectronicDataMapper, AuditMerchantElectronicData> implements IAuditMerchantElectronicDataService {

	@Autowired
	private IAuditEventService auditEventService;

	@Autowired
	private IAuditEventDetailService auditEventDetailService;

	@Autowired
	private IMerchantElectronicDataService merchantElectronicDataService;

	@Autowired
	private IMerchantInfoService merchantInfoService;

	@Autowired
	private IAuditEventFlowService auditEventFlowService;

	@Autowired
	private AuditMerchantElectronicDataMapper auditMerchantElectronicDataMapper;

    /**
     * 新增
     * @param auditMerchantElectronicData
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(AuditMerchantElectronicData auditMerchantElectronicData){
	
		return super.save(auditMerchantElectronicData);
	}
	
	/**
     * 修改
     * @param auditMerchantElectronicData
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateDraft(AuditMerchantElectronicData auditMerchantElectronicData){
		String currTime = DateUtil.now();
		Long auditEventId = auditMerchantElectronicData.getAuditEventId();

		/** 2.获取当前审核事件信息（供后续赋值） */
		AuditEvent auditEvent = auditEventService.getById(auditEventId);
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(auditEvent.getAuditEventType(), auditEvent.getNodeCode());

		/** 3.生成一条明细日志记录*/
		AuditEventDetail detail = new AuditEventDetail();
		//是日志记录，非审核流程记录。
		detail.setIfLog(Constants.YES);
		detail.setAuditEventId(auditEvent.getId());
		detail.setOperation(CAuditOperation.UPDATE);
		detail.setOperator(LoginUserInfoUtil.getUsername());
		detail.setNodeCode(auditEvent.getNodeCode());
		detail.setNodeName(flow.getNodeName());
//		detail.setUpdateRecord(JSON.toJSONString(auditMerchantElectronicData));
		detail.setDescription("商户电子资料草稿修改");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditEventDetailService.save(detail);
		auditMerchantElectronicData.setUpdateTime(currTime);

		/** 4.保存基本信息 */
		return super.updateById(auditMerchantElectronicData);
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
	 * 商户电子资料 入网新增
	 *
	 * 调用此接口需要如下操作
	 * 1.获取审核事件信息,供后续复值(审核事件在商户基本资料入网时已经生成)
	 * 2.生成一条明细日志记录
	 * 3.新增电子资料草稿
	 *
	 * @param merchantElectronicData
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
	public boolean accessNetworkCreate(AuditMerchantElectronicData merchantElectronicData) {
		accessJudgeRepeat(merchantElectronicData);
		/** 1.获取审核事件信息，供后续赋值 */
		String currTime = DateUtil.now();
		AuditEvent event = auditEventService.getById(merchantElectronicData.getAuditEventId());
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

		//流程权限校验
		auditEventService.checkFlowPermission(flow);

		/** 2.生成一条明细日志记录 */
		AuditEventDetail detail = new AuditEventDetail();
		detail.setIfLog(Constants.YES);
		detail.setAuditEventId(event.getId());
		detail.setOperation(CAuditOperation.CREATE);
		detail.setOperator(LoginUserInfoUtil.getUsername());
		detail.setNodeCode(event.getNodeCode());
		detail.setNodeName(flow.getNodeName());
//		detail.setUpdateRecord(merchantElectronicData.toString());
		detail.setDescription("入网新增商户电子资料");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditEventDetailService.save(detail);

		/** 3.新增电子资料草稿 */
		merchantElectronicData.setCreateTime(currTime);
		merchantElectronicData.setUpdateTime(currTime);
		return save(merchantElectronicData);
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
	public boolean changeUpdate(AuditMerchantElectronicData electronicData) {

		String currTime = DateUtil.now();
		String customerNo = electronicData.getCustomerNo();
		//获取客户信息
		MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);

		/** 1.生成审核事件 */
		// 查询“入网事件”初始节点信息，以备后续赋值
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
				CAuditEventType.MERCHANT_ELECTRONIC_DATA_CHANGE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
		//流程权限校验
		auditEventService.checkFlowPermission(flow);

		AuditEvent auditEvent = new AuditEvent();
		// 审核模块：运营管理-客户
		auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
		// 事件类型：商户配置变更修改
		auditEvent.setAuditEventType(CAuditEventType.MERCHANT_ELECTRONIC_DATA_CHANGE);
		// 审核主体：商户名称
		auditEvent.setSubject(merchantInfo.getName());
		//事件主体编码
		auditEvent.setSubjectCode(customerNo);
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
		detail.setOperation(CAuditOperation.CREATE);
		detail.setOperator(LoginUserInfoUtil.getUsername());
		detail.setNodeCode(flow.getNodeCode());
		detail.setNodeName(flow.getNodeName());
		detail.setDescription("变更修改商户电子资料");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditEventDetailService.save(detail);

		/** 3.将“审核事件ID”回填到商户电子资料草稿信息中*/
		electronicData.setAuditEventId(auditEvent.getId());


		/** 4.新增商户电子资料草稿信息 */
		electronicData.setCreateTime(currTime);
		electronicData.setUpdateTime(currTime);
		return save(electronicData);
	}

	/**
	 * 商户电子资料修改事件审核通过后续流程
	 * @param auditEventId
	 */
	@Override
	public void changeUpdatePass(Long auditEventId) {
		String currTime = DateUtil.now();

		//根据{审核事件ID}从审核表中获取刚审核通过的记录。
		AuditMerchantElectronicData auditElectronicData = getByEventId(auditEventId);
		//根据{客户号}从正式表中获取正式数据
		MerchantElectronicData formalData = merchantElectronicDataService.getByCustomerNo(auditElectronicData.getCustomerNo());
		formalData.setUpdateTime(currTime);

		MerchantElectronicData newData = new MerchantElectronicData();
		BeanUtils.copyProperties(auditElectronicData, newData);
		newData.setId(formalData.getId());
		newData.setCreateTime(formalData.getCreateTime());
		newData.setUpdateTime(formalData.getUpdateTime());

		/** 将通过审核的数据转存入正式表 */
		merchantElectronicDataService.updateById(newData);

	}

	/**
	 * 补充电子资料
	 * @param auditElectronicData
	 * @return
	 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
	public void supplement(AuditMerchantElectronicData auditElectronicData) {

        AuditEvent event = auditEventService.getById(auditElectronicData.getAuditEventId());
        AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

        //保存审核事件信息
        String currTime = DateUtil.now();

        /** 2.生成一条审核事件明细 */
        AuditEventDetail detail = new AuditEventDetail();
        //是审核事件明细，非日志记录
        detail.setIfLog(Constants.NO);
        detail.setAuditEventId(event.getId());
        //补充电子资料定为10
        detail.setOperation("10");
        detail.setOperator(LoginUserInfoUtil.getUsername());
        detail.setNodeCode(flow.getNodeCode());
        detail.setNodeName(flow.getNodeName());
        detail.setDescription(auditElectronicData.getDescription());
        detail.setCreateTime(currTime);
        //保存明细信息
        auditEventDetailService.save(detail);

        //保存新增的资料
        auditElectronicData.setUpdateTime(currTime);
        super.updateById(auditElectronicData);
    }

	/**
	 * 去重检查
	 * @param merchantElectronicData
	 */
	private void accessJudgeRepeat(AuditMerchantElectronicData merchantElectronicData) {
		AuditMerchantElectronicData repat = getOne(new LambdaQueryWrapper<AuditMerchantElectronicData>()
				.eq(AuditMerchantElectronicData::getAuditEventId, merchantElectronicData.getAuditEventId())
				.eq(AuditMerchantElectronicData::getCustomerNo, merchantElectronicData.getCustomerNo()));
		if (repat != null){
			throw new MobaoException("当前商户已上传电子资料,如需修改请调用商户电子资料修改接口");
		}
	}


	@Override
	public AuditMerchantElectronicData getByEventId(Long eventId) {

		LambdaQueryWrapper<AuditMerchantElectronicData> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AuditMerchantElectronicData::getAuditEventId, eventId);

		return getOne(wrapper);
	}

	@Override
	public AuditMerchantElectronicData getByCustomerNo(String customerNo) {
		LambdaQueryWrapper<AuditMerchantElectronicData> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AuditMerchantElectronicData::getCustomerNo, customerNo);
		return getOne(wrapper);
	}
}