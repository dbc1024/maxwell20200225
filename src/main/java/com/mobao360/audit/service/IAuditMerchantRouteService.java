package com.mobao360.audit.service;

import com.mobao360.audit.entity.AuditMerchantRoute;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 商户消费路由 service接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-15
 */
public interface IAuditMerchantRouteService extends IService<AuditMerchantRoute> {

    /**
     * 新增
     * @param auditMerchantRoute
     * @return
     */
    @Override
    boolean save(AuditMerchantRoute auditMerchantRoute);
	
	/**
     * 修改
     * @param auditMerchantRoute
     * @return
     */
    boolean updateDraft(AuditMerchantRoute auditMerchantRoute);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);


    /**
     * 路由变更修改事件
     * （需生成审核事件）
     * @param route
     * @return
     */
    boolean changeUpdate(AuditMerchantRoute route);

    /**
     * 路由新增事件
     * （需生成审核事件）
     * @param route
     * @return
     */
    boolean changeCreate(AuditMerchantRoute route);

    /**
     * 审核通过后续处理
     * @param eventId
     */
    void changePass(Long eventId);

    /**
     * 根据审核事件id获取实体详情
     * @param eventId
     * @return
     */
    AuditMerchantRoute getByEventId(Long eventId);

}