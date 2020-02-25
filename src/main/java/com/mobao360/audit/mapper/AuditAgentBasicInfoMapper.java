package com.mobao360.audit.mapper;

import com.mobao360.audit.entity.AuditAgentBasicInfo;
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
 * @since 2019-01-15
 */
@Mapper
public interface AuditAgentBasicInfoMapper extends BaseMapper<AuditAgentBasicInfo> {
    /**
     * 获取最大商户号
     * @return
     */
    String getMaxAgentNo();

    /**
     * 根据重复要素查询
     * @param auditAgentBasicInfo
     * @return
     */
    List<AuditAgentBasicInfo> checkInfoRepeat(AuditAgentBasicInfo auditAgentBasicInfo);

    /**
     * 统计变更修改审核事件数量
     * @param agentNo 代理商号
     * @return 事件数
     */
    int selectByAgentNoAndAuditEventType(@Param("AgentNo") String agentNo);

}
