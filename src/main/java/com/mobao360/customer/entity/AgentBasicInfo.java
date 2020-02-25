package com.mobao360.customer.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.mobao360.system.annotation.DictionaryCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@KeySequence("SEQ_BAS_AGENT_BASIC_INFO")
@TableName("BAS_AGENT_BASIC_INFO")
public class AgentBasicInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 代理商号
     */
    @TableField("AGENT_NO")
    private String agentNo;

    /** 分支机构号 */
    @TableField("BRANCH_NO")
    private String branchNo;

    /**
     * 代理商全称
     */
         @TableField("NAME")
    private String name;

        /**
     * 代理商简称
     */
         @TableField("SHORT_NAME")
    private String shortName;

        /**
     * 法人代表
     */
         @TableField("LEGAL_PERSON")
    private String legalPerson;


        /**
     * 法人证件类型（数据字典 certificate_type：0-身份证 1-护照）
     */
         @TableField("LEGAL_PERSON_CERT_TYPE")
         @DictionaryCode("certificate_type")
    private String legalPersonCertType;

        /**
     * 法人证件号
     */
         @TableField("LEGAL_PERSON_CERT_NUM")
    private String legalPersonCertNum;

        /**
     * 法人证件有效期开始时间
     */
         @TableField("LEGAL_PERSON_CERT_PERIOD_START")
    private String legalPersonCertPeriodStart;

        /**
     * 法人证件有效期结束时间
     */
         @TableField(value = "LEGAL_PERSON_CERT_PERIOD_END", strategy = FieldStrategy.IGNORED,el = "legalPersonCertPeriodEnd,jdbcType=VARCHAR")
    private String legalPersonCertPeriodEnd;

        /**
     * 营业执照号
     */
         @TableField("BUSINESS_LICENCE_NO")
    private String businessLicenceNo;

        /**
     * 营业执照有效期开始时间
     */
         @TableField("BUSINESS_LICENCE_PERIOD_START")
    private String businessLicencePeriodStart;

        /**
     * 营业执照有效期结束时间
     */
         @TableField(value = "BUSINESS_LICENCE_PERIOD_END", strategy = FieldStrategy.IGNORED,el = "businessLicencePeriodEnd,jdbcType=VARCHAR")
    private String businessLicencePeriodEnd;

        /**
     * 注册资金
     */
         @TableField("REGISTERED_FUND")
    private String registeredFund;

        /**
     * 企业经营范围
     */
         @TableField("BUSINESS_SCOPE")
    private String businessScope;

        /**
     * 代理商地址
     */
         @TableField("AGENT_ADDRESS")
    private String agentAddress;

        /**
     * 代理平台网址
     */
         @TableField("AGENT_PLATFORM_URL")
    private String agentPlatformUrl;

        /**
     * 联系人
     */
         @TableField("CONTACT_PERSON")
    private String contactPerson;

        /**
     * 联系电话
     */
         @TableField("CONTACT_PHONE")
    private String contactPhone;

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
     * 联系人身份证号码
     */
    @TableField("CONTACT_CERT_NUM")
    private String contactCertNum;

    /**
     * 联系人身份证有效期开始时间
     */
    @TableField("CONTACT_ID_PERIOD_START")
    private String contactIdPeriodStart;

    /**
     * 联系人身份证有效期结束时间
     */
    @TableField(value = "CONTACT_ID_PERIOD_END", strategy = FieldStrategy.IGNORED,el = "contactIdPeriodEnd,jdbcType=VARCHAR")
    private String contactIdPeriodEnd;

    /**
     * 代理状态
     */
    @TableField("AGENT_STATUS")
    private String agentStatus;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getLegalPersonCertType() {
        return legalPersonCertType;
    }

    public void setLegalPersonCertType(String legalPersonCertType) {
        this.legalPersonCertType = legalPersonCertType;
    }

    public String getLegalPersonCertNum() {
        return legalPersonCertNum;
    }

    public void setLegalPersonCertNum(String legalPersonCertNum) {
        this.legalPersonCertNum = legalPersonCertNum;
    }

    public String getLegalPersonCertPeriodStart() {
        return legalPersonCertPeriodStart;
    }

    public void setLegalPersonCertPeriodStart(String legalPersonCertPeriodStart) {
        this.legalPersonCertPeriodStart = legalPersonCertPeriodStart;
    }

    public String getLegalPersonCertPeriodEnd() {
        return legalPersonCertPeriodEnd;
    }

    public void setLegalPersonCertPeriodEnd(String legalPersonCertPeriodEnd) {
        this.legalPersonCertPeriodEnd = legalPersonCertPeriodEnd;
    }

    public String getBusinessLicenceNo() {
        return businessLicenceNo;
    }

    public void setBusinessLicenceNo(String businessLicenceNo) {
        this.businessLicenceNo = businessLicenceNo;
    }

    public String getBusinessLicencePeriodStart() {
        return businessLicencePeriodStart;
    }

    public void setBusinessLicencePeriodStart(String businessLicencePeriodStart) {
        this.businessLicencePeriodStart = businessLicencePeriodStart;
    }

    public String getBusinessLicencePeriodEnd() {
        return businessLicencePeriodEnd;
    }

    public void setBusinessLicencePeriodEnd(String businessLicencePeriodEnd) {
        this.businessLicencePeriodEnd = businessLicencePeriodEnd;
    }

    public String getRegisteredFund() {
        return registeredFund;
    }

    public void setRegisteredFund(String registeredFund) {
        this.registeredFund = registeredFund;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getAgentPlatformUrl() {
        return agentPlatformUrl;
    }

    public void setAgentPlatformUrl(String agentPlatformUrl) {
        this.agentPlatformUrl = agentPlatformUrl;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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

    public String getContactCertNum() {
        return contactCertNum;
    }

    public void setContactCertNum(String contactCertNum) {
        this.contactCertNum = contactCertNum;
    }

    public String getContactIdPeriodStart() {
        return contactIdPeriodStart;
    }

    public void setContactIdPeriodStart(String contactIdPeriodStart) {
        this.contactIdPeriodStart = contactIdPeriodStart;
    }

    public String getContactIdPeriodEnd() {
        return contactIdPeriodEnd;
    }

    public void setContactIdPeriodEnd(String contactIdPeriodEnd) {
        this.contactIdPeriodEnd = contactIdPeriodEnd;
    }

    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }
}
