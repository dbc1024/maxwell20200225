package com.mobao360.system.utils.excel;

import lombok.Data;

import java.util.Collection;

/**
 * 单个Excel sheet需要参数
 * 主要是用于多个sheet导出时传错参数
 * @author: lijun
 * @date: 2018/11/20
 * @description:
 */
@Data
public class ExcelSheet{

    /**
     * 数据集合
     */
    private Collection<?> data;

    /**
     * 数据类型
     */
    private Class clazz;

    /**
     * 标题
     */
    private String title;

    /**
     * sheet名称
     */
    private String sheetName;


    public ExcelSheet(Collection<?> data,Class clazz){
        this.data = data;
        this.clazz = clazz;
    }

    public ExcelSheet(Collection<?> data,Class clazz,String title,String sheetName){
        this.data = data;
        this.clazz = clazz;
        this.title = title;
        this.sheetName = sheetName;
    }

}
