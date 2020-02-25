package com.mobao360.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.base.mapper.OtherMapper;
import com.mobao360.base.entity.Other;
import com.mobao360.base.service.IOtherService;
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
 * @since 2019-01-18
 */
@Service
public class OtherServiceImpl extends ServiceImpl<OtherMapper, Other> implements IOtherService {

    /**
     * 新增
     * @param other
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(Other other){
	
		return super.save(other);
	}
	
	/**
     * 修改
     * @param other
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateById(Other other){
	
		return super.updateById(other);
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