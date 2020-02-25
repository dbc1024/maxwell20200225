package com.mobao360.audit.mapper;

import com.mobao360.audit.entity.AuditEventFlow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-10
 */
@Mapper
public interface AuditEventFlowMapper extends BaseMapper<AuditEventFlow> {

    /**
     * 通过“审核事件类型”，“节点编码”获取对应流程节点下一节点信息
     * @param auditEventType
     * @param nodeCode
     * @return
     */
    AuditEventFlow getNextNodeInfoByTypeAndNodeCode(@Param("auditEventType")String auditEventType, @Param("nodeCode")String nodeCode);

    /**
     * 通过“审核事件类型”，“节点编码”获取对应流程节点上一节点信息
     * @param auditEventType
     * @param nodeCode
     * @return
     */
    AuditEventFlow getLastNodeInfoByTypeAndNodeCode(@Param("auditEventType")String auditEventType, @Param("nodeCode")String nodeCode);

    /**
     * 通过“审核事件类型”获取对应流程的最大节点编码
     * @param auditEventType
     * @return
     */
    String getMaxNodeCodeByType(@Param("auditEventType")String auditEventType);

}
