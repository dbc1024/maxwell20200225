package com.mobao360.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.audit.service.IAuditMerchantElectronicDataService;
import com.mobao360.customer.entity.MerchantElectronicData;
import com.mobao360.customer.mapper.MerchantElectronicDataMapper;
import com.mobao360.customer.service.IMerchantElectronicDataService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2019-01-03
 */
@Service
public class MerchantElectronicDataServiceImpl extends ServiceImpl<MerchantElectronicDataMapper, MerchantElectronicData> implements IMerchantElectronicDataService {

    /**
     * 新增
     * @param merchantElectronicData
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(MerchantElectronicData merchantElectronicData){
	
		return super.save(merchantElectronicData);
	}
	
	/**
     * 修改
     * @param merchantElectronicData
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateById(MerchantElectronicData merchantElectronicData){

		return super.updateById(merchantElectronicData);
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
	public MerchantElectronicData getByCustomerNo(String customerNo) {

		LambdaQueryWrapper<MerchantElectronicData> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(MerchantElectronicData::getCustomerNo, customerNo);

		return getOne(wrapper);
	}


}