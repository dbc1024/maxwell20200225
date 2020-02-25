package com.mobao360.audit.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.api.feign.TradeFeign;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventDetail;
import com.mobao360.audit.entity.AuditEventFlow;
import com.mobao360.audit.entity.AuditMerchantRoute;
import com.mobao360.audit.mapper.AuditMerchantRouteMapper;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.audit.service.IAuditMerchantRouteService;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.system.constant.*;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.LoginUserInfoUtil;
import com.mobao360.system.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 商户消费路由 service实现类
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-15
 */
@Service
public class AuditMerchantRouteServiceImpl extends ServiceImpl<AuditMerchantRouteMapper, AuditMerchantRoute> implements IAuditMerchantRouteService {
	private Logger logger= LoggerFactory.getLogger(AuditMerchantRouteServiceImpl.class);

	@Autowired
	private IAuditEventService auditEventService;

	@Autowired
	private IAuditEventDetailService auditEventDetailService;

	@Autowired
	private IAuditEventFlowService auditEventFlowService;

	@Autowired
	private IMerchantInfoService merchantInfoService;

	@Autowired
	private AuditMerchantRouteMapper auditMerchantRouteMapper;

	@Autowired
	private TradeFeign tradeFeign;

    /**
     * 新增
     * @param auditMerchantRoute
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(AuditMerchantRoute auditMerchantRoute){
	
		return super.save(auditMerchantRoute);
	}
	
	/**
     * 修改
     * @param auditMerchantRoute
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateDraft(AuditMerchantRoute auditMerchantRoute){
		String currTime = DateUtil.now();
		Long auditEventId = auditMerchantRoute.getAuditEventId();

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
		detail.setUpdateRecord(JSON.toJSONString(auditMerchantRoute));
		detail.setDescription("商户结算信息草稿修改");
		detail.setCreateTime(currTime);

		/** 3.保存日志明细 */
		auditEventDetailService.save(detail);

		auditMerchantRoute.setUpdateTime(currTime);
		return super.updateById(auditMerchantRoute);
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
	 * 路由新增事件
	 * @param route
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
	public boolean changeCreate(AuditMerchantRoute route) {
		String customerNo = route.getCustomerNo();
		MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
		String currTime = DateUtil.now();
		route.setCreateTime(currTime);
		route.setUpdateTime(currTime);
		/** 1.生成审核事件 */
		// 查询“路由变更”初始节点信息，以备后续赋值
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
				CAuditEventType.MERCHANT_ROUTE_CHANGE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
		//流程权限校验
		auditEventService.checkFlowPermission(flow);

		AuditEvent auditEvent = new AuditEvent();
		// 审核模块：运营管理-客户
		auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
		// 事件类型：商户路由变更
		auditEvent.setAuditEventType(CAuditEventType.MERCHANT_ROUTE_CHANGE);
		// 审核主体：商户名称
		auditEvent.setSubject(merchantInfo.getName());
		//事件主体编码
		auditEvent.setSubjectCode(route.getCustomerNo());
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
		detail.setUpdateRecord(route.toString());
		detail.setDescription("新增商户路由资料");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditEventDetailService.save(detail);

		/** 3.将“审核事件ID”回填到路由信息中*/
		route.setAuditEventId(auditEvent.getId());

		/** 4.新增商户路由草稿*/
		return save(route);
	}

	@Override
	public AuditMerchantRoute getByEventId(Long eventId) {
		AuditMerchantRoute auditMerchantRoute = getOne(new LambdaQueryWrapper<AuditMerchantRoute>().eq(AuditMerchantRoute::getAuditEventId, eventId));
		return auditMerchantRoute;
	}

	/**
	 * 路由变更修改事件
	 * @param route
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
	public boolean changeUpdate(AuditMerchantRoute route) {
		String customerNo = route.getCustomerNo();
		MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
		if (merchantInfo != null){
			if (CMerchantStatus.LOGOUT.equals(merchantInfo.getStatus())){
			throw new MobaoException("当前账户已注销");
			}
		}else {
			throw new MobaoException("不存在此商户，请检查商户号");
		}
		int updateCount = auditMerchantRouteMapper.countUpdateByCustomerAndAuditEventType(customerNo);
		if (updateCount >= 1){
			throw new MobaoException("当前商户结算信息，已处于修改审核中，请于审核通过之后再提交新的审核");
		}
		String currTime = DateUtil.now();
		route.setCreateTime(currTime);
		route.setUpdateTime(currTime);
		/** 1.生成审核事件 */
		// 查询“路由变更”初始节点信息，以备后续赋值
		AuditEventFlow flow = auditEventFlowService.getNodeInfoByTypeAndNodeCode(
				CAuditEventType.MERCHANT_ROUTE_CHANGE, Constants.AUDIT_EVENT_BEGIN_NODE_CODE);

		//流程权限校验
		auditEventService.checkFlowPermission(flow);

		AuditEvent auditEvent = new AuditEvent();
		// 审核模块：运营管理-客户
		auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
		// 事件类型：商户路由变更
		auditEvent.setAuditEventType(CAuditEventType.MERCHANT_ROUTE_CHANGE);
		// 审核主体：商户名称
		auditEvent.setSubject(merchantInfo.getName());
		//事件主体编码
		auditEvent.setSubjectCode(merchantInfo.getCustomerNo());
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
		detail.setUpdateRecord(route.toString());
		detail.setDescription("变更修改商户路由资料");
		detail.setCreateTime(currTime);
		//保存明细日志记录
		auditEventDetailService.save(detail);

		/** 3.将“审核事件ID”回填到路由信息中*/
		route.setAuditEventId(auditEvent.getId());

		/** 4.新增商户路由草稿*/
		return save(route);
	}


	@Override
	public void changePass(Long eventId){

		AuditMerchantRoute route = getByEventId(eventId);
		Long formalId = route.getFormalId();
		Map<String, Object> mapRoute = BeanUtil.beanToMap(route);

		/** 判断事件类型，不同审核事件类型审核通过后有不同的后续处理流程*/
		//变更新增
		if(formalId == null){
			//将通过审核的数据推送给交易系统
			Result result;
			try {
				result = tradeFeign.createOrUpdateRoute(mapRoute);
			}catch (Exception e){
				throw new MobaoException("交易系统调用失败", e);
			}

			logger.info("交易系统返回："+ JSON.toJSONString(result));
			if(!Constants.YES.equals(result.getRetCode())){
				throw new MobaoException("交易系统新增商户路由,返回异常："+ result.getRetMsg());
			}
			logger.info("调用交易系统新增商户路由成功");

			//变更修改
		}else {
			//修改时需要传id,交易系统此模块id叫no
			mapRoute.put("no", formalId);

			//将通过审核的数据推送给交易系统
			Result result;
			try {
				result = tradeFeign.createOrUpdateRoute(mapRoute);
			}catch (Exception e){
				throw new MobaoException("交易系统调用失败", e);
			}

			logger.info("交易系统返回："+ JSON.toJSONString(result));
			if(!Constants.YES.equals(result.getRetCode())){
				throw new MobaoException("交易系统修改商户路由,返回异常："+ result.getRetMsg());
			}
			logger.info("调用交易系统修改商户路由成功");
		}

	}


}