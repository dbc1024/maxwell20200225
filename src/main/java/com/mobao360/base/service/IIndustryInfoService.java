package com.mobao360.base.service;

import com.mobao360.base.entity.IndustryInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
public interface IIndustryInfoService extends IService<IndustryInfo> {

    /**
     * 新增
     * @param industryInfo
     * @return
     */
    @Override
    boolean save(IndustryInfo industryInfo);

    /**
     * 修改
     * @param industryInfo
     * @return
     */
    @Override
    boolean updateById(IndustryInfo industryInfo);
}
