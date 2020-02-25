package com.mobao360.base.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/3/18 15:50
 */
@Data
@Builder
public class LogEntity {

    /** 系统名称*/
    private String system;
    /** 模块名称*/
    private String module;
    /** 操作者*/
    private String operator;
    /** 操作类型*/
    private String operation;
    /** 操作者IP*/
    private String operatorIp;
    /** 操作时间*/
    private Date operatorTime;
    /** 操作前的数据*/
    private String beforeInfo;
    /** 变化后的数据*/
    private String afterInfo;


}
