package com.mobao360.audit.service;

import com.mobao360.audit.entity.AuditEvent;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.audit.entity.AuditEventFlow;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-08
 */
public interface IAuditEventService extends IService<AuditEvent> {

    /**
     * 所有的审核操作（11-提交审核，12-通过，13-修改并通过，01-退回，02-拒绝）
     * 都可直接调用此方法完成审核流程。
     *
     * 入参：{审核事件ID}，{审核操作}，{明细描述}。
     *
     * 1.更新审核事件表
     * 2.生成一条审核事件明细
     * @param params
     * @return 是否已通过审核
     */
    boolean audit(Map<String, String> params);

    /**
     * 本系统所有审核调用此接口，其他系统使用OpenApi 102接口
     * @param params
     * @return
     */
    void auditMax(Map<String, String> params);

    /**
     *  商户中心合作商户资料审核
     *  入参：{客户号_customerNo}，{审核操作_operation}（通过-99，拒绝-98），{审核意见_auditAdvice}。
     * @param params
     * @return
     */
    void merchantCentreAudit(Map<String, String> params);

    /**
     * 根据审核事件ID删除审核事件
     *
     * @param eventId
     * @return
     */
    boolean removeByEventId(Long eventId);


    /**
     * 通过条件参数进行分页列表查询
     * @param params
     * @return
     */
    List<Map<String, String>> pageQuery(Map<String, String> params);


    /**
     * 新增
     * @param auditEvent
     * @return
     */
    boolean create(AuditEvent auditEvent);

    /**
     * 统计当前登录人待处理的事件数量
     * @return
     */
    int countDoing();


    /**
     * 判断审核流程是否配置
     * 判断发起人是否在流程中
     * @param flow
     */
    void checkFlowPermission(AuditEventFlow flow);

}
