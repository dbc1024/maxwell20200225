package com.mobao360.audit.mapper;

import com.mobao360.audit.entity.AuditMerchantConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-11
 */
@Mapper
public interface AuditMerchantConfigMapper extends BaseMapper<AuditMerchantConfig> {

    /**
     * 根据商户号和商户审核类型 查询商户配置审核事件记录数
     * @param customerNo 商户号
     * @return 记录条数
     */
    int countByCustomerNoAndCAuditCAuditEventType(@Param("customerNo") String customerNo);

}
