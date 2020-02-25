package com.mobao360.audit.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.mobao360.system.annotation.DictionaryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("AUDIT_EVENT_FLOW")
@KeySequence("SEQ_AUDIT_EVENT_FLOW")
public class AuditEventFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 审核事件类型（数据字典：audit_event_type）
     */
    @NotBlank(message = "auditEventType不能为空")
    @DictionaryCode("audit_event_type")
    @TableField("AUDIT_EVENT_TYPE")
    private String auditEventType;

    /**
     * 节点编码
     */
    @NotBlank(message = "节点编码nodeCode不能为空")
    @TableField("NODE_CODE")
    private String nodeCode;

    /**
     * 节点名称
     */
    @TableField("NODE_NAME")
    private String nodeName;

    /**
     * 操作角色
     */
    @NotBlank(message = "操作角色operatorRole不能为空")
    @TableField("OPERATOR_ROLE")
    private String operatorRole;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称roleName不能为空")
    @TableField("ROLE_NAME")
    private String roleName;

    /**
     * 操作权限
     */
    @TableField("OPERATOR_PERMISSION")
    private String operatorPermission;

    /**
     * 按钮权限
     */
    @TableField("BUTTON_PERMISSION")
    private String buttonPermission;

    /**
     * 是否可修改 1-是
     */
    @NotBlank(message = "是否可修改ifEdit不能为空")
    @TableField("IF_EDIT")
    private String ifEdit;


}
