package com.mobao360.customer.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.mobao360.system.annotation.DictionaryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商户配置信息
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BAS_MERCHANT_CONFIG")
@KeySequence("SEQ_BAS_MERCHANT_CONFIG")
public class MerchantConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 客户号
     */
    @TableField("CUSTOMER_NO")
    private String customerNo;

    /**
     * 结算周期（数据字典 settlement_period）
     */
    @TableField("SETTLEMENT_PERIOD")
    @DictionaryCode("settlement_period")
    private String settlementPeriod;

    /**
     * 消费（0：关闭 ，1：开通）
     */
    @TableField("CONSUME")
    private String consume;

    /**
     * 退款（0：关闭 1：开通）
     */
    @TableField("REFUND")
    private String refund;

    /**
     * 结算 （0：关闭，1：开通）
     */
    @TableField("SETTLEMENT")
    private String settlement;

    /**
     * 跨境 （0：关闭，1：开通）
     */
    @TableField("CROSS_BORDER")
    private String crossBorder;


    /**
     * 支付方式（多选，逗号隔开。数据字典：pay_type）
     */
    @TableField("PAY_TYPE")
    private String payType;

    /**
     * 支付方式（中文名称，逗号隔开）
     */
    @TableField("PAY_TYPE_DIC")
    private String payTypeDic;


    /**
     * 最低提现金额（以元为单位存储，保留两位小数，页面默认值500.00元）
     */
    @TableField("MIN_WITHDRAW_CASH_MONEY")
    private String minWithdrawCashMoney;


    /**
     * 支付账户（0：关闭 ，1：开通）
     */
    @TableField("PAYMENT_ACCOUNT")
    private String paymentAccount;

    /**
     * 汇差保证金比例
     */
    @TableField("EXCHANGE_DEPOSIT_RATE")
    private String exchangeDepositRate;

    /**
     * 购付汇银行通道（数据字典 purchase_payment_bank_channel）
     */
    @TableField("PURCHASE_PAYMENT_BANK_CHANNEL")
    @DictionaryCode("purchase_payment_bank_channel" )
    private String purchasePaymentBankChannel;

    /**
     * 收结汇银行通道（待选项与“购付汇银行通道”相同）
     */
    @TableField("RECEIVE_SETT_BANK_CHANNEL")
    @DictionaryCode("purchase_payment_bank_channel" )
    private String receiveSettBankChannel;


    /**
     * 开通币种账户（支持多选，逗号分隔。数据字典：currency_type）
     */
    @TableField("OPENED_CURRENCY_ACCOUNT")
    private String openedCurrencyAccount;

    /**
     * 开通币种账户（中文名称，逗号分隔。）
     */
    @TableField("OPENED_CURRENCY_ACCOUNT_DIC")
    private String openedCurrencyAccountDic;

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

    public String getSettlementPeriod() {
        return settlementPeriod;
    }

    public void setSettlementPeriod(String settlementPeriod) {
        this.settlementPeriod = settlementPeriod;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public String getCrossBorder() {
        return crossBorder;
    }

    public void setCrossBorder(String crossBorder) {
        this.crossBorder = crossBorder;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTypeDic() {
        return payTypeDic;
    }

    public void setPayTypeDic(String payTypeDic) {
        this.payTypeDic = payTypeDic;
    }

    public String getMinWithdrawCashMoney() {
        return minWithdrawCashMoney;
    }

    public void setMinWithdrawCashMoney(String minWithdrawCashMoney) {
        this.minWithdrawCashMoney = minWithdrawCashMoney;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getExchangeDepositRate() {
        return exchangeDepositRate;
    }

    public void setExchangeDepositRate(String exchangeDepositRate) {
        this.exchangeDepositRate = exchangeDepositRate;
    }

    public String getPurchasePaymentBankChannel() {
        return purchasePaymentBankChannel;
    }

    public void setPurchasePaymentBankChannel(String purchasePaymentBankChannel) {
        this.purchasePaymentBankChannel = purchasePaymentBankChannel;
    }

    public String getReceiveSettBankChannel() {
        return receiveSettBankChannel;
    }

    public void setReceiveSettBankChannel(String receiveSettBankChannel) {
        this.receiveSettBankChannel = receiveSettBankChannel;
    }

    public String getOpenedCurrencyAccount() {
        return openedCurrencyAccount;
    }

    public void setOpenedCurrencyAccount(String openedCurrencyAccount) {
        this.openedCurrencyAccount = openedCurrencyAccount;
    }

    public String getOpenedCurrencyAccountDic() {
        return openedCurrencyAccountDic;
    }

    public void setOpenedCurrencyAccountDic(String openedCurrencyAccountDic) {
        this.openedCurrencyAccountDic = openedCurrencyAccountDic;
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
