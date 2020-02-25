package com.mobao360.base.service;

import com.mobao360.base.entity.Other;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;

/**
 * <p>
 *  service接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-18
 */
public interface IOtherService extends IService<Other> {

    /**
     * 新增
     * @param other
     * @return
     */
    @Override
    boolean save(Other other);
	
	/**
     * 修改
     * @param other
     * @return
     */
    @Override
    boolean updateById(Other other);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);


}