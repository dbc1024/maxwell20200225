package com.mobao360.audit.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.mobao360.system.annotation.DictionaryCode;
import com.mobao360.system.utils.valid.ValidGroup1;
import com.mobao360.system.utils.valid.ValidGroup2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("AUDIT_MERCHANT_FEE_RATE")
@KeySequence("SEQ_AUDIT_MERCHANT_FEE_RATE")
public class AuditMerchantFeeRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("AUDIT_EVENT_ID")
    @NotNull(message = "auditEventId不能为空", groups = {ValidGroup1.class})
    private Long auditEventId;

    /** 对应已审核通过正式数据的ID */
    @NotNull(message = "formalId不能为空", groups = {ValidGroup2.class})
    @TableField("FORMAL_ID")
    private Long formalId;

    /**
     * 客户号
     */
    @TableField("CUSTOMER_NO")
    @NotNull(message = "商户号 customerNo 不能为空")
    private String customerNo;

    /**
     * 产品类型 （数据字典：product_kind）
     */
    @TableField("PRODUCT_KIND")
    @NotNull(message = "产品类型 productKind 不能为空")
    @DictionaryCode("product_kind")
    private String productKind;

    /**
     * 支付方式 （数据字典：pay_type）
     */
    @TableField("PAY_TYPE")
    @DictionaryCode("pay_type")
    @NotNull(message = "支付方式 payType 不能为空")
    private String payType;

    /**
     * 银行编码
     */
    @TableField("BANK_CODE")
    private String bankCode;

    /**
     * 计费方式 （数据字典：fee_type 1-固定值 2-按比例）
     */
    @TableField("FEE_TYPE")
    @DictionaryCode("fee_type")
    @Length(min = 1,max = 1,message = "参数值格式错误")
    private String feeType;

    /**
     * 费率 （计费方式为“按比例”时赋值）
     */
    @TableField("FEE_RATE")
    private String feeRate;

    /**
     * 固定金额 （计费方式为“固定值”时赋值）
     */
    @TableField("FIXED_AMOUNT")
    private String fixedAmount;

    /**
     * 保底
     */
    @TableField("MIN_FEE")
    private String minFee;

    /**
     * 封顶
     */
    @TableField("MAX_FEE")
    private String maxFee;

    /**
     * 生效时间
     */
    @TableField("START_TIME")
    @Length(min = 19,max = 19,message = "生效时间startTime格式为[2019-11-04 00:00:00]")
    private String startTime;

    /**
     * 失效时间
     */
    @TableField("END_TIME")
    private String endTime;

    /**
     * 产品预留，暂未使用
     */
    @TableField("IF_EFFECTIVE")
    private String ifEffective;

    @TableField("CREATE_TIME")
    private String createTime;

    @TableField("UPDATE_TIME")
    private String updateTime;

}
