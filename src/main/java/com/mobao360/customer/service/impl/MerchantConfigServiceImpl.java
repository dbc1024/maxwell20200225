package com.mobao360.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.audit.service.IAuditMerchantConfigService;
import com.mobao360.customer.entity.MerchantConfig;
import com.mobao360.customer.mapper.MerchantConfigMapper;
import com.mobao360.customer.service.IMerchantConfigService;
import com.mobao360.customer.service.IMerchantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 * 商户配置信息 service实现类
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2018-12-29
 */
@Service
public class MerchantConfigServiceImpl extends ServiceImpl<MerchantConfigMapper, MerchantConfig> implements IMerchantConfigService {

    /**
     * 新增
     * @param merchantConfig
     * @return
     */
    @Override
    public boolean save(MerchantConfig merchantConfig) {

        return super.save(merchantConfig);
    }

    /**
     * 修改
     * @param merchantConfig
     * @return
     */
    @Override
    public boolean updateById(MerchantConfig merchantConfig) {

        return super.updateById(merchantConfig);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {

        return super.removeById(id);
    }


    @Override
    public MerchantConfig getByCustomerNo(String customerNo) {
        return getOne(new LambdaQueryWrapper<MerchantConfig>().eq(MerchantConfig::getCustomerNo, customerNo));
    }



}