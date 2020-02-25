package com.mobao360.audit.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.mobao360.system.annotation.DictionaryCode;
import com.mobao360.system.utils.valid.ValidGroup1;
import com.mobao360.system.utils.valid.ValidGroup2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * <p>
 * 商户消费路由
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("AUDIT_MERCHANT_ROUTE")
@KeySequence("SEQ_AUDIT_MERCHANT_ROUTE")
public class AuditMerchantRoute implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 审核事件ID
     */
    @NotNull(message = "auditEventId不能为空",groups = ValidGroup1.class)
    @TableField("AUDIT_EVENT_ID")
    private Long auditEventId;

    /**
     * 对应已审核通过正式数据的ID
     */
    @NotNull(message = "formalId不能为空",groups = ValidGroup2.class)
    @TableField("FORMAL_ID")
    private Long formalId;

    /**
     * 客户号
     */
    @NotBlank(message = "customerNo不能为空")
    @TableField("CUSTOMER_NO")
    private String customerNo;

    /**
     * 客户全称（冗余设计）
     */
    @TableField("CUSTOMER_NAME")
    @NotBlank(message = "[客户全称 customerName]不能为空")
    private String customerName;

    /**
     * 交易类型（1：消费 2：出款 ）
     */
    @DictionaryCode("trade_type")
    @TableField("TRADE_TYPE")
    @Length(min = 1,max = 1,message = "交易类型 tradeType 参数长度只能为1")
    private String tradeType;

    /**
     * 支付方式（多选，逗号分开）
     */
    @TableField("PAY_TYPES")
    private String payTypes;

    /**
     * 支付方式（中文名称，以逗号隔开）
     */
    @TableField("PAY_TYPES_DIC")
    private String payTypesDic;

    /**
     * 支持银行（多选，以逗号分隔银行编码存储）
     */
    @TableField("BANK_CODES")
    private String bankCodes;

    /**
     * 支持银行（逗号分隔，以中文名称存储）
     */
    @TableField("BANK_CODES_DIC")
    private String bankCodesDic;

    /**
     * 金额区间（最小金额）
     */
    @TableField("MIN_AMOUNT")
    @NotBlank(message = "[金额区间（最小金额） minAmount]不能为空")
    private String minAmount;

    /**
     * 金额区间（最大金额）
     */
    @TableField("MAX_AMOUNT")
    @NotBlank(message = "金额区间（最大金额） maxAmount]不能为空")
    private String maxAmount;

    /**
     * 通道编码
     */
    @TableField("CHANNEL_CODE")
    @DictionaryCode("channel_code")
    @NotBlank(message = "通道编码 channelCode]不能为空")
    private String channelCode;

    /**
     * 通道商户号
     */
    @TableField("CHANNEL_MERCHANT_NO")
    @NotBlank(message = "通道商户号 channelMerchantNo]不能为空")
    private String channelMerchantNo;

    /**
     * 通道商户名称（冗余设计）
     */
    @TableField("CHANNEL_MERCHANT_NAME")
    @NotBlank(message = "通道商户名称 channelMerchantName]不能为空")
    private String channelMerchantName;

    /**
     * 修改记录
     */
    @TableField("UPDATE_RECORD")
    private String updateRecord;

    /**
     * 规则描述
     */
    @TableField("RULE")
    private String rule;

    /** 优先级*/
    @NotBlank(message =  "优先级不能为空")
    @TableField("PRIORITY")
    private String priority;

    /**
     * 是否有效（0：无效 1：有效）
     */
    @TableField("IF_EFFCTIVE")
    private String ifEffctive;

    @TableField("CREATE_TIME")
    private String createTime;

    @TableField("UPDATE_TIME")
    private String updateTime;


}
