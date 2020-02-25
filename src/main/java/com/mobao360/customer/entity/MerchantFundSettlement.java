package com.mobao360.customer.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.mobao360.system.annotation.DictionaryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("BAS_MERCHANT_FUND_SETTLEMENT")
@KeySequence("SEQ_B_M_FUND_SETTLEMENT")
public class MerchantFundSettlement implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

        /**
     * 客户号
     */
         @TableField("CUSTOMER_NO")
    private String customerNo;

    /**
     * 商户全称（冗余设计）
     */
    @TableField("CUSTOMER_NAME")
    private String customerName;

        /**
     * 开通垫资结算 (1-T0  2-D0   0-关闭)
     */
         @TableField("OPEN_FUND_SETTLEMENT")
    private String openFundSettlement;

        /**
     * 计费方式 （数据字典：fee_type 1-固定值 2-按比例）
     */
         @TableField("FEE_TYPE")
         @DictionaryCode("fee_type")
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
    private String singleDayMaxFund;

        /**
     * 垫资结算比例
     */
         @TableField("FUND_SETTLEMENT_SCALE")
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOpenFundSettlement() {
        return openFundSettlement;
    }

    public void setOpenFundSettlement(String openFundSettlement) {
        this.openFundSettlement = openFundSettlement;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public String getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(String fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public String getSingleDayMaxFund() {
        return singleDayMaxFund;
    }

    public void setSingleDayMaxFund(String singleDayMaxFund) {
        this.singleDayMaxFund = singleDayMaxFund;
    }

    public String getFundSettlementScale() {
        return fundSettlementScale;
    }

    public void setFundSettlementScale(String fundSettlementScale) {
        this.fundSettlementScale = fundSettlementScale;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
