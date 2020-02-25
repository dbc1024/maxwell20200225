package com.mobao360.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.audit.entity.AuditMerchantElectronicData;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *  service接口
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-03
 */
public interface IAuditMerchantElectronicDataService extends IService<AuditMerchantElectronicData> {

    /**
     * 新增
     * @param auditMerchantElectronicData
     * @return
     */
    @Override
    boolean save(AuditMerchantElectronicData auditMerchantElectronicData);
	
	/**
     * 修改
     * @param auditMerchantElectronicData
     * @return
     */
    boolean updateDraft(AuditMerchantElectronicData auditMerchantElectronicData);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 入网新增
     * @param merchantElectronicData
     * @return
     */
    boolean accessNetworkCreate(AuditMerchantElectronicData merchantElectronicData);


    /**
     * 根据审核事件ID查询实体
     * @param eventId
     * @return
     */
    AuditMerchantElectronicData getByEventId(Long eventId);

    /**
     * 根据客户号查询详情
     * @param customerNo
     * @return
     */
    AuditMerchantElectronicData getByCustomerNo(String customerNo);


    /**
     * 补充电子资料
     * @param electronicData
     * @return
     */
    void supplement(AuditMerchantElectronicData electronicData);

    /**
     * 变更修改事件
     * @param electronicData
     * @return
     */
    boolean changeUpdate(AuditMerchantElectronicData electronicData);

    /**
     * 商户电子资料修改事件审核通过后续流程
     * @param auditEventId
     */
    void changeUpdatePass(Long auditEventId);
}