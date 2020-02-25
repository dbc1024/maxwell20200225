package com.mobao360.audit.service;

import com.mobao360.audit.entity.AuditMerchantInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-03
 */
public interface IAuditMerchantInfoService extends IService<AuditMerchantInfo> {


    /**
     * 入网新增商户草稿信息
     * @param auditMerchantInfo
     * @return
     */
    boolean accessNetworkCreate(AuditMerchantInfo auditMerchantInfo);

    /**
     * 商户中心新增平台商户下的合作商户
     * @param auditMerchantInfo
     * @return
     */
    boolean merchantCentreCreate(AuditMerchantInfo auditMerchantInfo);

    /**
     * 商户中心修改平台商户下的合作商户
     * @param auditMerchantInfo
     * @return
     */
    boolean merchantCentreUpdate(AuditMerchantInfo auditMerchantInfo);

    /**
     * 入网审核通过后续处理
     * @param eventId
     */
    void accessNetworkPass(Long eventId);

    /**
     * 商户中心合作商户入网通过后续处理
     * @param customerNo
     */
    void merchantCentreAuditPass(String customerNo);

    /**
     * "变更修改"新增商户草稿信息
     * @param auditMerchantInfo
     * @return
     */
    boolean changeUpdate(AuditMerchantInfo auditMerchantInfo);


    /**
     * 变更修改通过后续处理
     * @param eventId
     */
    void changeUpdatePass(Long eventId);

    /**
     * 商户锁定
     * 传入参数：客户号customerNo，明细描述description
     * @param params
     */
    void lock(Map<String, String> params);

    /**
     * 审核通过后续处理
     * @param customerNo
     */
    void lockPass(String customerNo);

    /**
     * 商户解锁
     * 传入参数：客户号customerNo，明细描述description
     * @param params
     */
    void unlock(Map<String, String> params);

    /**
     * 审核通过后续处理
     * @param customerNo
     */
    void unlockPass(String customerNo);

    /**
     * 商户注销
     * 传入参数：客户号customerNo，明细描述description
     * @param params
     */
    void logout(Map<String, String> params);

    /**
     * 审核通过后续处理
     * @param customerNo
     */
    void logoutPass(String customerNo);

    /**
     * 新增商户信息
     * @param auditMerchantInfo
     * @return
     */
    @Override
    boolean save(AuditMerchantInfo auditMerchantInfo);


    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);


    /**
     * 修改
     * @param auditMerchantInfo
     * @return
     */
    boolean updateDraft(AuditMerchantInfo auditMerchantInfo);


    /**
     * 根据审核事件ID查询详情
     * @param eventId
     * @return
     */
    AuditMerchantInfo getByEventId(Long eventId);

    /**
     * 根据客户号查询详情
     * @param customerNo
     * @return
     */
    AuditMerchantInfo getByCustomerNo(String customerNo);

    /**
     * 根据客户号修改状态
     * @param customerNo
     * @param status
     * @return
     */
    boolean updateStatusByCustomerNo(String customerNo, String status);
}
