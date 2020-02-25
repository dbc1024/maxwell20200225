package com.mobao360.base.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.mobao360.system.constant.Constants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 银行编码表
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BAS_BANK_CODE")
@KeySequence("SEQ_BAS_BANK_CODE")
public class BankCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;


    /** 银行编码*/
    @TableField("BANK_CODE")
    private String bankCode;

    /** 发卡行名称*/
    @TableField("BANK_NAME")
    private String bankName;

    /** 总行联行号*/
    @NotNull(message = "headUnionBankNo不能为空")
    @TableField("HEAD_UNION_BANK_NO")
    private String headUnionBankNo;

    /** 网银借记卡（1-启用，0-禁用）*/
    @TableField("INTERNET_BANK_DEBIT")
    private String internetBankDebit;

    /** 网银贷记卡（1-启用，0-禁用）*/
    @TableField("INTERNET_BANK_CREDIT")
    private String internetBankCredit;

    /** 网银混合（1-启用，0-禁用）*/
    @TableField("INTERNET_BANK_MIX")
    private String internetBankMix;

    /** 支持企业网银（1-启用，0-禁用）*/
    @TableField("ENTERPRISE_INTERNET_BANK")
    private String enterpriseInternetBank;

    /** 提现（1-启用，0-禁用）*/
    @TableField("WITHDRAW_CASH")
    private String withdrawCash;

    /** 委托付款（1-启用，0-禁用）*/
    @TableField("ENTRUST_PAY")
    private String entrustPay;

    /** 协议支付借记卡（1-启用，0-禁用）*/
    @TableField("AGREEMENT_DEBIT")
    private String agreementDebit;

    /** 协议支付贷记卡（1-启用，0-禁用）*/
    @TableField("AGREEMENT_CREDIT")
    private String agreementCredit;

    /** 快捷支付借记卡（1-启用，0-禁用）*/
    @TableField("QUICK_DEBIT")
    private String quickDebit;

    /** 快捷支付贷记卡（1-启用，0-禁用）*/
    @TableField("QUICK_CREDIT")
    private String quickCredit;



    public boolean supportPayType(String payType){


        switch (payType){

            case "111":{
                if(Constants.YES.equals(getInternetBankDebit())){
                    return true;
                }
                return false;
            }
            case "112":{
                if(Constants.YES.equals(getInternetBankCredit())){
                    return true;
                }
                return false;
            }
            case "113":{
                if(Constants.YES.equals(getEnterpriseInternetBank())){
                    return true;
                }
                return false;
            }
            case "114":{
                if(Constants.YES.equals(getInternetBankMix())){
                    return true;
                }
                return false;
            }
            case "311":{
                if(Constants.YES.equals(getWithdrawCash())){
                    return true;
                }
                return false;
            }
            case "312":{
                if(Constants.YES.equals(getEntrustPay())){
                    return true;
                }
                return false;
            }
            case "141":{
                if(Constants.YES.equals(getAgreementDebit())){
                    return true;
                }
                return false;
            }
            case "142":{
                if(Constants.YES.equals(getAgreementCredit())){
                    return true;
                }
                return false;
            }
            case "131":{
                if(Constants.YES.equals(getQuickDebit())){
                    return true;
                }
                return false;
            }
            case "132":{
                if(Constants.YES.equals(getQuickCredit())){
                    return true;
                }
                return false;
            }

            default:{
                return false;
            }

        }
    }

}
