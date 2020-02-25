package com.mobao360.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.customer.entity.MerchantFundSettlement;

import java.io.Serializable;

/**
 * <p>
 *  service接口
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-17
 */
public interface IMerchantFundSettlementService extends IService<MerchantFundSettlement> {

    /**
     * 新增
     * @param merchantFundSettlement
     * @return
     */
    @Override
    boolean save(MerchantFundSettlement merchantFundSettlement);
	
	/**
     * 修改
     * @param merchantFundSettlement
     * @return
     */
    @Override
    boolean updateById(MerchantFundSettlement merchantFundSettlement);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);


    /**
     *
     * @param customerNo
     * @return
     */
    MerchantFundSettlement getByCustomerNo(String customerNo);
}