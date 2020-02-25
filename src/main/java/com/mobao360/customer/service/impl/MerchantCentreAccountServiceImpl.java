package com.mobao360.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.customer.entity.MerchantCentreAccount;
import com.mobao360.customer.mapper.MerchantCentreAccountMapper;
import com.mobao360.customer.service.IMerchantCentreAccountService;
import com.mobao360.system.constant.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 * service实现类
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2018-12-28
 */
@Service
public class MerchantCentreAccountServiceImpl extends ServiceImpl<MerchantCentreAccountMapper, MerchantCentreAccount> implements IMerchantCentreAccountService {


    /**
     * 新增
     *
     * @param merchantCentreAccount
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean save(MerchantCentreAccount merchantCentreAccount) {

        return super.save(merchantCentreAccount);
    }



    /**
     * 修改
     *
     * @param merchantCentreAccount
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean updateById(MerchantCentreAccount merchantCentreAccount) {

        return super.updateById(merchantCentreAccount);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {

        return super.removeById(id);
    }

    @Override
    public MerchantCentreAccount findPushedAccount() {

        LambdaQueryWrapper<MerchantCentreAccount> wrapper = new LambdaQueryWrapper<>();

        wrapper.ne(MerchantCentreAccount::getTask1, Constants.YES);
        wrapper.last("AND ROWNUM = 1");

        return getOne(wrapper);
    }

    @Override
    public MerchantCentreAccount getByCustomerNo(String customerNo) {

        LambdaQueryWrapper<MerchantCentreAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantCentreAccount::getCustomerNo, customerNo);

        return getOne(wrapper);
    }

}