package com.mobao360.audit.mapper;

import com.mobao360.audit.entity.AuditMerchantFundSettlement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-17
 */
@Mapper
public interface AuditMerchantFundSettlementMapper extends BaseMapper<AuditMerchantFundSettlement> {

    /**
     *
     * @param customerNo 商户号
     * @return 正在审核事件数
     */
    int countUpdateByCustomerAndAuditEventType(@Param("customerNo") String customerNo);

}
