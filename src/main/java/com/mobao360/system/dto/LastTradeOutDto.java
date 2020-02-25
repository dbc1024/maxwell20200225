package com.mobao360.system.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 接收商户最后交易日期，最后出款日期
 * @author: CSZ 991587100@qq.com
 * @date: 2019/3/28 09:47
 */
@Data
public class LastTradeOutDto implements Serializable {

    /** 最后下单日期（最后交易日期，最后出款日期。每天定时推送到本系统） */
    private String lastDate;

    /** 此日期发生交易的商户号列表 */
    private List<String> tradeCustomerNoList;

    /** 此日期发生出款的商户号列表 */
    private List<String> outCustomerNoList;


}
