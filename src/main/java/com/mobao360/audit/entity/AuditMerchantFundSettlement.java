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

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("AUDIT_MERCHANT_FUND_SETTLEMENT")
@KeySequence("SEQ_A_MERCHANT_FUND_SETTLEMENT")
public class AuditMerchantFundSettlement implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

        /**
     * 客户号
     */
         @NotNull(message = "customerNo不能为空" )
         @TableField("CUSTOMER_NO")
    private String customerNo;

    /**
     * 商户全称（冗余设计）
     */
    @TableField("CUSTOMER_NAME")
    private String customerName;

    /**
     * 对应已审核通过正式数据的ID
     */
    @NotNull(message = "formalId不能为空", groups = {ValidGroup2.class})
    @TableField("FORMAL_ID")
    private Long formalId;

        /**
     * 开通垫资结算 (1-T0  2-D0   0-关闭)
     */
         @TableField("OPEN_FUND_SETTLEMENT")
    private String openFundSettlement;

        /**
     * 计费方式 （数据字典：fee_type 1-固定值 2-按比例)
     */
         @TableField("FEE_TYPE")
         @DictionaryCode("fee_type")
         @Length(min = 1,max = 1,message = "计费方式 feeType 长度只能为1")
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
     * 单日最高垫资金额
     */
         @TableField("SINGLE_DAY_MAX_FUND")
         @NotNull(message = "单日最高垫资金额 singleDayMaxFund 不能为空")
    private String singleDayMaxFund;

        /**
     * 垫资结算比例
     */
         @TableField("FUND_SETTLEMENT_SCALE")
         @NotNull(message = "垫资结算比例 fundSettlementScale 不能为空")
    private String fundSettlementScale;

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

        /**
     * 更新记录
     */
         @TableField("UPDATE_RECORD")
    private String updateRecord;

    /**
     * 审核事件ID
     */
    @NotNull(message = "auditEventId不能为空",groups = {ValidGroup1.class})
    @TableField("AUDIT_EVENT_ID")
    private Long auditEventId;


}
