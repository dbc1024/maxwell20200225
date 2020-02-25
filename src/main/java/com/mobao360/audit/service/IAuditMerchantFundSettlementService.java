package com.mobao360.audit.service;

import com.mobao360.audit.entity.AuditMerchantFundSettlement;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *  service接口
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-17
 */
public interface IAuditMerchantFundSettlementService extends IService<AuditMerchantFundSettlement> {

    /**
     * 新增
     * @param auditMerchantFundSettlement
     * @return
     */
    @Override
    boolean save(AuditMerchantFundSettlement auditMerchantFundSettlement);
	
	/**
     * 修改
     * @param auditMerchantFundSettlement
     * @return
     */
    @Override
    boolean updateById(AuditMerchantFundSettlement auditMerchantFundSettlement);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 商户垫资结算资料修改
     * @param auditMerchantFundSettlement
     */
    boolean changeUpdate(AuditMerchantFundSettlement auditMerchantFundSettlement);

    /**
     * 商户垫资结算资料新增
     * @param auditMerchantFundSettlement
     */
    boolean changeCreate(AuditMerchantFundSettlement auditMerchantFundSettlement);

    /**
     * 审核通过后续处理
     * @param eventId
     */
    void changePass(Long eventId);

    /**
     * 修改草稿审核信息
     * @param auditMerchantFundSettlement
     * @return
     */
    boolean updateDraft(AuditMerchantFundSettlement auditMerchantFundSettlement);

}