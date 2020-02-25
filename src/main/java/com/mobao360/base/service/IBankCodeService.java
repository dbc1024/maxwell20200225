package com.mobao360.base.service;

import com.mobao360.base.entity.BankCode;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 银行编码表 service接口
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-03
 */
public interface IBankCodeService extends IService<BankCode> {

    /**
     * 新增
     * @param bankCode
     * @return
     */
    @Override
    boolean save(BankCode bankCode);
	
	/**
     * 修改
     * @param bankCode
     * @return
     */
    @Override
    boolean updateById(BankCode bankCode);
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 通过分支行行号查询对应总行的银行代码
     * @param branchBankNo
     * @return
     */
    BankCode getCodeByBranchBankNo(String branchBankNo);

    /**
     * 通过银行编码查询银行配置相关信息
     * @param bankCode
     * @return
     */
    BankCode getByBankCode(String bankCode);

    /**
     * 获取银行编码和名称键值对
     * @return
     */
    Map<String, String> getKeyValue();


}