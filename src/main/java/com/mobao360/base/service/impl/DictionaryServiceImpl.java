package com.mobao360.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.base.entity.Dictionary;
import com.mobao360.base.entity.DictionaryType;
import com.mobao360.base.mapper.DictionaryMapper;
import com.mobao360.base.service.IDictionaryService;
import com.mobao360.base.service.IDictionaryTypeService;
import com.mobao360.system.constant.COperation;
import com.mobao360.system.constant.Constants;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements IDictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private IDictionaryTypeService dictionaryTypeService;

    @Override
    public List<Map<String, String>> getKeyValueListByCode(String typeCode) {

        return dictionaryMapper.getKeyValueListByCode(typeCode);
    }

    @Override
    public List<Map<String, String>> pageQuery(Map<String, String> params) {

        return dictionaryMapper.pageQuery(params);
    }


    /**
     * 数据字典增、删、改的方法中应该调用此方法,
     * 传入对应数据字典类型 typeCode
     * 清除对应 typeCode 的缓存数据
     * typeCode="0"时，清除所有缓存
     * @param typeCode
     */
    @Override
    public void cleanDictionaryCache(String typeCode){

        DictionaryUtil.cleanDictionaryCache(typeCode);
    }


    /**
     * 新增
     * @param dictionary
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(Dictionary dictionary){

        LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dictionary::getDicTypeId, dictionary.getDicTypeId());
        wrapper.eq(Dictionary::getValue, dictionary.getValue());
        Dictionary one = getOne(wrapper);
        if(one != null){
            throw new MobaoException("该类型下此值已存在，请重新输入。");
        }

        dictionary.setIfEdit(Constants.YES);
        super.save(dictionary);
        DictionaryType dictionaryType = dictionaryTypeService.getById(dictionary.getDicTypeId());
        cleanDictionaryCache(dictionaryType.getCode());

        //日志记录
        LogUtil.insert("数据字典", COperation.CREATE, "", dictionary);

        return true;
    }


    /**
     * 修改
     * @param dictionary
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateById(Dictionary dictionary){

        LambdaQueryWrapper<Dictionary> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dictionary::getDicTypeId, dictionary.getDicTypeId());
        wrapper.eq(Dictionary::getValue, dictionary.getValue());
        wrapper.ne(Dictionary::getId, dictionary.getId());
        Dictionary one = getOne(wrapper);
        if(one != null){
            throw new MobaoException("该类型下此值已存在，请重新输入。");
        }

        Dictionary primaryBean = getById(dictionary.getId());
        if(!Constants.YES.equals(primaryBean.getIfEdit())){
            throw new MobaoException("此数据为数据库导入，不可变动。");
        }
        super.updateById(dictionary);
        DictionaryType dictionaryType = dictionaryTypeService.getById(dictionary.getDicTypeId());
        cleanDictionaryCache(dictionaryType.getCode());

        //日志记录
        LogUtil.insert("数据字典", COperation.UPDATE, primaryBean, dictionary);

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

        Dictionary primaryBean = getById(id);
        if(!Constants.YES.equals(primaryBean.getIfEdit())){
            throw new MobaoException("此数据为数据库导入，不可变动");
        }
        super.removeById(id);
        DictionaryType dictionaryType = dictionaryTypeService.getById(primaryBean.getDicTypeId());
        cleanDictionaryCache(dictionaryType.getCode());

        //日志记录
        LogUtil.insert("数据字典", COperation.UPDATE, "", primaryBean);

        return true;
    }
}
