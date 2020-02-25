package com.mobao360.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.system.vo.CertDueExcel;
import com.mobao360.system.vo.NoTradeExcel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
public interface IMerchantInfoService extends IService<MerchantInfo> {

    /**
     * 查询所有
     * @return
     */
    List<Map<String, String>> queryAll();
    /**
     * 通过条件参数进行分页列表查询
     * @param params
     * @return
     */
    List<Map<String, String>> pageQuery(Map<String, String> params);

    /**
     * 证件到期预警分页查询
     * @param params
     * @return
     */
    List<Map<String, Object>> certDuePage(Map<String, String> params);

    /**
     * 证件到期预警excel下载
     * @param params
     */
    List<CertDueExcel> certDueExcelDown(Map<String, String> params);


    /**
     * 证件到期预警excel下载
     * @param params
     */
    List<NoTradeExcel> noTradeExcelDown(Map<String, String> params);


    /**
     * 新增商户信息
     * @param merchantInfo
     * @return
     */
    @Override
    boolean save(MerchantInfo merchantInfo);


    /**
     * 通过客户号查询商户信息
     * @param customerNo
     * @return
     */
    Map<String, String> detailByCustomerNo(String customerNo);


    /**
     * 通过客户号查询商户信息
     * @param customerNo
     * @return
     */
    MerchantInfo getByCustomerNo(String customerNo);


    /**
     * 查找应该向交易服务推送的数据
     * @return
     */
    MerchantInfo findTradePushedData();


    /**
     * 查找应该向风控服务推送的数据
     * @return
     */
    MerchantInfo findRiskPushedData();


    /**
     * 按日,周,月统计商户开户数
     * @param period
     * @return
     */
    Map<String,Integer> countOpenQuantity(String period);


    /**
     * 向交易系统推送老数据
     * @param ifAll
     */
    void pushOldData(String ifAll);

    List<MerchantInfo> checkDueCert(String today);

    /**
     * 通过客户号查询商户信息
     * @param customerNo
     * @return
     */
    List<String> getSubByCustomerNo(String customerNo);
}
