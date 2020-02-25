package com.mobao360.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.customer.entity.MerchantFundSettlement;
import com.mobao360.customer.mapper.MerchantFundSettlementMapper;
import com.mobao360.customer.service.IMerchantFundSettlementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 *  service实现类
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-17
 */
@Service
public class MerchantFundSettlementServiceImpl extends ServiceImpl<MerchantFundSettlementMapper, MerchantFundSettlement> implements IMerchantFundSettlementService {

    /**
	 *
     * 新增
     * @param merchantFundSettlement
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(MerchantFundSettlement merchantFundSettlement){
	
		return super.save(merchantFundSettlement);
	}
	
	/**
     * 修改
     * @param merchantFundSettlement
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateById(MerchantFundSettlement merchantFundSettlement){
	
		return super.updateById(merchantFundSettlement);
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
	public MerchantFundSettlement getByCustomerNo(String customerNo) {
		LambdaQueryWrapper<MerchantFundSettlement> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(MerchantFundSettlement::getCustomerNo,customerNo);
		return getOne(wrapper);
	}


}