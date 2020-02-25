package com.mobao360.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.customer.entity.MerchantSettlementInfo;

import java.io.Serializable;

/**
 * <p>
 * 商户结算信息 service接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-25
 */
public interface IMerchantSettlementInfoService extends IService<MerchantSettlementInfo> {

    /**
     * 新增
     * @param merchantSettlementInfo
     * @return
     */
    @Override
    boolean save(MerchantSettlementInfo merchantSettlementInfo);

	
	/**
     * 修改
     * @param merchantSettlementInfo
     * @return
     */
    @Override
    boolean updateById(MerchantSettlementInfo merchantSettlementInfo);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);


    /**
     * 通过客户号查询
     * @param customerNo
     * @return
     */
    MerchantSettlementInfo getByCustomerNo(String customerNo);

}