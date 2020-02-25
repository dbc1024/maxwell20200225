package com.mobao360.customer.service;

import com.mobao360.customer.entity.Salesman;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;

/**
 * <p>
 *  service接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-14
 */
public interface ISalesmanService extends IService<Salesman> {

    /**
     * 新增
     * @param salesman
     * @return
     */
    @Override
    boolean save(Salesman salesman);
	
	/**
     * 修改
     * @param salesman
     * @return
     */
    @Override
    boolean updateById(Salesman salesman);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);


}