package com.mobao360.audit.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.mobao360.system.annotation.DictionaryCode;
import com.mobao360.system.utils.valid.ValidGroup2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("AUDIT_MERCHANT_CONFIG")
@KeySequence("SEQ_AUDIT_M_CONFIG")
public class AuditMerchantConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("AUDIT_EVENT_ID")
    @NotNull(message = "审核事件id   auditEventId  不能为空",groups = ValidGroup2.class)
    private Long auditEventId;

    /**
     * 客户号
     */
    @NotNull(message = "customerNo不能为空")
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

    /**
     * 修改记录
     */
    @TableField("UPDATE_RECORD")
    private String updateRecord;

}
