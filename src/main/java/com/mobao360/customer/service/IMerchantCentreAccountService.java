package com.mobao360.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.customer.entity.MerchantCentreAccount;

import java.io.Serializable;

/**
 * <p>
 *  service接口
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2018-12-28
 */
public interface IMerchantCentreAccountService extends IService<MerchantCentreAccount> {

    /**
     * 新增
     * @param merchantCentreAccount
     * @return
     */
    @Override
    boolean save(MerchantCentreAccount merchantCentreAccount);

	
	/**
     * 修改
     * @param merchantCentreAccount
     * @return
     */
    @Override
    boolean updateById(MerchantCentreAccount merchantCentreAccount);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 寻找可被推送的商户中心账号
     * @return
     */
    MerchantCentreAccount findPushedAccount();


    /**
     * 根据客户号查询
     * @param customerNo
     * @return
     */
    MerchantCentreAccount getByCustomerNo(String customerNo);

}