package com.mobao360.system.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 无交易分析报表excel下载对象
 * @author: CSZ 991587100@qq.com
 * @date: 2019/3/29 20:16
 */
@Data
public class NoTradeExcel {

    @Excel(name = "商户号", width=15)
    private String customerNo;

    @Excel(name = "商户名称", width=20)
    private String customerName;

    @Excel(name = "开户日期")
    private String createTime;

    @Excel(name = "商户类型")
    private String merchantType;

    @Excel(name = "所属销售")
    private String salesmanName;

    @Excel(name = "业务人员")
    private String staff;

    @Excel(name = "商户状态")
    private String status;

    @Excel(name = "交易状态", replace={"关闭_0", "开启_1"})
    private String tradeStatus;

    @Excel(name = "结算状态", replace={"关闭_0", "开启_1"})
    private String settStatus;

    @Excel(name = "最后交易日期")
    private String lastTradeDate;

    @Excel(name = "累计无交易天数")
    private Long noTradeDays;

}
