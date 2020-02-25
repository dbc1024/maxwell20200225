package com.mobao360.customer.mapper;

import com.mobao360.customer.entity.MerchantInfo;
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
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@Mapper
public interface MerchantInfoMapper extends BaseMapper<MerchantInfo> {

    /**
     * 获取最大商户号
     * @return
     */
    Long getMaxCustomerNo ();

    /**
     * 查询所有
     * @return
     */
    List<Map<String, String>> queryAll();

    /**
     * 通过条件参数进行分页列表查询
     * @param params
     * @return
     */
    List<Map<String, String>> pageQuery(Map<String, String> params);

    /**
     * 通过customerNo查询实体详情
     * @param customerNo
     * @return
     */
    Map<String, String> detail(String customerNo);

    /**
     * 证件到期预警分页查询
     * @param params
     * @return
     */
    List<Map<String, Object>> certDuePage(Map<String, String> params);


    /**
     * 获取存在证件失效商户的商户号
     * @param today
     * @return
     */
    List<String> checkDueCert(@Param("today")String today);

    /**
     * 证件到期锁定商户
     * @param customerNoList
     * @return
     */
    void certDueLock(@Param("customerNoList")List<String> customerNoList);

}
