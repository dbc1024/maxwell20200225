package com.mobao360.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.base.entity.DictionaryType;

import java.io.Serializable;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
public interface IDictionaryTypeService extends IService<DictionaryType> {


    /**
     * 新增
     * @param dictionaryType
     * @return
     */
    @Override
    boolean save(DictionaryType dictionaryType);

    /**
     * 修改
     * @param dictionaryType
     * @return
     */
    @Override
    boolean updateById(DictionaryType dictionaryType);

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

}
