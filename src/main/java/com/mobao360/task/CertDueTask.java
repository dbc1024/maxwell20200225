package com.mobao360.task;

import cn.hutool.core.date.DateUtil;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventDetail;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.system.constant.*;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商户证件到期检测定时任务
 * @author: CSZ 991587100@qq.com
 * @date: 2019/2/27 09:22
 */
@Log4j2
@Component
@EnableScheduling
public class CertDueTask {
    private Logger logger = LoggerFactory.getLogger(CertDueTask.class);

    @Autowired
    private IAuditEventService auditEventService;
    @Autowired
    private IMerchantInfoService merchantInfoService;
    @Autowired
    private IAuditEventDetailService auditEventDetailService;

    /**
     * 检测商户证件【法人身份证，营业执照，ICP】
     * 若其一过期，则将商户状态置为“锁定”
     */
    @Scheduled(cron = "0 1 0 * * ?")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void certDueCheckTask(){
        if(!InitTaskData.isTaskServer){
            return;
        }
        logger.info("[定时任务]商户证件过期自动锁定开始执行");

        String now = DateUtil.now();
        List<MerchantInfo> merchantInfos = merchantInfoService.checkDueCert(DateUtil.today());
        if(merchantInfos!=null && merchantInfos.size()>0){
            for (MerchantInfo merchantInfo : merchantInfos) {
                logger.info("检测到可锁定商户[{}]", merchantInfo.getName()+merchantInfo.getCustomerNo());

                /** 锁定 */
                merchantInfo.setStatus(CMerchantStatus.LOCKED);
                merchantInfo.setLockTime(now);
                merchantInfoService.updateById(merchantInfo);

                /** 生成审核事件 */
                AuditEvent auditEvent = new AuditEvent();
                // 审核模块：运营管理-客户
                auditEvent.setAuditModule(CAuditModule.MAXWELL_CUSTOMER);
                // 事件类型：商户锁定
                auditEvent.setAuditEventType(CAuditEventType.MERCHANT_LOCK);
                // 审核主体：商户名称
                auditEvent.setSubject(merchantInfo.getName());
                // 审核主体编码：客户号
                auditEvent.setSubjectCode(merchantInfo.getCustomerNo());
                // 事件状态：直接通过
                auditEvent.setStatus(CAuditEventStatus.PASS);
                // 节点信息赋值
                auditEvent.setNodeCode(Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
                auditEvent.setCreateTime(now);
                auditEvent.setUpdateTime(now);

                // 保存审核事件
                auditEventService.save(auditEvent);

                /** 生成明细记录 */
                AuditEventDetail detail = new AuditEventDetail();
                //写死一条审核流程记录。
                detail.setIfLog(Constants.NO);
                detail.setAuditEventId(auditEvent.getId());
                detail.setOperation(CAuditOperation.PASS);
                detail.setOperator("定时任务");
                detail.setNodeCode(Constants.AUDIT_EVENT_BEGIN_NODE_CODE);
                detail.setNodeName("自动锁定");
                //用于记录一些状态变更事件对应实体的原始状态
                detail.setUpdateRecord("unlock");
                detail.setDescription("商户证件到期自动锁定");
                detail.setCreateTime(now);
                //保存明细日志记录
                auditEventDetailService.save(detail);
            }

            logger.info("商户证件过期自动锁定执行成功");
        }else {
            logger.info("未检测到可锁定商户");
        }


    }


    /**
     * 检测商户证件【法人身份证，营业执照，ICP】
     * 若其一过期，则将商户状态置为“锁定”
     */
//    @Scheduled(cron = "0 1 0 * * ?")
//    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
//    public void certDueCheckTask(){
//        if(!InitTaskData.isTaskServer){
//            return;
//        }
//
//        logger.info("[定时任务]商户证件过期自动锁定开始执行");
//        List<String> customerNoList = merchantInfoMapper.checkDueCert(DateUtil.today());
//
//        if(customerNoList != null && customerNoList.size()>0){
//            logger.info("检测到可锁定商户"+ customerNoList.toString());
//            merchantInfoMapper.certDueLock(customerNoList);
//
//            for (String customerNo : customerNoList) {
//                merchantInfoService.getByCustomerNo(customerNo);
//
//
//            }
//
//            logger.info("商户证件过期自动锁定执行成功");
//        }
//
//        logger.info("未检测到可锁定商户");
//    }

}
