package com.mobao360.customer.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.mobao360.system.annotation.DictionaryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商户结算信息
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BAS_MERCHANT_SETTLEMENT_INFO")
@KeySequence("SEQ_BAS_SETTLEMENT_INFO")
public class MerchantSettlementInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

        /**
     * 客户号（商户1开头，个人5开头）
     */
         @TableField("CUSTOMER_NO")
    private String customerNo;

        /**
     * 开户户名
     */
         @TableField("BANK_ACCOUNT_NAME")
    private String bankAccountName;

        /**
     * 收款人身份证号
     */
         @TableField("PAYEE_ID_NUM")
    private String payeeIdNum;

        /**
     * 开户账号
     */
         @TableField("ACCOUNT_NO")
    private String accountNo;

        /**
     * 银行预留手机号
     */
         @TableField("RESERVE_MOBILE_NO")
    private String reserveMobileNo;

    /**
     * 账户类型（1：对公 2：对私）
     */
    @TableField("ACCOUNT_TYPE")
    @DictionaryCode("account_type")
    private String accountType;



    /**
     * 开户银行名称
     */
    @TableField("BANK_NAME")
    private String bankName;

    /**
     * 开户银行行号
     */
    @TableField("BANK_NO")
    private String bankNO;



    /**
     * 省名称
     */
    @TableField("PROVINCE")
    private String province;

    /**
     * 省代码
     */
    @TableField("PROVINCE_CODE")
    private String provinceCode;

    /**
     * 市名称
     */
    @TableField("CITY")
    private String city;

    /**
     * 市代码
     */
    @TableField("CITY_CODE")
    private String cityCode;



    /**
     * 分支行名称
     */
    @TableField("BRANCH_BANK_NAME")
    private String branchBankName;

    /**
     * 分支行行号
     */
    @TableField("BRANCH_BANK_NO")
    private String branchBankNo;


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

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getPayeeIdNum() {
        return payeeIdNum;
    }

    public void setPayeeIdNum(String payeeIdNum) {
        this.payeeIdNum = payeeIdNum;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getReserveMobileNo() {
        return reserveMobileNo;
    }

    public void setReserveMobileNo(String reserveMobileNo) {
        this.reserveMobileNo = reserveMobileNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNO() {
        return bankNO;
    }

    public void setBankNO(String bankNO) {
        this.bankNO = bankNO;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getBranchBankName() {
        return branchBankName;
    }

    public void setBranchBankName(String branchBankName) {
        this.branchBankName = branchBankName;
    }

    public String getBranchBankNo() {
        return branchBankNo;
    }

    public void setBranchBankNo(String branchBankNo) {
        this.branchBankNo = branchBankNo;
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
