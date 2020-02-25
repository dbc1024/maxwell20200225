package com.mobao360.customer.service;

import com.mobao360.audit.entity.AuditMerchantConfig;
import com.mobao360.customer.entity.MerchantConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 商户配置信息 service接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-29
 */
public interface IMerchantConfigService extends IService<MerchantConfig> {

    /**
     * 新增
     * @param merchantConfig
     * @return
     */
    @Override
    boolean save(MerchantConfig merchantConfig);
	
	/**
     * 修改
     * @param merchantConfig
     * @return
     */
    @Override
    boolean updateById(MerchantConfig merchantConfig);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);


    /**
     * 根据商户号获取 商户配置正式数据（tip：每个商户只能有一条配置记录）
     * @param customerNo
     * @return
     */
    MerchantConfig getByCustomerNo(String customerNo);

}