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
@TableName("BAS_MERCHANT_INFO")
@KeySequence("SEQ_BAS_MERCHANT_INFO")
public class MerchantInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /** 分支机构号 */
    @TableField("BRANCH_NO")
    private String branchNo;

    /**
     * 客户号
     */
    @TableField("CUSTOMER_NO")
    private String customerNo;

    /**
     * 老客户号(对应老系统迁移数据的客户号)
     */
    @TableField("OLD_CUSTOMER_NO")
    private String oldCustomerNo;

    /**
     * 合作商户对应的平台商户的客户号
     */
    @TableField("PLATFORM_CUSTOMER_NO")
    private String platformCustomerNo;

    /**
     * 商户全称
     */
    @TableField("NAME")
    private String name;

    /**
     * 商户简称
     */
    @TableField("SHORT_NAME")
    private String shortName;

    /**
     * 商户类型(数据字典 merchant_type：1-个人，2-企业)
     */
    @TableField("MERCHANT_TYPE")
    @DictionaryCode("merchant_type")
    private String merchantType;

    /**
     * 商户属性(数据字典 merchant_attribute 1-直营商户 2-代理商户)
     */
    @TableField("MERCHANT_ATTRIBUTE")
    @DictionaryCode("merchant_attribute")
    private String merchantAttribute;

    /**
     * 商户性质 1-一般商户 2-平台商户 3-合作商户
     */
    @TableField("MERCHANT_PROPERTY")
    private String merchantProperty;


    /**
     * 所属行业（关联"行业信息"模块IndustryInfo）
     */
    @TableField("INDUSTRY_CODE")
    private String industryCode;

    /**
     * 所属行业名称（冗余设计）
     */
    @TableField("INDUSTRY_NAME")
    private String industryName;

    /**
     * 所属地区(数据字典 area_code)
     */
    @TableField("AREA_CODE")
    @DictionaryCode("area_code")
    private String areaCode;

    /**
     * 业务种类（数据字典 business_kind：1-互联网，2-移动支付）
     * */
    @TableField("BUSINESS_KIND")
    @DictionaryCode("business_kind")
    private String businessKind;

    /**
     * 所属销售（关联销售信息Salesman）
     */
    @TableField("SALESMAN_NO")
    private String salesmanNo;
    /**
     * 所属销售姓名（冗余设计）
     */
    @TableField("SALESMAN_NAME")
    private String salesmanName;

    /**
     * 所属代理 （关联代理信息）
     */
    @TableField("AGENT_NO")
    private String agentNo;

    /**
     * 所属代理名称 （冗余设计）
     */
    @TableField("AGENT_NAME")
    private String agentName;

    /** 商户业务类型
     * (数据字典 merchant_business_type：1-普通业务，2-跨境进口，3-跨境出口，4-跨境通关)
     * 赋值方式为逗号隔开，且前后加逗号，如：,1, 或 ,1,2,3,
     */
    @TableField("BUSINESS_TYPE")
    private String businessType;

    /**
     * 商户业务类型(中文名称，逗号分隔)
     * 仅用于页面的中文展示
     */
    @TableField("BUSINESS_TYPE_DIC")
    private String businessTypeDic;


    /**
     * MCC代码（关联MCC表）
     */
    @TableField("MCC_CODE")
    private String mccCode;
    /**
     * MCC类目名称（冗余设计）
     */
    @TableField("MCC_KIND_NAME")
    private String mccKindName;

    /**
     * 微信经营类目代码（关联微信经营类目表）
     */
    @TableField("WECHAT_BUSINESS_KIND_CODE")
    private String wechatBusinessKindCode;
    /**
     * 微信经营类目名称（冗余设计）
     */
    @TableField("WECHAT_BUSINESS_KIND_NAME")
    private String wechatBusinessKindName;

    /**
     * 网联业务种类代码（关联网联业务种类表）
     */
    @TableField("NUCC_BUSINESS_KIND_CODE")
    private String nuccBusinessKindCode;
    /**
     * 网联业务种类名称（冗余设计）
     */
    @TableField("NUCC_BUSINESS_KIND_NAME")
    private String nuccBusinessKindName;

    /**
     * 银联业务种类
     */
    @DictionaryCode("cup_business_kind")
    @TableField("CUP_BUSINESS_KIND")
    private String cupBusinessKind;


    /**
     * 支付宝经营类目（数据字典 alipay_business_kind）
     */
    @DictionaryCode("alipay_business_kind")
    @TableField("ALIPAY_BUSINESS_KIND")
    private String alipayBusinessKind;




    /**
     * 法人代表
     */
    @TableField("LEGAL_PERSON")
    private String legalPerson;

    /**
     * 法人职业（数据字典 occupation）
     */
    @TableField("LEGAL_PERSON_CAREER")
    @DictionaryCode("occupation")
    private String legalPersonCareer;

    /**
     * 法人国籍（数据字典 nationality）
     */
    @TableField("LEGAL_PERSON_NATIONALITY")
    @DictionaryCode("nationality")
    private String legalPersonNationality;

    /**
     * 法人证件类型（数据字典 certificate_type：0-身份证 1-护照）
     */
    @TableField("LEGAL_PERSON_CERT_TYPE")
    @DictionaryCode("certificate_type")
    private String legalPersonCertType;

    /**
     * 法人证件编号
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
     * 法人性别（数据字典 sex：0-男 1-女）
     */
    @TableField("LEGAL_PERSON_SEX")
    @DictionaryCode("sex")
    private String legalPersonSex;







    /**
     * 商户状态（数据字典 merchant_status：1-活动 2-锁定 3-注销）
     */
         @TableField("STATUS")
         @DictionaryCode("merchant_status")
    private String status;

        /**
     * 控股股东或实际控制人
     */
         @TableField("SHAREHOLDER")
    private String shareholder;

        /**
     * 股东身份证号
     */
         @TableField("SHAREHOLDER_ID_NUM")
    private String shareholderIdNum;


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
         @TableField(value = "BUSINESS_LICENCE_PERIOD_END",strategy = FieldStrategy.IGNORED,el = "businessLicencePeriodEnd,jdbcType=VARCHAR")
    private String businessLicencePeriodEnd;

        /**
     * 注册资金
     */
         @TableField("REGISTERED_FUND")
    private String registeredFund;

        /**
     * 经营范围
     */
         @TableField("BUSINESS_SCOPE")
    private String businessScope;

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
     * 联系邮箱
     */
    @TableField("CONTACT_EMAIL")
    private String contactEmail;

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
     * 商户地址
     */
         @TableField("ADDRESS")
    private String address;

    /**
     * 商户域名
     */
    @TableField("MERCHANT_DOMAIN")
    private String merchantDomain;

        /**
     * 备案域名/电商网站域名
     */
         @TableField("ECOMMERCE_DOMAIN")
    private String ecommerceDomain;

        /**
     * ICP备案号
     */
         @TableField("ICP")
    private String icp;

        /**
     * ICP备案有效日期开始时间
     */
         @TableField("ICP_PERIOD_START")
    private String icpPeriodStart;

    /**
     * ICP备案有效日期结束时间
     */
    @TableField(value = "ICP_PERIOD_END", strategy = FieldStrategy.IGNORED,el = "icpPeriodEnd,jdbcType=VARCHAR")
    private String icpPeriodEnd;


        /**
     * 授权办理业务人员的姓名
     */
         @TableField("STAFF")
    private String staff;

    /**
     * 客服电话
     */
    @TableField("CUSTOMER_TEL")
    private String customerTel;

        /**
     * 授权办理业务人员证件类型（数据字典 certificate_type：0-身份证 1-护照）
     */
         @TableField("STAFF_CERT_TYPE")
         @DictionaryCode("certificate_type")
    private String staffCertType;

        /**
     * 授权办理业务人员证件号
     */
         @TableField("STAFF_CERT_NUM")
    private String staffCertNum;

        /**
     * 授权办理业务人员证件有效期开始时间
     */
         @TableField("STAFF_CERT_PERIOD_START")
    private String staffCertPeriodStart;

    /**
     * 授权办理业务人员证件有效期结束时间
     */
    @TableField(value = "STAFF_CERT_PERIOD_END", strategy = FieldStrategy.IGNORED,el = "staffCertPeriodEnd,jdbcType=VARCHAR")
    private String staffCertPeriodEnd;


        /**
     * 是否报送海关：0-不报送，1-报送
     */
         @TableField("IF_SUBMIT_CUSTOMS")
    private String ifSubmitCustoms;

    /**
     * 报送海关（数据字典 customs：0-四川 1-成都）
     */
    @TableField("CUSTOMS")
    @DictionaryCode("customs")
    private String customs;

        /**
     * 海关登记编号
     */
         @TableField("CUSTOMS_REGISTER_NO")
    private String customsRegisterNo;

    /**
     * 跨境进口商户属性（数据字典 cross_in_merchant_attribute：1-境内商户，2-境外商户
     */
    @DictionaryCode("cross_in_merchant_attribute")
    @TableField("CROSS_BORDER_ENTRANCE")
    private String crossBorderEntrance;

    /**
     * 出口业务模式（数据字典 exit_business_model：1-境内自建网站，2-境外自建网站，3-挂靠电商平台
     */
         @TableField("EXIT_BUSINESS_MODEL")
         @DictionaryCode("exit_business_model")
    private String exitBusinessModel;

    /**
     * 电商网站名称
     */
         @TableField("ECOMMERCE_WEBSITE_NAME")
    private String ecommerceWebsiteName;

    /** 锁定时间时间 */
    @TableField("LOCK_TIME")
    private String lockTime;

    /** 注销时间 */
    @TableField("CANCEL_TIME")
    private String cancelTime;

    /** 创建时间 */
    @TableField("CREATE_TIME")
    private String createTime;

    /** 更新时间 */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /** 交易是否需要被检查：0-否，1-是 */
    @TableField("TRADE_CHECK")
    private String tradeCheck;

    /** 最后交易日期：由订单系统每天推送*/
    @TableField("LAST_TRADE_DATE")
    private String lastTradeDate;

    /** 最后出款日期：由订单系统每天推送*/
    @TableField("LAST_OUT_DATE")
    private String lastOutDate;

    /** 向交易服务推送开户数据：0-未推送成功，1-已推送成功 */
    @TableField("TASK1")
    private String task1;

    /** 向风控系统推送新入网的客户号：0-未推送成功，1-已推送成功 */
    @TableField("RISK_TASK")
    private String riskTask;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getOldCustomerNo() {
        return oldCustomerNo;
    }

    public void setOldCustomerNo(String oldCustomerNo) {
        this.oldCustomerNo = oldCustomerNo;
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

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getMerchantAttribute() {
        return merchantAttribute;
    }

    public void setMerchantAttribute(String merchantAttribute) {
        this.merchantAttribute = merchantAttribute;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getBusinessKind() {
        return businessKind;
    }

    public void setBusinessKind(String businessKind) {
        this.businessKind = businessKind;
    }

    public String getSalesmanNo() {
        return salesmanNo;
    }

    public void setSalesmanNo(String salesmanNo) {
        this.salesmanNo = salesmanNo;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessTypeDic() {
        return businessTypeDic;
    }

    public void setBusinessTypeDic(String businessTypeDic) {
        this.businessTypeDic = businessTypeDic;
    }

    public String getMccCode() {
        return mccCode;
    }

    public void setMccCode(String mccCode) {
        this.mccCode = mccCode;
    }

    public String getMccKindName() {
        return mccKindName;
    }

    public void setMccKindName(String mccKindName) {
        this.mccKindName = mccKindName;
    }

    public String getWechatBusinessKindCode() {
        return wechatBusinessKindCode;
    }

    public void setWechatBusinessKindCode(String wechatBusinessKindCode) {
        this.wechatBusinessKindCode = wechatBusinessKindCode;
    }

    public String getWechatBusinessKindName() {
        return wechatBusinessKindName;
    }

    public void setWechatBusinessKindName(String wechatBusinessKindName) {
        this.wechatBusinessKindName = wechatBusinessKindName;
    }

    public String getNuccBusinessKindCode() {
        return nuccBusinessKindCode;
    }

    public void setNuccBusinessKindCode(String nuccBusinessKindCode) {
        this.nuccBusinessKindCode = nuccBusinessKindCode;
    }

    public String getNuccBusinessKindName() {
        return nuccBusinessKindName;
    }

    public void setNuccBusinessKindName(String nuccBusinessKindName) {
        this.nuccBusinessKindName = nuccBusinessKindName;
    }

    public String getCupBusinessKind() {
        return cupBusinessKind;
    }

    public void setCupBusinessKind(String cupBusinessKind) {
        this.cupBusinessKind = cupBusinessKind;
    }

    public String getAlipayBusinessKind() {
        return alipayBusinessKind;
    }

    public void setAlipayBusinessKind(String alipayBusinessKind) {
        this.alipayBusinessKind = alipayBusinessKind;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getLegalPersonCareer() {
        return legalPersonCareer;
    }

    public void setLegalPersonCareer(String legalPersonCareer) {
        this.legalPersonCareer = legalPersonCareer;
    }

    public String getLegalPersonNationality() {
        return legalPersonNationality;
    }

    public void setLegalPersonNationality(String legalPersonNationality) {
        this.legalPersonNationality = legalPersonNationality;
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

    public String getLegalPersonSex() {
        return legalPersonSex;
    }

    public void setLegalPersonSex(String legalPersonSex) {
        this.legalPersonSex = legalPersonSex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShareholder() {
        return shareholder;
    }

    public void setShareholder(String shareholder) {
        this.shareholder = shareholder;
    }

    public String getShareholderIdNum() {
        return shareholderIdNum;
    }

    public void setShareholderIdNum(String shareholderIdNum) {
        this.shareholderIdNum = shareholderIdNum;
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

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMerchantDomain() {
        return merchantDomain;
    }

    public void setMerchantDomain(String merchantDomain) {
        this.merchantDomain = merchantDomain;
    }

    public String getEcommerceDomain() {
        return ecommerceDomain;
    }

    public void setEcommerceDomain(String ecommerceDomain) {
        this.ecommerceDomain = ecommerceDomain;
    }

    public String getIcp() {
        return icp;
    }

    public void setIcp(String icp) {
        this.icp = icp;
    }

    public String getIcpPeriodStart() {
        return icpPeriodStart;
    }

    public void setIcpPeriodStart(String icpPeriodStart) {
        this.icpPeriodStart = icpPeriodStart;
    }

    public String getIcpPeriodEnd() {
        return icpPeriodEnd;
    }

    public void setIcpPeriodEnd(String icpPeriodEnd) {
        this.icpPeriodEnd = icpPeriodEnd;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public String getStaffCertType() {
        return staffCertType;
    }

    public void setStaffCertType(String staffCertType) {
        this.staffCertType = staffCertType;
    }

    public String getStaffCertNum() {
        return staffCertNum;
    }

    public void setStaffCertNum(String staffCertNum) {
        this.staffCertNum = staffCertNum;
    }

    public String getStaffCertPeriodStart() {
        return staffCertPeriodStart;
    }

    public void setStaffCertPeriodStart(String staffCertPeriodStart) {
        this.staffCertPeriodStart = staffCertPeriodStart;
    }

    public String getStaffCertPeriodEnd() {
        return staffCertPeriodEnd;
    }

    public void setStaffCertPeriodEnd(String staffCertPeriodEnd) {
        this.staffCertPeriodEnd = staffCertPeriodEnd;
    }

    public String getIfSubmitCustoms() {
        return ifSubmitCustoms;
    }

    public void setIfSubmitCustoms(String ifSubmitCustoms) {
        this.ifSubmitCustoms = ifSubmitCustoms;
    }

    public String getCustoms() {
        return customs;
    }

    public void setCustoms(String customs) {
        this.customs = customs;
    }

    public String getCustomsRegisterNo() {
        return customsRegisterNo;
    }

    public void setCustomsRegisterNo(String customsRegisterNo) {
        this.customsRegisterNo = customsRegisterNo;
    }

    public String getCrossBorderEntrance() {
        return crossBorderEntrance;
    }

    public void setCrossBorderEntrance(String crossBorderEntrance) {
        this.crossBorderEntrance = crossBorderEntrance;
    }

    public String getExitBusinessModel() {
        return exitBusinessModel;
    }

    public void setExitBusinessModel(String exitBusinessModel) {
        this.exitBusinessModel = exitBusinessModel;
    }

    public String getEcommerceWebsiteName() {
        return ecommerceWebsiteName;
    }

    public void setEcommerceWebsiteName(String ecommerceWebsiteName) {
        this.ecommerceWebsiteName = ecommerceWebsiteName;
    }

    public String getLockTime() {
        return lockTime;
    }

    public void setLockTime(String lockTime) {
        this.lockTime = lockTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
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

    public String getTradeCheck() {
        return tradeCheck;
    }

    public void setTradeCheck(String tradeCheck) {
        this.tradeCheck = tradeCheck;
    }

    public String getLastTradeDate() {
        return lastTradeDate;
    }

    public void setLastTradeDate(String lastTradeDate) {
        this.lastTradeDate = lastTradeDate;
    }

    public String getLastOutDate() {
        return lastOutDate;
    }

    public void setLastOutDate(String lastOutDate) {
        this.lastOutDate = lastOutDate;
    }

    public String getTask1() {
        return task1;
    }

    public void setTask1(String task1) {
        this.task1 = task1;
    }

    public String getRiskTask() {
        return riskTask;
    }

    public void setRiskTask(String riskTask) {
        this.riskTask = riskTask;
    }

    public String getMerchantProperty() {
        return merchantProperty;
    }

    public void setMerchantProperty(String merchantProperty) {
        this.merchantProperty = merchantProperty;
    }
}
