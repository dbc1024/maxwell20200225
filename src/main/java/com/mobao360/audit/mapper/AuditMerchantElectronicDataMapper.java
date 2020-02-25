package com.mobao360.audit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mobao360.audit.entity.AuditMerchantElectronicData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-03
 */
@Mapper
public interface AuditMerchantElectronicDataMapper extends BaseMapper<AuditMerchantElectronicData> {

    /**
     * 统计变更修改审核事件数
     * @param customerNo 客户号
     * @return 事件数
     */
    int countUpdateByCustomerAndAuditEventType(@Param("customerNo") String customerNo);
}
