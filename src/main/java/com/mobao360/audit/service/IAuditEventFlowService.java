package com.mobao360.audit.service;

import com.mobao360.audit.entity.AuditEventFlow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-10
 */
public interface IAuditEventFlowService extends IService<AuditEventFlow> {

    /**
     * 通过“审核事件类型”，“节点编码”获取对应流程节点信息
     * @param auditEventType
     * @param nodeCode
     * @return
     */
    AuditEventFlow getNodeInfoByTypeAndNodeCode(String auditEventType, String nodeCode);

    /**
     * 通过“审核事件类型”，“节点编码”获取对应流程节点下一节点信息
     * @param auditEventType
     * @param nodeCode
     * @return
     */
    AuditEventFlow getNextNodeInfoByTypeAndNodeCode(String auditEventType, String nodeCode);

    /**
     * 通过“审核事件类型”，“节点编码”获取对应流程节点上一节点信息
     * @param auditEventType
     * @param nodeCode
     * @return
     */
    AuditEventFlow getLastNodeInfoByTypeAndNodeCode(String auditEventType, String nodeCode);

    /**
     * 通过“审核事件类型”获取对应流程的最大节点编码
     * @param auditEventType
     * @return
     */
    String getMaxNodeCodeByType(String auditEventType);

    /**
     * 新增
     * @param flowList
     * @return
     */
    boolean save(List<AuditEventFlow> flowList);

    /**
     * 修改
     * @param flowList
     * @return
     */
    boolean update(List<AuditEventFlow> flowList);

}
