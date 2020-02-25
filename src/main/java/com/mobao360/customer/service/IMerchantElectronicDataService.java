package com.mobao360.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.customer.entity.MerchantElectronicData;

import java.io.Serializable;

/**
 * <p>
 *  service接口
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-03
 */
public interface IMerchantElectronicDataService extends IService<MerchantElectronicData> {

    /**
     * 新增
     * @param merchantElectronicData
     * @return
     */
    @Override
    boolean save(MerchantElectronicData merchantElectronicData);
	
	/**
     * 修改
     * @param merchantElectronicData
     * @return
     */
    @Override
    boolean updateById(MerchantElectronicData merchantElectronicData);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);


    /**
     * 根据客户号查询
     * @param customerNo
     * @return
     */
    MerchantElectronicData getByCustomerNo(String customerNo);

}