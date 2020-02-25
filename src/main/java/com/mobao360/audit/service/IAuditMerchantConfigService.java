package com.mobao360.audit.service;

import com.mobao360.audit.entity.AuditMerchantConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *  service接口
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-11
 */
public interface IAuditMerchantConfigService extends IService<AuditMerchantConfig> {

    /**
     * 新增
     * @param auditMerchantConfig
     * @return
     */
    @Override
    boolean save(AuditMerchantConfig auditMerchantConfig);
	
	/**
     * 修改
     * @param auditMerchantConfig
     * @return
     */
    boolean updateDraft(AuditMerchantConfig auditMerchantConfig);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 入网新增
     * @param auditMerchantConfig
     * @return
     */
    boolean accessNetworkCreate(AuditMerchantConfig auditMerchantConfig);

    /**
     * 变更修改
     * @param auditMerchantConfig
     * @return
     */
    boolean changeUpdate(AuditMerchantConfig auditMerchantConfig);

    /**
     * 审核通过后续流程
     * @param auditEventId
     */
    void changeUpdatePass(Long auditEventId);

    /**
     * 结算关闭
     * @param params
     */
    void settlementClose(Map<String, String> params);

    /**
     * 审核通过后续流程
     * @param customerNo
     */
    void settlementClosePass(String customerNo);

    /**
     * 交易关闭
     * @param params
     */
    void transactionClose(Map<String, String> params);

    /**
     * 审核通过后续流程
     * @param customerNo
     */
    void tradeClosePass(String customerNo);


    /**
     * 根据审核事件ID查询
     * @param eventId
     * @return
     */
    AuditMerchantConfig getByEventId(Long eventId);
}