package com.mobao360.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.customer.mapper.SalesmanMapper;
import com.mobao360.customer.entity.Salesman;
import com.mobao360.customer.service.ISalesmanService;
import java.io.Serializable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  service实现类
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-14
 */
@Service
public class SalesmanServiceImpl extends ServiceImpl<SalesmanMapper, Salesman> implements ISalesmanService {

    /**
     * 新增
     * @param salesman
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(Salesman salesman){

		return super.save(salesman);
	}

	/**
     * 修改
     * @param salesman
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateById(Salesman salesman){
	
		return super.updateById(salesman);
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


}