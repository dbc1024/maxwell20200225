package com.mobao360.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.customer.entity.MerchantSettlementInfo;
import com.mobao360.customer.mapper.MerchantSettlementInfoMapper;
import com.mobao360.customer.service.IMerchantSettlementInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 * 商户结算信息 service实现类
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-25
 */
@Service
public class MerchantSettlementInfoServiceImpl extends ServiceImpl<MerchantSettlementInfoMapper, MerchantSettlementInfo> implements IMerchantSettlementInfoService {

    /**
     * 新增
     * @param merchantSettlementInfo
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(MerchantSettlementInfo merchantSettlementInfo){

		return super.save(merchantSettlementInfo);
	}


	/**
     * 修改
     * @param merchantSettlementInfo
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateById(MerchantSettlementInfo merchantSettlementInfo){

		return super.updateById(merchantSettlementInfo);
	}

	/**
     * 删除
     * @param id
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean removeById(Serializable id){

		return super.removeById(id);
	}

    @Override
    public MerchantSettlementInfo getByCustomerNo(String customerNo) {

        LambdaQueryWrapper<MerchantSettlementInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantSettlementInfo::getCustomerNo, customerNo);

        return getOne(wrapper);
    }

}