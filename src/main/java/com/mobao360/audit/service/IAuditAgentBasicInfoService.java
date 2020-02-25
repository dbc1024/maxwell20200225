package com.mobao360.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.audit.entity.AuditAgentBasicInfo;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *  service接口
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-15
 */
public interface IAuditAgentBasicInfoService extends IService<AuditAgentBasicInfo> {

    /**
     * 新增
     * @param auditAgentBasicInfo
     * @return
     */
    @Override
    boolean save(AuditAgentBasicInfo auditAgentBasicInfo);
	
	/**
     * 代理资料修改
     * @param auditAgentBasicInfo
     * @return
     */
    boolean changeUpdate(AuditAgentBasicInfo auditAgentBasicInfo);

    /**
     * 审核代理草稿资料修改
     * @param auditAgentBasicInfo
     * @return
     */
    boolean updateDraft(AuditAgentBasicInfo auditAgentBasicInfo);

    /**
     * 代理资料新增
     * @param auditAgentBasicInfo
     * @return
     */
    boolean changeCreate(AuditAgentBasicInfo auditAgentBasicInfo);

    /**
     * 审核通过后续处理
     * @param eventId
     */
    void changePass(Long eventId);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     *  获取审核实例
     * @param auditEventId
     * @return
     */
    AuditAgentBasicInfo getByAuditEventId(Long auditEventId);

}