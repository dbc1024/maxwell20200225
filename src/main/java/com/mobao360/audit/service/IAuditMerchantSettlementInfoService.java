package com.mobao360.audit.service;

import com.mobao360.audit.entity.AuditMerchantSettlementInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 商户结算信息 service接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-28
 */
public interface IAuditMerchantSettlementInfoService extends IService<AuditMerchantSettlementInfo> {

    /**
     * 新增
     * @param auditMerchantSettlementInfo
     * @return
     */
    @Override
    boolean save(AuditMerchantSettlementInfo auditMerchantSettlementInfo);
	
	/**
     * 修改
     * @param auditMerchantSettlementInfo
     * @return
     */
    boolean updateDraft(AuditMerchantSettlementInfo auditMerchantSettlementInfo);

    /**
     * 调此新增接口，实际是新增1条待审核的草稿结算信息
     *（需生成审核事件）
     * @param auditMerchantSettlementInfo
     * @return
     */
    boolean changeUpdate(AuditMerchantSettlementInfo auditMerchantSettlementInfo);

    /**
     * 审核通过后续操作
     * @param eventId
     */
    void changeUpdatePass(Long eventId);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);


    /**
     * 入网新增
     * 调此新增接口，实际是新增1条待审核的草稿结算信息
     * (无需生成审核事件，在商户基本资料入网接口已生成)
     * @param auditMerchantSettlementInfo
     * @return
     */
    boolean accessNetworkCreate(AuditMerchantSettlementInfo auditMerchantSettlementInfo);

    /**
     * 商户中心新增/修改合作商户结算信息
     * @param auditMerchantSettlementInfo
     * @return
     */
    boolean merchantCentreCreateOrUpdate(AuditMerchantSettlementInfo auditMerchantSettlementInfo);

    /**
     * 根据审核事件ID详情查询
     * @param eventId
     * @return
     */
    AuditMerchantSettlementInfo getByEventId(Long eventId);

    /**
     * 根据客户号查询详情
     * @param customerNo
     * @return
     */
    AuditMerchantSettlementInfo getByCustomerNo(String customerNo);
}