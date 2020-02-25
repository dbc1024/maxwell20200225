package com.mobao360.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventDetail;
import com.mobao360.audit.entity.AuditEventFlow;
import com.mobao360.audit.entity.AuditMerchantFeeRate;
import com.mobao360.audit.mapper.AuditMerchantFeeRateMapper;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.audit.service.IAuditMerchantFeeRateService;
import com.mobao360.customer.entity.MerchantFeeRate;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.service.IMerchantFeeRateService;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.system.constant.*;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.DateUtils;
import com.mobao360.system.utils.LoginUserInfoUtil;
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
 *  service实现类
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-18
 */
@Service
public class AuditMerchantFeeRateServiceImpl extends ServiceImpl<AuditMerchantFeeRateMapper, AuditMerchantFeeRate> implements IAuditMerchantFeeRateService {

	@Autowired
	private IAuditEventService auditEventService;

	@Autowired
	private IAuditEventDetailService auditEventDetailService;

	@Autowired
	private IMerchantFeeRateService merchantFeeRateService;
	@Autowired
	private IAuditEventFlowService auditEventFlowService;

	@Autowired
	private IMerchantInfoService merchantInfoService;

	@Autowired
	private AuditMerchantFeeRateMapper auditMerchantFeeRateMapper;


    /**
     * 新增
     * @param auditMerchantFeeRate
     * @return
     */
    @Override
    public boolean save(AuditMerchantFeeRate auditMerchantFeeRate){
	
		return super.save(auditMerchantFeeRate);
	}


	/**
	 * 调此新增接口，实际是新增n条待审核的草稿费率。
	 * (无需生成审核事件，在商户基本资料入网接口已生成)
	 *
	 * 1.获取审核事件信息，供后续赋值
	 * 2.生成一条明细日志记录
	 * 3.批量新增草稿费率
	 * @param rateList
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
	public boolean accessNetworkCreate(List<AuditMerchantFeeRate> rateList) {
		//入网时 只需要检查提交的支付方式是否有重复
		ergodicRepeat(rateList);
		String currTime = DateUtil.now();

		/** 1.获取审核事件信息，供后续赋值 */
		AuditMerchantFeeRate oneRate = rateList.get(0);
		AuditEvent event = auditEventService.getById(oneRate.getAuditEventId());
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

		//流程权限校验
		auditEventService.checkFlowPermission(flow);

		/** 2.生成一条明细日志记录 */
		AuditEventDetail detail = new AuditEventDetail();
		//是日志记录，非审核流程记录。
		detail.setIfLog(Constants.YES);
		detail.setAuditEventId(event.getId());
		detail.setOperation(CAuditOperation.CREATE);
		detail.setOperator(LoginUserInfoUtil.getUsername());
		detail.setNodeCode(event.getNodeCode());
		detail.setNodeName(flow.getNodeName());
//		detail.setUpdateRecord(getUpdateRecord(rateList));
		detail.setDescription("入网新增商户费率");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditEventDetailService.save(detail);

		/** 3.批量新增草稿费率 */
		for (AuditMerchantFeeRate auditMerchantFeeRate : rateList) {
			auditMerchantFeeRate.setCreateTime(currTime);
			auditMerchantFeeRate.setUpdateTime(currTime);
			save(auditMerchantFeeRate);
		}

		return true;
	}

	/**
	 * 调此新增接口，实际是新增n条待审核的草稿费率。
	 *（需生成审核事件）
	 *
	 * 1.生成审核事件
	 * 2.生成一条明细日志记录
	 * 3.将“审核事件ID”回填到费率信息中
	 * 4.新增商户费率信息草稿
	 * @param rateList
	 * @return
	 */
	@Override
	public boolean changeCreate(List<AuditMerchantFeeRate> rateList) {
		AuditMerchantFeeRate oneRate = rateList.get(0);
		String customerNo = oneRate.getCustomerNo();
		Long formalId = oneRate.getFormalId();
		//需保证商户当前正在审核的事件只能同时存在1个 前提：商户未被注销
		MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
		if (merchantInfo != null){
			if (CMerchantStatus.LOGOUT.equals(merchantInfo.getStatus())){
				throw new MobaoException("商户已被注销，不能新增费率.");
			}
		}else {
			throw new MobaoException("新增商户不存在，请检查商户号。");
		}
		//通过正在审核的新增审核事件数量
		int updateCount = auditMerchantFeeRateMapper.countUpdateByCustomerAndAuditEventType(customerNo,formalId);
		if (updateCount >= 1){
			throw new MobaoException("商户费率正在新增审核中，请在审核通过之后再进行审核。");
		}

		//去重检查
		changeCreateJudgeRepeat(rateList);

		String currTime = DateUtil.now();


		/** 1.生成审核事件 */
		// 查询“费率变更”事件初始节点信息，以备后续赋值
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
				CAuditEventType.MERCHANT_FEERATE_CHANGE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
		//流程权限校验
		auditEventService.checkFlowPermission(flow);

		AuditEvent auditEvent = new AuditEvent();
		// 审核模块：运营管理-客户
		auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
		// 事件类型：商户入网
		auditEvent.setAuditEventType(CAuditEventType.MERCHANT_FEERATE_CHANGE);
		// 审核主体：商户名称
		auditEvent.setSubject(merchantInfo.getName());
		//事件主体编码
		auditEvent.setSubjectCode(oneRate.getCustomerNo());
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
//		detail.setUpdateRecord(getUpdateRecord(rateList));
		detail.setDescription("变更新增商户费率");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditEventDetailService.save(detail);

		/** 3.将“审核事件ID”回填到费率信息中*/
		for (AuditMerchantFeeRate auditMerchantFeeRate : rateList) {
			auditMerchantFeeRate.setAuditEventId(auditEvent.getId());
		}

		/** 4.新增商户费率信息草稿*/
		for (AuditMerchantFeeRate auditMerchantFeeRate : rateList) {
			auditMerchantFeeRate.setCreateTime(currTime);
			auditMerchantFeeRate.setUpdateTime(currTime);
			save(auditMerchantFeeRate);
		}

		return true;
	}



	/**
	 * 调此新增接口，实际是新增1条待审核的草稿费率。
	 *（需生成审核事件）
	 *
	 * 1.生成审核事件
	 * 2.生成一条明细日志记录
	 * 3.将“审核事件ID”回填到费率信息中
	 * 4.新增商户费率信息草稿
	 *
	 * @param auditMerchantFeeRate
	 * @return
	 */
	@Override
	public boolean changeUpdate(AuditMerchantFeeRate auditMerchantFeeRate) {
		//验证是否有对应的正式数据
		Long formalId = auditMerchantFeeRate.getFormalId();
		//取出正式表当前费率
		MerchantFeeRate formalRate = merchantFeeRateService.getById(formalId);
		if(formalRate != null){
			if (!formalRate.getPayType().equals(auditMerchantFeeRate.getPayType())||!formalRate.getProductKind().equals(auditMerchantFeeRate.getProductKind())){
				throw new MobaoException("产品类型和支付方式不允许修改。");
			}
		}else {
			throw new MobaoException("无此formalId");
		}

		//需保证商户当前正在审核的事件只能同时存在1个 前提：商户未被注销
		String customerNo = auditMerchantFeeRate.getCustomerNo();
		String currTime = DateUtil.now();
		MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
		if (merchantInfo != null){
			if (CMerchantStatus.LOGOUT.equals(merchantInfo.getStatus())){
				throw new MobaoException("商户已被注销，不能修改费率.");
			}
		}else {
			throw new MobaoException("修改商户不存在，请检查商户号。");
		}
		//通过正在审核的变更审核事件数量
		int updateCount = auditMerchantFeeRateMapper.countUpdateByCustomerAndAuditEventType(customerNo,formalId);
		if (updateCount >= 1){
			throw new MobaoException("该商户此费率正在审核中，待其审核通过后再发起。");
		}

		//验证：一个商户相同支付方式的未生效费率条数最多只能有一条
		//取出结束时间为空的唯一记录
		MerchantFeeRate one = merchantFeeRateService.getOne(new LambdaQueryWrapper<MerchantFeeRate>().isNull(MerchantFeeRate::getEndTime)
				.eq(MerchantFeeRate::getCustomerNo,auditMerchantFeeRate.getCustomerNo())
				.eq(MerchantFeeRate::getPayType,auditMerchantFeeRate.getPayType()));

		String now = DateUtil.now();


		//比较正式表中此支付方式结束时间为空的记录 是否生效
		if (DateUtils.compare(one.getStartTime(), now) > 0){
			throw new MobaoException("当前已存在此支付方式未生效的费率。(商户相同支付方式的未生效费率条数最多只能有一条)");
		}

		/** 1.生成审核事件 */
		// 查询“入网事件”初始节点信息，以备后续赋值
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
				CAuditEventType.MERCHANT_FEERATE_CHANGE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
		//流程权限校验
		auditEventService.checkFlowPermission(flow);

		AuditEvent auditEvent = new AuditEvent();
		// 审核模块：运营管理-客户
		auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
		// 事件类型：商户入网
		auditEvent.setAuditEventType(CAuditEventType.MERCHANT_FEERATE_CHANGE);
		// 审核主体：商户名称
		auditEvent.setSubject(merchantInfo.getName());
		//事件主体编码
		auditEvent.setSubjectCode(auditMerchantFeeRate.getCustomerNo());
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
		detail.setUpdateRecord(JSON.toJSONString(auditMerchantFeeRate));
		detail.setDescription("变更修改商户费率");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditEventDetailService.save(detail);

		/** 3.将“审核事件ID”回填到费率信息中*/
		auditMerchantFeeRate.setAuditEventId(auditEvent.getId());

		/** 4.新增商户费率信息草稿*/
		auditMerchantFeeRate.setCreateTime(currTime);
		auditMerchantFeeRate.setUpdateTime(currTime);
		return save(auditMerchantFeeRate);
	}


	/**
	 * 对于"费率变更-修改"这个事件
	 * 对应的草稿修改操作，就是单条修改
	 *
	 * 1.生成一条明细日志记录
	 * 2.修改费率草稿
	 *
     * @param auditMerchantFeeRate
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateDraft(AuditMerchantFeeRate auditMerchantFeeRate){

		String currTime = DateUtil.now();

		/** 1.生成一条明细日志记录*/
		AuditEvent event = auditEventService.getById(auditMerchantFeeRate.getAuditEventId());
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

		AuditEventDetail detail = new AuditEventDetail();
		//是日志记录，非审核流程记录。
		detail.setIfLog(Constants.YES);
		detail.setAuditEventId(event.getId());
		detail.setOperation(CAuditOperation.UPDATE);
		detail.setOperator(LoginUserInfoUtil.getUsername());
		detail.setNodeCode(event.getNodeCode());
		detail.setNodeName(flow.getNodeName());
		detail.setUpdateRecord(JSON.toJSONString(auditMerchantFeeRate));
		detail.setDescription("商户费率草稿修改");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditEventDetailService.save(detail);

		auditMerchantFeeRate.setUpdateTime(currTime);
		return super.updateById(auditMerchantFeeRate);
	}

	/**
	 * 对于"商户入网"，"费率变更-新增"这两个事件
	 * 对应的草稿修改操作，实际是批量新增，有以下几个步骤：
	 *
	 * 1.删除此事件对应的所有费率
	 * 2.将所有数据的id置null
	 * 3.生成一条明细日志记录
	 * 4.批量新增费率草稿
	 *
	 *
	 * @param rateList
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
	public boolean batchUpdateDraft(List<AuditMerchantFeeRate> rateList) {

		String currTime = DateUtil.now();
		String updateRecord = "";


		/** 1.删除此事件对应的所有费率*/
		AuditMerchantFeeRate oneRate = rateList.get(0);
		Long eventId = oneRate.getAuditEventId();

		LambdaQueryWrapper<AuditMerchantFeeRate> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AuditMerchantFeeRate::getAuditEventId, eventId);
		remove(wrapper);

		/** 2.将所有数据的id置null*/
		for (AuditMerchantFeeRate auditMerchantFeeRate : rateList) {

			updateRecord = updateRecord + auditMerchantFeeRate.toString();

			auditMerchantFeeRate.setId(null);
		}

		/** 3.生成一条明细日志记录*/
		AuditEvent event = auditEventService.getById(oneRate.getAuditEventId());
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

		AuditEventDetail detail = new AuditEventDetail();
		//是日志记录，非审核流程记录。
		detail.setIfLog(Constants.YES);
		detail.setAuditEventId(event.getId());
		detail.setOperation(CAuditOperation.UPDATE);
		detail.setOperator(LoginUserInfoUtil.getUsername());
		detail.setNodeCode(event.getNodeCode());
		detail.setNodeName(flow.getNodeName());
//		detail.setUpdateRecord(updateRecord);
		detail.setDescription("商户费率草稿修改");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditEventDetailService.save(detail);

		/** 4.批量新增费率草稿*/
		for (AuditMerchantFeeRate auditMerchantFeeRate : rateList) {
			save(auditMerchantFeeRate);
		}

		return true;
	}


	@Override
	public void changePass(Long eventId){

		String currTime = DateUtil.now();

		LambdaQueryWrapper<AuditMerchantFeeRate> auditWrapper = new LambdaQueryWrapper<>();
		auditWrapper.eq(AuditMerchantFeeRate::getAuditEventId, eventId);
		List<AuditMerchantFeeRate> auditRateList = list(auditWrapper);
		AuditMerchantFeeRate oneAuditRate = auditRateList.get(0);
		Long formalId = oneAuditRate.getFormalId();

		//构造待保存数据
		List<MerchantFeeRate> tempRateList = new LinkedList<>();
		for (AuditMerchantFeeRate auditMerchantFeeRate : auditRateList) {
			MerchantFeeRate rate = new MerchantFeeRate();

			BeanUtils.copyProperties(auditMerchantFeeRate, rate);
			rate.setId(null);
			rate.setCreateTime(currTime);
			rate.setUpdateTime(currTime);

			//产品要求：填写生效时间>审核成功时间，不做更改;填写生效时间<=审核成功时间，生效时间更新为当前时间
			int result = DateUtils.compare(rate.getStartTime(), currTime);
			if(result < 1){
				rate.setStartTime(currTime);
			}

			tempRateList.add(rate);
		}

		/** 判断事件类型，不同审核事件类型审核通过后有不同的后续处理流程*/
		//formalId为空，证明是"入网新增"或"费率变更-新增"事件
		if(formalId == null){
			// 费率新增，直接批量保存即可
			for (MerchantFeeRate merchantFeeRate : tempRateList) {
				merchantFeeRateService.save(merchantFeeRate);
			}

			//formalId不为空，证明是"费率变更-修改"事件
		}else {

			//同一支付方式的那条费率，其结束时间应置为新审过费率开始时间。
			MerchantFeeRate newRate = tempRateList.get(0);
			newRate.setCreateTime(currTime);
			newRate.setUpdateTime(currTime);
			MerchantFeeRate oldRate = merchantFeeRateService.getById(formalId);
			oldRate.setEndTime(newRate.getStartTime());
			oldRate.setUpdateTime(currTime);
			//将修改结束日期的费率和新过审的费率一起保存
			merchantFeeRateService.updateById(oldRate);
			merchantFeeRateService.save(newRate);
		}

	}


	@Override
	public List<AuditMerchantFeeRate> getByEventId(Long eventId) {

		LambdaQueryWrapper<AuditMerchantFeeRate> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AuditMerchantFeeRate::getAuditEventId, eventId);

		List<AuditMerchantFeeRate> list = super.list(wrapper);

		return list;
	}


	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@Override
	public boolean removeById(Serializable id){

		return super.removeById(id);
	}


	/**
	 * 变更新增去重检查
	 * @param rateList
	 */
	private void changeCreateJudgeRepeat(List<AuditMerchantFeeRate> rateList) {
		//提交支付 重复检查
		ergodicRepeat(rateList);
		String customerNo = rateList.get(0).getCustomerNo();
		//把提交的费率支付方式全部装到payTypes中
        List<String> payTypes = new ArrayList<>();
        for (AuditMerchantFeeRate amf : rateList){
		    payTypes.add(amf.getPayType());
        }
        //正式表的支付方式和提交的支付方式 取重
		int count = merchantFeeRateService.count(new LambdaQueryWrapper<MerchantFeeRate>().in(MerchantFeeRate::getPayType, payTypes).eq(MerchantFeeRate::getCustomerNo, customerNo));
		if (count != 0) {
            throw new MobaoException("新增的商户费率中已存在于正式表中，请与正式表核对新增费率的重复性。");
        }
    }

	/**
	 * 检查待修改费率配置列表中是否存在相同的支付方式
	 * @param rateList
	 */
	private void ergodicRepeat(List<AuditMerchantFeeRate> rateList) {
		HashSet<String> feeRateSet = new HashSet<>();
		for (AuditMerchantFeeRate rate : rateList){
			feeRateSet.add(rate.getPayType());
		}
		if (feeRateSet.size() < rateList.size()){
			throw new MobaoException("产品支付方式不能重复");
		}
	}


	private String getUpdateRecord(List<AuditMerchantFeeRate> rateList){

		//费率的变动无法确定修改内容，所以直接保存全量字符串。
		String updateRecord = "";

		String currTime = DateUtil.now();

		for (AuditMerchantFeeRate auditMerchantFeeRate : rateList) {
			auditMerchantFeeRate.setCreateTime(currTime);
			auditMerchantFeeRate.setUpdateTime(currTime);

			updateRecord = updateRecord + auditMerchantFeeRate.toString();
		}

		return updateRecord;
	}

}