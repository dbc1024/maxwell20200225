package com.mobao360.system.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 证件到期预警excel下载对象
 * @author: CSZ 991587100@qq.com
 * @date: 2019/3/29 20:16
 */
@Data
public class CertDueExcel {

    @Excel(name = "商户号", mergeVertical = true, width=15)
    private String customerNo;

    @Excel(name = "商户名称", mergeVertical = true, mergeRely={0}, width=20)
    private String customerName;

    @Excel(name = "销售人员", mergeVertical = true, mergeRely={0})
    private String salesmanName;

    @Excel(name = "交易状态", replace={"关闭_0", "开启_1"}, mergeVertical=true, mergeRely={0})
    private String tradeStatus;

    @Excel(name = "结算状态", replace={"关闭_0", "开启_1"}, mergeVertical=true, mergeRely={0})
    private String settStatus;

    @Excel(name = "证件类型")
    private String certType;

    @Excel(name = "证件状态")
    private String certStatus;

    @Excel(name = "到期时间")
    private String certDueDate;

}
