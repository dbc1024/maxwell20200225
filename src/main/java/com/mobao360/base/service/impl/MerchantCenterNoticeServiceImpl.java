package com.mobao360.base.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.base.entity.MerchantCenterNotice;
import com.mobao360.base.mapper.MerchantCenterNoticeMapper;
import com.mobao360.base.service.IMerchantCenterNoticeService;
import com.mobao360.system.constant.COperation;
import com.mobao360.system.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 * 商户中心公告 service实现类
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-02
 */
@Service
public class MerchantCenterNoticeServiceImpl extends ServiceImpl<MerchantCenterNoticeMapper, MerchantCenterNotice> implements IMerchantCenterNoticeService {

    /**
     * 新增
     * @param merchantCenterNotice
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(MerchantCenterNotice merchantCenterNotice){

		String now = DateUtil.now();
		merchantCenterNotice.setCreateTime(now);
		merchantCenterNotice.setUpdateTime(now);

		LogUtil.insert("商户中心公告", COperation.CREATE, null, merchantCenterNotice);

		return super.save(merchantCenterNotice);
	}
	
	/**
     * 修改
     * @param merchantCenterNotice
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateById(MerchantCenterNotice merchantCenterNotice){

		String now = DateUtil.now();
		merchantCenterNotice.setUpdateTime(now);

		LogUtil.insert("商户中心公告", COperation.UPDATE, null, merchantCenterNotice);
	
		return super.updateById(merchantCenterNotice);
	}
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean removeById(Serializable id){

		LogUtil.insert("商户中心公告", COperation.DELETE, null, id);
		return super.removeById(id);
	}

}