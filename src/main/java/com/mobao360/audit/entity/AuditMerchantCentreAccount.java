package com.mobao360.audit.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.mobao360.system.utils.valid.ValidGroup1;
import com.mobao360.system.utils.valid.ValidGroup2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2018-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("AUDIT_MERCHANT_CENTRE_ACCOUNT")
@KeySequence("SEQ_A_M_C_A")
public class AuditMerchantCentreAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("AUDIT_EVENT_ID")
    @NotNull(message = "审核事件ID auditEventId不能为空",groups = {ValidGroup2.class})
    private Long auditEventId;

    @TableField("CUSTOMER_NO")
    @NotNull(message = "商户号 customerNo不能为空")
    private String customerNo;

    /**
     * 商户管理员邮箱
     */
    @NotNull(message = "商户管理员邮箱managerEmail不能为空")
    @TableField("MANAGER_EMAIL")
    private String managerEmail;

    /**
     * 商户管理员绑定手机
     */
    @Length(min = 11, max = 11, message = "商户管理员绑定手机managerTel   号只能为11位")
    @TableField("MANAGER_TEL")
    private String managerTel;

    @TableField("CREATE_TIME")
    private String createTime;

    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 修改记录
     */
    @TableField("UPDATE_RECORD")
    private String updateRecord;

}
