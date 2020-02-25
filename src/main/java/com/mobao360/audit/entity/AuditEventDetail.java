package com.mobao360.audit.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 审核事件明细
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("AUDIT_EVENT_DETAIL")
@KeySequence("SEQ_AUDIT_EVENT_DETAIL")
public class AuditEventDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 审核事件ID
     */
    @TableField("AUDIT_EVENT_ID")
    private Long auditEventId;

    /**
     * 操作
     */
    @TableField("OPERATION")
    private String operation;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 节点名称
     */
    @TableField("NODE_NAME")
    private String nodeName;

    /**
     * 操作人员
     */
    @TableField("OPERATOR")
    private String operator;

    /**
     * 修改记录
     */
    @TableField("UPDATE_RECORD")
    private String updateRecord;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private String createTime;


    /**
     * 节点编码
     */
    @TableField("NODE_CODE")
    private String nodeCode;

    /**
     * 是否为日志
     * 1-是，0-不是
     * (如在“提交审核”前，会有新增和多次修改，都会形成一条记录存于此表中。这些是每一步的日志记录，并非审核记录。)
     */
    @TableField("IF_LOG")
    private String ifLog;


    /** 非数据库字段
     * (本系统未存操作人名称，每次向前端展示时，去用户系统拉取对应的名称)*/
    @TableField(exist = false)
    private String operatorName;


}
