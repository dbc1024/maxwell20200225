package com.mobao360.audit.mapper;

import com.mobao360.audit.entity.AuditMerchantFeeRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-18
 */
@Mapper
public interface AuditMerchantFeeRateMapper extends BaseMapper<AuditMerchantFeeRate> {

    /**
     * 统计变更修改审核事件数量
     * @param customerNo 代理商号
     * @return 事件数
     */
    int countUpdateByCustomerAndAuditEventType(@Param("customerNo") String customerNo, @Param("formalId") Long formalId);

}
