package com.mobao360.base.service;

import com.mobao360.base.entity.Dictionary;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
public interface IDictionaryService extends IService<Dictionary> {

    /**
     * 根据数据字典类型code获取对应所有数据字典键值对
     * @param typeCode
     * @return
     */
    List<Map<String, String>> getKeyValueListByCode(String typeCode);

    /**
     * 通过条件参数进行分页列表查询
     * @param params
     * @return
     */
    List<Map<String, String>> pageQuery(Map<String, String> params);

    /**
     * 数据字典增、删、改的方法中应该调用此方法,
     * 传入对应数据字典类型 typeCode
     * 清除对应 typeCode 的缓存数据
     * typeCode="0"时，清除所有缓存
     * @param typeCode
     */
    void cleanDictionaryCache(String typeCode);


    /**
     * 新增
     * @param dictionary
     * @return
     */
    @Override
    boolean save(Dictionary dictionary);

    /**
     * 修改
     * @param dictionary
     * @return
     */
    @Override
    boolean updateById(Dictionary dictionary);

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

}
