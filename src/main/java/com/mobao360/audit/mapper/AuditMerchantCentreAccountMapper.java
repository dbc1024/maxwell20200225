package com.mobao360.audit.mapper;

import com.mobao360.audit.entity.AuditMerchantCentreAccount;
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
 * @since 2018-12-28
 */
@Mapper
public interface AuditMerchantCentreAccountMapper extends BaseMapper<AuditMerchantCentreAccount> {
    /**
     * 去重检验
     * @param auditMerchantCentreAccount
     * @return
     */
    List<AuditMerchantCentreAccount> checkInfoRepeat(AuditMerchantCentreAccount auditMerchantCentreAccount);

    /**
     * 统计变更修改审核事件数
     * @param customerNo 客户号
     * @return 事件数
     */
    int countUpdateByCustomerAndAuditEventType(@Param("customerNo") String customerNo);
}
