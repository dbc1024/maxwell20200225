package com.mobao360.audit.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.audit.entity.AuditEventDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-10
 */
public interface IAuditEventDetailService extends IService<AuditEventDetail> {

    /**
     * 根据审核事件ID删除事件明细
     *
     * @param eventId
     * @return
     */
    boolean removeByEventId(Long eventId);


    /**
     * 审核明细分页查询
     * @param page
     * @param params
     * @return
     */
    IPage auditDetailPage(IPage page, Map<String, String> params);


    /**
     * 日志明细分页查询
     * @param page
     * @param params
     * @return
     */
    IPage logDetailPage(IPage page, Map<String, String> params);

}
