package com.mobao360.audit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.mobao360.system.annotation.DictionaryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审核事件类型
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("AUDIT_EVENT_TYPE")
public class AuditEventType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 审核模块 （数据字典：audit_module）
     */
    @TableField("AUDIT_MODULE")
    @DictionaryCode("audit_module")
    private String auditModule;

    /**
     * 审核事件类型（数据字典：audit_event_type）
     */
    @TableField("AUDIT_EVENT_TYPE")
    @DictionaryCode("audit_event_type")
    private String auditEventType;

    /**
     * 用途
     */
    @TableField("APPLICATION")
    private String application;


}
