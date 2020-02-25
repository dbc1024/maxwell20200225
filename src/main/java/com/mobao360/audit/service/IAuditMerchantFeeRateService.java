package com.mobao360.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.audit.entity.AuditMerchantFeeRate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  service接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-18
 */
public interface IAuditMerchantFeeRateService extends IService<AuditMerchantFeeRate> {


    /**
     * 入网新增
     * 调此新增接口，实际是新增n条待审核的草稿费率。
     * (无需生成审核事件，在商户基本资料入网接口已生成)
     * @param rateList
     * @return
     */
    boolean accessNetworkCreate(List<AuditMerchantFeeRate> rateList);


    /**
     * 调此新增接口，实际是新增n条待审核的草稿费率。
     *（需生成审核事件）
     * @param rateList
     * @return
     */
    boolean changeCreate(List<AuditMerchantFeeRate> rateList);


    /**
     * 调此新增接口，实际是新增1条待审核的草稿费率。
     *（需生成审核事件）
     * @param auditMerchantFeeRate
     * @return
     */
    boolean changeUpdate(AuditMerchantFeeRate auditMerchantFeeRate);

    /**
     * 变更（变更新增，变更修改）审核通过后续流程
     * @param eventId
     */
    void changePass(Long eventId);

    /**
     * 新增
     * @param auditMerchantFeeRate
     * @return
     */
    @Override
    boolean save(AuditMerchantFeeRate auditMerchantFeeRate);
	
	/**
     * 修改
     * @param auditMerchantFeeRate
     * @return
     */
    boolean updateDraft(AuditMerchantFeeRate auditMerchantFeeRate);


    /**
     * 对于"商户入网"，"费率变更-新增"这两个事件
     * 对应的草稿修改操作，实际是批量新增，有以下几个步骤：
     *
     * 1.删除此事件对应的所有费率
     * 2.将所有数据的id置null
     * 3.批量新增费率草稿
     * @param rateList
     * @return
     */
    boolean batchUpdateDraft(List<AuditMerchantFeeRate> rateList);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 根据审核事件ID查询列表
     * @param eventId
     * @return
     */
    List<AuditMerchantFeeRate> getByEventId(Long eventId);

}