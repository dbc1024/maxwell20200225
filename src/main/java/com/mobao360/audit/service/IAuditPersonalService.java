package com.mobao360.audit.service;

import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/4/9 11:04
 */
public interface IAuditPersonalService {

    /**
     * 个人锁定
     * @param params customerNo：客户号，description：申请意见
     */
    void lock(Map<String,String> params);

    /**
     * 个人解锁
     * @param params customerNo：客户号，description：申请意见
     */
    void unlock(Map<String, String> params);

    /**
     * 个人注销
     * @param params customerNo：客户号，description：申请意见
     */
    void logout(Map<String, String> params);

    /**
     * 锁定,解锁,注销审核通过后续操作
     * @param customerNo
     * @param status
     */
    void updatePersonalStatus(String customerNo, String status);
}
