package com.mobao360.audit.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.api.feign.RiskFeign;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventDetail;
import com.mobao360.audit.entity.AuditEventFlow;
import com.mobao360.audit.entity.AuditMerchantSettlementInfo;
import com.mobao360.audit.mapper.AuditMerchantSettlementInfoMapper;
import com.mobao360.audit.service.*;
import com.mobao360.base.entity.BankCode;
import com.mobao360.base.service.IBankCodeService;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.entity.MerchantSettlementInfo;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.customer.service.IMerchantSettlementInfoService;
import com.mobao360.system.constant.*;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.BeanChangeCompareUtil;
import com.mobao360.system.utils.EndeUtil;
import com.mobao360.system.utils.LoginUserInfoUtil;
import com.mobao360.system.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 商户结算信息 service实现类
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-28
 */
@Service
public class AuditMerchantSettlementInfoServiceImpl extends ServiceImpl<AuditMerchantSettlementInfoMapper, AuditMerchantSettlementInfo> implements IAuditMerchantSettlementInfoService {

	private Logger logger = LoggerFactory.getLogger(AuditMerchantSettlementInfoServiceImpl.class);

	@Autowired
	private IAuditEventService auditEventService;
	@Autowired
	private IAuditEventDetailService auditEventDetailService;
	@Autowired
	private IMerchantSettlementInfoService merchantSettlementInfoService;
	@Autowired
	private IAuditEventFlowService auditEventFlowService;
	@Autowired
	private IMerchantInfoService merchantInfoService;
	@Autowired
	private IAuditMerchantInfoService auditMerchantInfoService;
	@Autowired
	AuditMerchantSettlementInfoMapper auditMerchantSettlementInfoMapper;
	@Autowired
	private IBankCodeService bankCodeService;
	@Autowired
	private RiskFeign riskFeign;



	/**
     * 新增
     * @param auditMerchantSettlementInfo
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(AuditMerchantSettlementInfo auditMerchantSettlementInfo){

		return super.save(auditMerchantSettlementInfo);
	}
	
	/**
     * 修改
     * @param auditMerchantSettlementInfo
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateDraft(AuditMerchantSettlementInfo auditMerchantSettlementInfo){

		String currTime = DateUtil.now();
		Long auditEventId = auditMerchantSettlementInfo.getAuditEventId();

		//敏感信息加密储存
		encrypt(auditMerchantSettlementInfo);

		//黑名单校验
		blacklistCheck(auditMerchantSettlementInfo);

		/** 2.获取当前审核事件信息（供后续赋值） */
		AuditEvent event = auditEventService.getById(auditEventId);
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(event.getAuditEventType(), event.getNodeCode());

		/** 3.生成一条明细日志记录 */
		AuditEventDetail detail = new AuditEventDetail();
		//是日志记录，非审核流程记录。
		detail.setIfLog(Constants.YES);
		detail.setAuditEventId(event.getId());
		detail.setOperation(CAuditOperation.UPDATE);
		detail.setOperator(LoginUserInfoUtil.getUsername());
		detail.setNodeCode(event.getNodeCode());
		detail.setNodeName(flow.getNodeName());
		detail.setUpdateRecord(JSON.toJSONString(auditMerchantSettlementInfo));
		detail.setDescription("商户结算信息草稿修改");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditMerchantSettlementInfo.setUpdateTime(currTime);
		auditEventDetailService.save(detail);


		/** 4.保存基本信息 */
		return super.updateById(auditMerchantSettlementInfo);
	}

	/**
	 * 调此新增接口，实际是新增1条待审核的草稿结算信息
	 *（需生成审核事件）
	 * @param auditMerchantSettlementInfo
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
	public boolean changeUpdate(AuditMerchantSettlementInfo auditMerchantSettlementInfo) {
		String customerNo = auditMerchantSettlementInfo.getCustomerNo();
		MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
		if (merchantInfo != null){
			if (CMerchantStatus.LOGOUT.equals(merchantInfo.getStatus())){
				throw new MobaoException("商户已处于注销状态,不能进行修改操作");
			}
		}else {
			throw new MobaoException("修改商户不存在，请检查商户号");
		}
		int updateCount = auditMerchantSettlementInfoMapper.countUpdateByCustomerAndAuditEventType(customerNo);
		if (updateCount >= 1){
			throw new MobaoException("商户结算信息已处于修改审核中，请于当前审核通过后，再提交新的审核");
		}

		//验证系统是否支持此类银行
		checkBank(auditMerchantSettlementInfo.getBranchBankNo());

		String currTime = DateUtil.now();
		auditMerchantSettlementInfo.setCreateTime(currTime);
		auditMerchantSettlementInfo.setUpdateTime(currTime);

		//敏感信息加密储存
		encrypt(auditMerchantSettlementInfo);

		//黑名单校验
		blacklistCheck(auditMerchantSettlementInfo);

		/** 1.清空id */
		auditMerchantSettlementInfo.setId(null);

		/** 2.生成审核事件 */
		// 查询“商户结算信息修改”事件初始节点信息，以备后续赋值
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
				CAuditEventType.MERCHANT_SETTLEMENT_UPDATE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
		//流程权限校验
		auditEventService.checkFlowPermission(flow);

		AuditEvent auditEvent = new AuditEvent();
		// 审核模块：运营管理-客户
		auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
		// 事件类型：商户结算信息修改
		auditEvent.setAuditEventType(CAuditEventType.MERCHANT_SETTLEMENT_UPDATE);
		// 审核主体：商户名称
		auditEvent.setSubject(merchantInfo.getName());
		//事件主体编码
		auditEvent.setSubjectCode(auditMerchantSettlementInfo.getCustomerNo());
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
		detail.setUpdateRecord(auditMerchantSettlementInfo.toString());
		detail.setDescription("变更修改商户结算信息");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditEventDetailService.save(detail);

		/** 4.将“审核事件ID”回填到结算信息中 */
		auditMerchantSettlementInfo.setAuditEventId(auditEvent.getId());

		/** 5.新增商户草稿结算信息 */
		return save(auditMerchantSettlementInfo);
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
	 * 入网新增
	 * 调此新增接口，实际是新增1条待审核的草稿结算信息
	 * (无需生成审核事件，在商户基本资料入网接口已生成)
	 * @param auditMerchantSettlementInfo
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
	public boolean accessNetworkCreate(AuditMerchantSettlementInfo auditMerchantSettlementInfo) {
		//验证系统是否支持此类银行
		checkBank(auditMerchantSettlementInfo.getBranchBankNo());
		accessJudgeRepeat(auditMerchantSettlementInfo);
		String currTime = DateUtil.now();

		//敏感信息加密储存
		encrypt(auditMerchantSettlementInfo);

		//黑名单校验
		blacklistCheck(auditMerchantSettlementInfo);

		/** 1.获取审核事件信息，供后续赋值 */
		AuditEvent event = auditEventService.getById(auditMerchantSettlementInfo.getAuditEventId());
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
		detail.setUpdateRecord(auditMerchantSettlementInfo.toString());
		detail.setDescription("入网新增商户结算信息");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditEventDetailService.save(detail);

		/** 3.保存费率草稿 */
		auditMerchantSettlementInfo.setCreateTime(currTime);
		auditMerchantSettlementInfo.setUpdateTime(currTime);
		return save(auditMerchantSettlementInfo);
	}

	/**
	 * 商户中心新增/修改合作商户结算信息
	 * @param auditMerchantSettlementInfo
	 * @return
	 */
	@Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
	public boolean merchantCentreCreateOrUpdate(AuditMerchantSettlementInfo auditMerchantSettlementInfo) {
		//验证系统是否支持此类银行
		checkBank(auditMerchantSettlementInfo.getBranchBankNo());
		String currTime = DateUtil.now();

		//敏感信息加密储存
		encrypt(auditMerchantSettlementInfo);
		//黑名单校验
		blacklistCheck(auditMerchantSettlementInfo);

		if(auditMerchantSettlementInfo.getId()==null){
			auditMerchantSettlementInfo.setCreateTime(currTime);
			auditMerchantSettlementInfo.setUpdateTime(currTime);
			return save(auditMerchantSettlementInfo);
		}else{

			// 拒绝再修改时，整体变为草稿状态
			auditMerchantInfoService.updateStatusByCustomerNo(auditMerchantSettlementInfo.getCustomerNo(), "01");

			auditMerchantSettlementInfo.setUpdateTime(currTime);
			return updateById(auditMerchantSettlementInfo);
		}
	}

	@Override
	public AuditMerchantSettlementInfo getByEventId(Long eventId) {

		LambdaQueryWrapper<AuditMerchantSettlementInfo> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AuditMerchantSettlementInfo::getAuditEventId, eventId);

		AuditMerchantSettlementInfo one = super.getOne(wrapper);

		return one;
	}

	@Override
	public AuditMerchantSettlementInfo getByCustomerNo(String customerNo) {
		LambdaQueryWrapper<AuditMerchantSettlementInfo> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AuditMerchantSettlementInfo::getCustomerNo, customerNo);

		AuditMerchantSettlementInfo one = super.getOne(wrapper);

		return one;
	}

	private void accessJudgeRepeat(AuditMerchantSettlementInfo auditMerchantSettlementInfo) {
		AuditMerchantSettlementInfo repat = getOne(new LambdaQueryWrapper<AuditMerchantSettlementInfo>()
				.eq(AuditMerchantSettlementInfo::getAuditEventId, auditMerchantSettlementInfo.getAuditEventId())
				.eq(AuditMerchantSettlementInfo::getCustomerNo, auditMerchantSettlementInfo.getCustomerNo()));
		if (repat != null){
			throw new MobaoException("当前商户已配置结算信息，如需修改请调用修改接口");
		}
	}



	@Override
	public void changeUpdatePass(Long eventId){

		/** 1.记录修改内容 */
		//根据{审核事件ID}从审核表中获取刚审核通过的记录。
		AuditMerchantSettlementInfo auditMerchantSettlementInfo = getByAuditEventId(eventId);

		//根据{客户号}从正式表中获取正式数据
		LambdaQueryWrapper<MerchantSettlementInfo> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(MerchantSettlementInfo::getCustomerNo, auditMerchantSettlementInfo.getCustomerNo());
		MerchantSettlementInfo formalData = merchantSettlementInfoService.getOne(wrapper);
		formalData.setUpdateTime(DateUtil.now());

		//对比最终修改内容并记录
		//对比之前先将不需要修改的数据统一成正式数据
		MerchantSettlementInfo tempData = new MerchantSettlementInfo();
		BeanUtils.copyProperties(auditMerchantSettlementInfo, tempData);

		tempData.setId(formalData.getId());
		tempData.setCreateTime(formalData.getCreateTime());
		tempData.setUpdateTime(formalData.getUpdateTime());

		//获取修改内容，并将修改内容保存到草稿表中
		String updateRecord = BeanChangeCompareUtil.compare(formalData, tempData);
		auditMerchantSettlementInfo.setUpdateRecord(updateRecord);
		updateById(auditMerchantSettlementInfo);

		/** 2.将通过审核的数据转存入正式表 */
		merchantSettlementInfoService.updateById(tempData);
	}


	private AuditMerchantSettlementInfo getByAuditEventId(Long auditEventId){

		LambdaQueryWrapper<AuditMerchantSettlementInfo> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AuditMerchantSettlementInfo::getAuditEventId, auditEventId);

		AuditMerchantSettlementInfo info = getOne(wrapper);

		return info;
	}


	private void encrypt(AuditMerchantSettlementInfo settlementInfo){

		settlementInfo.setBankAccountName(EndeUtil.encrypt(settlementInfo.getBankAccountName()));
		settlementInfo.setPayeeIdNum(EndeUtil.encrypt(settlementInfo.getPayeeIdNum()));
		settlementInfo.setAccountNo(EndeUtil.encrypt(settlementInfo.getAccountNo()));
		settlementInfo.setReserveMobileNo(EndeUtil.encrypt(settlementInfo.getReserveMobileNo()));

	}

	private void checkBank(String branchBankNo){
		BankCode code = bankCodeService.getCodeByBranchBankNo(branchBankNo);
		if(code == null){
			throw new MobaoException("系统暂不支持此类银行，请前往[银行编码]模块配置]");
		}
	}


	/**
	 * 调用风控系统，进行黑名单校验
	 * @param auditMerchantSettlementInfo
	 */
	private void blacklistCheck(AuditMerchantSettlementInfo auditMerchantSettlementInfo){

		Map<String, String> checkInfo = new HashMap<>(1);
		checkInfo.put("cardNoEncpt", auditMerchantSettlementInfo.getAccountNo());

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

}