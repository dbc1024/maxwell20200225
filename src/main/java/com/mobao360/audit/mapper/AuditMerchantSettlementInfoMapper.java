package com.mobao360.audit.mapper;

import com.mobao360.audit.entity.AuditMerchantSettlementInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商户结算信息 Mapper 接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-28
 */
@Mapper
public interface AuditMerchantSettlementInfoMapper extends BaseMapper<AuditMerchantSettlementInfo> {

    /**
     *
     * @param customerNo 商户号
     * @return 审核事件数
     */
    int countUpdateByCustomerAndAuditEventType(@Param("customerNo") String customerNo);

}
