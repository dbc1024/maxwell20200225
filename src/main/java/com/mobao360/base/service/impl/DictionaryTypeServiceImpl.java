package com.mobao360.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.base.entity.DictionaryType;
import com.mobao360.base.mapper.DictionaryTypeMapper;
import com.mobao360.base.service.IDictionaryTypeService;
import com.mobao360.system.constant.Constants;
import com.mobao360.system.exception.MobaoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@Service
public class DictionaryTypeServiceImpl extends ServiceImpl<DictionaryTypeMapper, DictionaryType> implements IDictionaryTypeService {

    /**
     * 新增
     * @param dictionaryType
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(DictionaryType dictionaryType){

        DictionaryType one = getOne(new LambdaQueryWrapper<DictionaryType>().eq(DictionaryType::getCode, dictionaryType.getCode()));
        if(one != null){
            throw new MobaoException("数据字典类型编码已存在，请重新输入。");
        }
        dictionaryType.setIfEdit("1");
        super.save(dictionaryType);

        return true;
    }


    /**
     * 修改
     * @param dictionaryType
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateById(DictionaryType dictionaryType){


        LambdaQueryWrapper<DictionaryType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictionaryType::getCode, dictionaryType.getCode());
        wrapper.ne(DictionaryType::getId, dictionaryType.getId());
        DictionaryType one = getOne(wrapper);
        if(one != null){
            throw new MobaoException("数据字典类型编码已存在，请重新输入。");
        }

        DictionaryType primaryBean = getById(dictionaryType.getId());
        if(!Constants.YES.equals(primaryBean.getIfEdit())){
            throw new MobaoException("此数据为数据库导入，不可变动");
        }
        super.updateById(dictionaryType);

        return true;
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean removeById(Serializable id){

        DictionaryType primaryBean = getById(id);
        if(!Constants.YES.equals(primaryBean.getIfEdit())){
            throw new MobaoException("此数据为数据库导入，不可变动");
        }
        super.removeById(id);

        return true;
    }

}
