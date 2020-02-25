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
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BAS_MERCHANT_FEE_RATE")
@KeySequence("SEQ_BAS_MERCHANT_FEE_RATE")
public class MerchantFeeRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /** 客户号 */
    @TableField("CUSTOMER_NO")
    private String customerNo;

    /** 产品类型 （数据字典：product_kind）*/
    @TableField("PRODUCT_KIND")
    @DictionaryCode("product_kind")
    private String productKind;

    /** 支付方式 （数据字典：pay_type）*/
    @TableField("PAY_TYPE")
    @DictionaryCode("pay_type")
    private String payType;

    /** 银行编码 */
    @TableField("BANK_CODE")
    private String bankCode;

    /** 计费方式 （数据字典：fee_type 1-固定值 2-按比例）*/
    @TableField("FEE_TYPE")
    @DictionaryCode("fee_type")
    private String feeType;

    /** 费率 （计费方式为“按比例”时赋值）*/
    @TableField("FEE_RATE")
    private String feeRate;

    /** 固定金额 （计费方式为“固定值”时赋值）*/
    @TableField("FIXED_AMOUNT")
    private String fixedAmount;

    /** 保底 */
    @TableField("MIN_FEE")
    private String minFee;

    /** 封顶 */
    @TableField("MAX_FEE")
    private String maxFee;

    /** 生效时间 */
    @TableField("START_TIME")
    private String startTime;

    /** 失效日期 */
    @TableField("END_TIME")
    private String endTime;

    /** 产品预留，暂未使用 */
    @TableField("IF_EFFECTIVE")
    private String ifEffective;

    @TableField("CREATE_TIME")
    private String createTime;

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

    public String getProductKind() {
        return productKind;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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

    public String getMinFee() {
        return minFee;
    }

    public void setMinFee(String minFee) {
        this.minFee = minFee;
    }

    public String getMaxFee() {
        return maxFee;
    }

    public void setMaxFee(String maxFee) {
        this.maxFee = maxFee;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIfEffective() {
        return ifEffective;
    }

    public void setIfEffective(String ifEffective) {
        this.ifEffective = ifEffective;
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
