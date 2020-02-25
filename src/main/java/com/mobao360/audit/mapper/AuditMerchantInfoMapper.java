package com.mobao360.audit.mapper;

import com.mobao360.audit.entity.AuditMerchantInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-03
 */
@Mapper
public interface AuditMerchantInfoMapper extends BaseMapper<AuditMerchantInfo> {

    /**
     * 获取最大商户号
     * @return
     */
    String getMaxCustomerNo ();


    /**
     * 重复要素校验
     * @param auditMerchantInfo
     * @return
     */
    List<AuditMerchantInfo> checkInfoRepeat (AuditMerchantInfo auditMerchantInfo);

    /**
     * 根据商户号和商户审核类型 查询商户资料审核事件记录数
     * @param customerNo 商户号
     * @param auditEventTypeList 审核事件类型
     * @return 记录条数
     */
    int countByCustomerNoAndCAuditCAuditEventType(@Param("customerNo") String customerNo, @Param("list")List auditEventTypeList);


}
