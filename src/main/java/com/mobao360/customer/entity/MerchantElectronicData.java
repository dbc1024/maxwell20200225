package com.mobao360.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BAS_MERCHANT_ELECTRONIC_DATA")
@KeySequence("SEQ_BAS_M_E_D")
public class MerchantElectronicData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

        /**
     * 营业执照(三证合一)
     */
         @TableField("BUSINESS_LICENSE")
    private String businessLicense;

        /**
     * 开户许可证
     */
         @TableField("OPEN_ACCOUNT_PERMIT")
    private String openAccountPermit;

        /**
     * 法人身份证正面
     */
         @TableField("CORP_ID_CARD_FRONT")
    private String corpIdCardFront;

        /**
     * 法人身份证反面 
     */
         @TableField("CORP_ID_CARD_BACK")
    private String corpIdCardBack;

        /**
     * 法人身份信息核查
     */
         @TableField("CORP_IDENTITY_CHECK")
    private String corpIdentityCheck;

        /**
     * 法人手持身份证
     */
         @TableField("CORP_HAND_ID_CARD")
    private String corpHandIdCard;

        /**
     * ICP备案信息 
     */
         @TableField("ICP_ARCHIVAL")
    private String icpArchival;

        /**
     * ICP授权书（非自有）
     */
         @TableField("ICP_AUTHORIZATION")
    private String icpAuthorization;

        /**
     * Mo宝支付服务协议 
     */
         @TableField("MOBAO_PAYMENT_AGREEMENT")
    private String mobaoPaymentAgreement;

        /**
     * Mo宝支付商户信息登记表 
     */
         @TableField("MOBAO_MERCHANT_REGISTRATION")
    private String mobaoMerchantRegistration;

        /**
     * Mo宝支付委托结算协议
     */
         @TableField("MOBAO_ENTRUST_AGREEMENT")
    private String mobaoEntrustAgreement;

        /**
     * 委托结算人员名单
     */
         @TableField("ENTRUST_PERSONNEL_LIST")
    private String entrustPersonnelList;

        /**
     * 股东出资说明
     */
         @TableField("SHAREHOLDER_FUNDING")
    private String shareholderFunding;

        /**
     * 业务模式情况说明  
     */
         @TableField("BUSINESS_MODEL")
    private String businessModel;

        /**
     * 特殊行业经营许可证明 
     */
         @TableField("SPECIAL_BUSINESS_CERTIFICATE")
    private String specialBusinessCertificate;

        /**
     * 工商信息
     */
         @TableField("BUSINESS_INFO")
    private String businessInfo;

        /**
     * 办公场景照 
     */
         @TableField("OFFICE_SCENE_PHOTO")
    private String officeScenePhoto;

        /**
     * 门牌照
     */
         @TableField("DOORPLATE_PHOTO")
    private String doorplatePhoto;

        /**
     * 网站首页
     */
         @TableField("WEBSITE_HOMEPAGE")
    private String websiteHomepage;

        /**
     * 网站下单页面 
     */
         @TableField("WEBSITE_ORDER_PAGE")
    private String websiteOrderPage;

        /**
     * 海关报关单位注册登记证书
     */
         @TableField("CUSTOMS_REGISTRATION")
    private String customsRegistration;

        /**
     * 对外贸易经营者备案登记表
     */
         @TableField("FOREIGN_TRADE_REGISTRATION")
    private String foreignTradeRegistration;

        /**
     * 与物流合作协议
     */
         @TableField("LOGISTICS_COMPACT")
    private String logisticsCompact;

        /**
     * Mo宝出口电商跨境支付协议
     */
         @TableField("MOBAO_OUT_CROSS_BORDER_PAYMENT")
    private String mobaoOutCrossBorderPayment;

        /**
     * Mo宝跨境支付服务协议
     */
         @TableField("MOBAO_CROSS_BORDER_PAYMENT")
    private String mobaoCrossBorderPayment;

        /**
     * 个人身份信息核查
     */
         @TableField("INDIVIDUAL_IDENTITY_CHECK")
    private String individualIdentityCheck;

        /**
     * 境外网站备案信息
     */
         @TableField("OVERSEAS_WEBSITE_ARCHIVAL")
    private String overseasWebsiteArchival;

        /**
     * 挂靠平台合作协议
     */
         @TableField("DEPEND_PLATFORM_COMPACT")
    private String dependPlatformCompact;

        /**
     * 与境外商户合作协议
     */
         @TableField("CROSS_BORDER_MERCHANT_COMPACT")
    private String crossBorderMerchantCompact;

        /**
     * 与境内商户合作协议
     */
         @TableField("MERCHANT_COMPACT")
    private String merchantCompact;

        /**
     * 政府部门营业证明文件
     */
         @TableField("GOVERNMENT_BUSINESS_CERTIFY")
    private String governmentBusinessCertify;

        /**
     * 法定授权代表人身份证明文件
     */
         @TableField("CORP_IDENTITY")
    private String corpIdentity;

        /**
     * 仓库照
     */
         @TableField("WAREHOUSE_PHOTO")
    private String warehousePhoto;

        /**
     * 进货渠道证明
     */
         @TableField("STOCK_CHANNEL_CERTIFY ")
    private String stockChannelCertify ;

        /**
     * 收款银行账户证明
     */
         @TableField("DUE_BANK_ACCOUNT_CERTIFY ")
    private String dueBankAccountCertify ;

        /**
     * 其他信息
     */
         @TableField("OTHERS")
    private String others;

    /**
     * 补充资料
     */
    @TableField("SUPPLEMENT")
    private String supplement;

        /**
     * 客户号
     */
         @TableField("CUSTOMER_NO")
    private String customerNo;

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

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getOpenAccountPermit() {
        return openAccountPermit;
    }

    public void setOpenAccountPermit(String openAccountPermit) {
        this.openAccountPermit = openAccountPermit;
    }

    public String getCorpIdCardFront() {
        return corpIdCardFront;
    }

    public void setCorpIdCardFront(String corpIdCardFront) {
        this.corpIdCardFront = corpIdCardFront;
    }

    public String getCorpIdCardBack() {
        return corpIdCardBack;
    }

    public void setCorpIdCardBack(String corpIdCardBack) {
        this.corpIdCardBack = corpIdCardBack;
    }

    public String getCorpIdentityCheck() {
        return corpIdentityCheck;
    }

    public void setCorpIdentityCheck(String corpIdentityCheck) {
        this.corpIdentityCheck = corpIdentityCheck;
    }

    public String getCorpHandIdCard() {
        return corpHandIdCard;
    }

    public void setCorpHandIdCard(String corpHandIdCard) {
        this.corpHandIdCard = corpHandIdCard;
    }

    public String getIcpArchival() {
        return icpArchival;
    }

    public void setIcpArchival(String icpArchival) {
        this.icpArchival = icpArchival;
    }

    public String getIcpAuthorization() {
        return icpAuthorization;
    }

    public void setIcpAuthorization(String icpAuthorization) {
        this.icpAuthorization = icpAuthorization;
    }

    public String getMobaoPaymentAgreement() {
        return mobaoPaymentAgreement;
    }

    public void setMobaoPaymentAgreement(String mobaoPaymentAgreement) {
        this.mobaoPaymentAgreement = mobaoPaymentAgreement;
    }

    public String getMobaoMerchantRegistration() {
        return mobaoMerchantRegistration;
    }

    public void setMobaoMerchantRegistration(String mobaoMerchantRegistration) {
        this.mobaoMerchantRegistration = mobaoMerchantRegistration;
    }

    public String getMobaoEntrustAgreement() {
        return mobaoEntrustAgreement;
    }

    public void setMobaoEntrustAgreement(String mobaoEntrustAgreement) {
        this.mobaoEntrustAgreement = mobaoEntrustAgreement;
    }

    public String getEntrustPersonnelList() {
        return entrustPersonnelList;
    }

    public void setEntrustPersonnelList(String entrustPersonnelList) {
        this.entrustPersonnelList = entrustPersonnelList;
    }

    public String getShareholderFunding() {
        return shareholderFunding;
    }

    public void setShareholderFunding(String shareholderFunding) {
        this.shareholderFunding = shareholderFunding;
    }

    public String getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
    }

    public String getSpecialBusinessCertificate() {
        return specialBusinessCertificate;
    }

    public void setSpecialBusinessCertificate(String specialBusinessCertificate) {
        this.specialBusinessCertificate = specialBusinessCertificate;
    }

    public String getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo;
    }

    public String getOfficeScenePhoto() {
        return officeScenePhoto;
    }

    public void setOfficeScenePhoto(String officeScenePhoto) {
        this.officeScenePhoto = officeScenePhoto;
    }

    public String getDoorplatePhoto() {
        return doorplatePhoto;
    }

    public void setDoorplatePhoto(String doorplatePhoto) {
        this.doorplatePhoto = doorplatePhoto;
    }

    public String getWebsiteHomepage() {
        return websiteHomepage;
    }

    public void setWebsiteHomepage(String websiteHomepage) {
        this.websiteHomepage = websiteHomepage;
    }

    public String getWebsiteOrderPage() {
        return websiteOrderPage;
    }

    public void setWebsiteOrderPage(String websiteOrderPage) {
        this.websiteOrderPage = websiteOrderPage;
    }

    public String getCustomsRegistration() {
        return customsRegistration;
    }

    public void setCustomsRegistration(String customsRegistration) {
        this.customsRegistration = customsRegistration;
    }

    public String getForeignTradeRegistration() {
        return foreignTradeRegistration;
    }

    public void setForeignTradeRegistration(String foreignTradeRegistration) {
        this.foreignTradeRegistration = foreignTradeRegistration;
    }

    public String getLogisticsCompact() {
        return logisticsCompact;
    }

    public void setLogisticsCompact(String logisticsCompact) {
        this.logisticsCompact = logisticsCompact;
    }

    public String getMobaoOutCrossBorderPayment() {
        return mobaoOutCrossBorderPayment;
    }

    public void setMobaoOutCrossBorderPayment(String mobaoOutCrossBorderPayment) {
        this.mobaoOutCrossBorderPayment = mobaoOutCrossBorderPayment;
    }

    public String getMobaoCrossBorderPayment() {
        return mobaoCrossBorderPayment;
    }

    public void setMobaoCrossBorderPayment(String mobaoCrossBorderPayment) {
        this.mobaoCrossBorderPayment = mobaoCrossBorderPayment;
    }

    public String getIndividualIdentityCheck() {
        return individualIdentityCheck;
    }

    public void setIndividualIdentityCheck(String individualIdentityCheck) {
        this.individualIdentityCheck = individualIdentityCheck;
    }

    public String getOverseasWebsiteArchival() {
        return overseasWebsiteArchival;
    }

    public void setOverseasWebsiteArchival(String overseasWebsiteArchival) {
        this.overseasWebsiteArchival = overseasWebsiteArchival;
    }

    public String getDependPlatformCompact() {
        return dependPlatformCompact;
    }

    public void setDependPlatformCompact(String dependPlatformCompact) {
        this.dependPlatformCompact = dependPlatformCompact;
    }

    public String getCrossBorderMerchantCompact() {
        return crossBorderMerchantCompact;
    }

    public void setCrossBorderMerchantCompact(String crossBorderMerchantCompact) {
        this.crossBorderMerchantCompact = crossBorderMerchantCompact;
    }

    public String getMerchantCompact() {
        return merchantCompact;
    }

    public void setMerchantCompact(String merchantCompact) {
        this.merchantCompact = merchantCompact;
    }

    public String getGovernmentBusinessCertify() {
        return governmentBusinessCertify;
    }

    public void setGovernmentBusinessCertify(String governmentBusinessCertify) {
        this.governmentBusinessCertify = governmentBusinessCertify;
    }

    public String getCorpIdentity() {
        return corpIdentity;
    }

    public void setCorpIdentity(String corpIdentity) {
        this.corpIdentity = corpIdentity;
    }

    public String getWarehousePhoto() {
        return warehousePhoto;
    }

    public void setWarehousePhoto(String warehousePhoto) {
        this.warehousePhoto = warehousePhoto;
    }

    public String getStockChannelCertify() {
        return stockChannelCertify;
    }

    public void setStockChannelCertify(String stockChannelCertify) {
        this.stockChannelCertify = stockChannelCertify;
    }

    public String getDueBankAccountCertify() {
        return dueBankAccountCertify;
    }

    public void setDueBankAccountCertify(String dueBankAccountCertify) {
        this.dueBankAccountCertify = dueBankAccountCertify;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
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

    public String getSupplement() {
        return supplement;
    }

    public void setSupplement(String supplement) {
        this.supplement = supplement;
    }
}
