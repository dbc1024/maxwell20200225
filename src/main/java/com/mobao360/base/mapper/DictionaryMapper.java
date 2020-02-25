package com.mobao360.base.mapper;

import com.mobao360.base.entity.Dictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@Mapper
public interface DictionaryMapper extends BaseMapper<Dictionary> {

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
}
