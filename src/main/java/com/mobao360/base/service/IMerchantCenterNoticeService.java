package com.mobao360.base.service;

import com.mobao360.base.entity.MerchantCenterNotice;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;

/**
 * <p>
 * 商户中心公告 service接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-02
 */
public interface IMerchantCenterNoticeService extends IService<MerchantCenterNotice> {

    /**
     * 新增
     * @param merchantCenterNotice
     * @return
     */
    @Override
    boolean save(MerchantCenterNotice merchantCenterNotice);
	
	/**
     * 修改
     * @param merchantCenterNotice
     * @return
     */
    @Override
    boolean updateById(MerchantCenterNotice merchantCenterNotice);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);


}