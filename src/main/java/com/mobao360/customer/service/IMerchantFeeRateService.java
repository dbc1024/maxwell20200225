package com.mobao360.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.customer.entity.MerchantFeeRate;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
public interface IMerchantFeeRateService extends IService<MerchantFeeRate> {


    /**
     * 根据客户号，支付方式查询当前可用的费率
     * @param customerNo
     * @param payType
     * @return
     */
    MerchantFeeRate getByCustomerNoAndPayType(String customerNo, String payType);


    /**
     * 根据商户号查询未过期费率
     * @param customerNo
     * @return
     */
    List<MerchantFeeRate> activityMerchantFate(String customerNo);
}
