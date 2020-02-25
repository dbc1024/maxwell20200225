package com.mobao360.audit.service;

import com.mobao360.audit.entity.AuditMerchantCentreAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.system.utils.Result;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *  service接口
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2018-12-28
 */
public interface IAuditMerchantCentreAccountService extends IService<AuditMerchantCentreAccount> {

    /**
     * 新增
     * @param merchantCentreAccount
     * @return
     */
    @Override
    boolean save(AuditMerchantCentreAccount merchantCentreAccount);
	
	/**
     * 修改
     * @param merchantCentreAccount
     * @return
     */
    boolean updateDraft(AuditMerchantCentreAccount merchantCentreAccount);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 入网新增
     * @param auditMerchantCentreAccount
     * @return
     */
    boolean accessNetworkCreate(AuditMerchantCentreAccount auditMerchantCentreAccount);

    /**
     * 商户中心账号变更修改
     * @param auditMerchantCentreAccount
     * @return
     */
    boolean changeUpdate(AuditMerchantCentreAccount auditMerchantCentreAccount);

    /**
     * 商户中心账号变更修改审核通过后续流程
     * @param eventId
     */
    void changeUpdatePass(Long eventId);


    /**
     * 根据审核事件ID查询
     * @param eventId
     * @return
     */
    AuditMerchantCentreAccount getByEventId(Long eventId);

}