package com.mobao360.base.mapper;

import com.mobao360.base.entity.Other;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-18
 */
@Mapper
public interface OtherMapper extends BaseMapper<Other> {

    /**
     * 行业分页查询
     * @param params
     * @return
     */
    List<Map<String, String>> industryList(Map<String, String> params);

    /**
     * MCC分页查询
     * @param params
     * @return
     */
    List<Map<String, String>> mccPageQuery(Map<String, String> params);

    /**
     * 微信经营类目分页查询
     * @param params
     * @return
     */
    List<Map<String, String>> wxPageQuery(Map<String, String> params);

    /**
     * 网联业务种类列表查询
     * @return
     */
    List<Map<String, String>> nuccList();


    /**
     * 总行联行列表查询
     * @param bankName
     * @return
     */
    List<Map<String, String>> headUnionBankList(@Param("bankName")String bankName);

    /**
     * 开户行下拉列表查询
     * @param bankName
     * @return
     */
    List<Map<String, String>> openingBankList(@Param("bankName")String bankName);

    /**
     * 省下拉列表查询
     * @return
     */
    List<Map<String, String>> provinceList();

    /**
     * 市下拉列表查询
     * @param provinceCode
     * @return
     */
    List<Map<String, String>> cityList(@Param("provinceCode")String provinceCode);

    /**
     * 根据市代码查询区县代码集合
     * @param cityCode
     * @return
     */
    List<String> districtCodeList(@Param("cityCode")String cityCode);


    /**
     * 分支行下拉列表查询
     * @param params
     * @return
     */
    List<Map<String, String>> branchBankList(Map<String, Object> params);


    /**
     * 通过分支行行号查询对应的总联行号
     * @param branchBankNo
     * @return
     */
    String getHeadByBranch(@Param("branchBankNo")String branchBankNo);


    /**
     * 老数据账户余额查询
     * @param customerNo
     * @return
     */
    List<Map<String, String>> accountBalanceList(@Param("customerNo")String customerNo);

}
