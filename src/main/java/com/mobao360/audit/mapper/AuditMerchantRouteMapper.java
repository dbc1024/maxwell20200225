package com.mobao360.audit.mapper;

import com.mobao360.audit.entity.AuditMerchantRoute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商户消费路由 Mapper 接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-15
 */
@Mapper
public interface AuditMerchantRouteMapper extends BaseMapper<AuditMerchantRoute> {

    /**
     *
     * @param customerNo 商户号
     * @return 审核事件数
     */
    int countUpdateByCustomerAndAuditEventType(@Param("customerNo") String customerNo);

}
