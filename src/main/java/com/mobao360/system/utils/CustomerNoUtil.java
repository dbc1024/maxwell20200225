package com.mobao360.system.utils;

import com.mobao360.system.exception.MobaoException;

/**
 * 客户号生成工具类
 * @author: CSZ 991587100@qq.com
 * @date: 2018/12/12 19:49
 */
public class CustomerNoUtil {

    /**
     * 客户号生成规则：个人/企业客户分类（1位）+机构号（3位）+顺序号（7位）+校验码（1位）=12位
     * (个人/企业客户分类：1-企业 5-个人)
     * 如：199900000019
     *
     * @param customerNo
     * @return
     */
    public static String generateCustomerNo(String customerNo){
        //获取中间7位顺序号
        String orderNo = customerNo.substring(4, 11);
        if("9999999".equals(orderNo)){
            throw new MobaoException("1开头的客户号已用完");
        }

        //生成新的顺序号
        Long orderNo8 = Long.valueOf("1" + orderNo) + 1;
        String newOrderNo = (orderNo8.toString()).substring(1);

        //获取校验码，生成新的客户号
        customerNo = customerNo.substring(0, 4) + newOrderNo;
        customerNo = customerNo + Luhn.getCheckNumber(customerNo);

        return customerNo;
    }

}
