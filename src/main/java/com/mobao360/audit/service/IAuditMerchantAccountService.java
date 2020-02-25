package com.mobao360.audit.service;

import java.util.Map;

/**
 * @author Miskai
 * @date 2019/3/7
 */
public interface IAuditMerchantAccountService {
    /**
     * 商户账户锁定
     * @param params merchantAccount：账户号，description：申请意见
     */
    void lock(Map<String,String> params);

    /**
     * 商户账户解锁
     * @param params merchantAccount：账户号，description：申请意见
     */
    void unlock(Map<String, String> params);

    /**
     * 商户账户注销
     * @param params merchantAccount：账户号，description：申请意见
     */
    void logout(Map<String, String> params);

    /**
     * 锁定,解锁,注销审核通过后续操作
     * @param accountNo
     * @param status
     */
    void updateAccountStatus(String accountNo, String status);

}
