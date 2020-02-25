package com.mobao360.audit.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.mobao360.system.annotation.DictionaryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 审核事件
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("AUDIT_EVENT")
@KeySequence("SEQ_AUDIT_EVENT")
public class AuditEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 审核模块 （数据字典：audit_module）
     */
    @NotBlank(message = "auditModule不能为空")
    @TableField("AUDIT_MODULE")
    @DictionaryCode("audit_module")
    private String auditModule;

    /**
     * 审核事件类型（数据字典：audit_event_type）
     */
    @NotBlank(message = "auditEventType不能为空")
    @TableField("AUDIT_EVENT_TYPE")
    @DictionaryCode("audit_event_type")
    private String auditEventType;

    /**
     * 审核主体
     */
    @NotBlank(message = "subject不能为空")
    @TableField("SUBJECT")
    private String subject;

    /**
     * 审核主体编码(审核主体在业务关系中的唯一标识，如：客户号、......)
     */
    @TableField("SUBJECT_CODE")
    private String subjectCode;

    /**
     * 节点编码
     */
    @TableField("NODE_CODE")
    private String nodeCode;

    /**
     * 事件状态
     */
    @TableField("STATUS")
    @DictionaryCode("audit_event_status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private String updateTime;


    /** 非数据库字段(用于其他服务新增审核事件时，传入审核操作)*/
    @NotBlank(message = "operation不能为空")
    @TableField(exist = false)
    private String operation;


    /** 非数据库字段(用于其他服务新增审核事件时，传入明细描述)*/
    @NotBlank(message = "description不能为空")
    @TableField(exist = false)
    private String description;


}
