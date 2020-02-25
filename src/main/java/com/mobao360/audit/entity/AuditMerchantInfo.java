package com.mobao360.audit.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.mobao360.system.annotation.DictionaryCode;
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
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("AUDIT_MERCHANT_INFO")
@KeySequence("SEQ_AUDIT_MERCHANT_INFO")
public class AuditMerchantInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /**
     * 审核事件ID，关联审核事件表
     */
    @TableField("AUDIT_EVENT_ID")
    private Long auditEventId;

    /**
     * 分支机构号
     */
    @TableField("BRANCH_NO")
    private String branchNo;

    /**
     * 客户号
     */
    @TableField("CUSTOMER_NO")
    private String customerNo;

    /**
     * 合作商户对应的平台商户的客户号
     */
    @TableField("PLATFORM_CUSTOMER_NO")
    private String platformCustomerNo;

    /**
     * 商户全称
     */
    @NotNull(message = "[商户全称name]不能为空")
    @TableField("NAME")
    private String name;

    /**
     * 商户简称
     */
    @TableField("SHORT_NAME")
    @NotNull(message = "[商户简称shortName]不能为空")
    private String shortName;

    /**
     * 商户类型(数据字典 merchant_type：1-个人，2-企业)
     */
    @TableField("MERCHANT_TYPE")
    @DictionaryCode("merchant_type")
    @Length(min = 1, max = 1, message = "商户类型 merchantType 参数长度只能为1")
    private String merchantType;

    /**
     * 商户属性(数据字典 merchant_attribute 1-直营商户 2-代理商户)
     */
    @TableField("MERCHANT_ATTRIBUTE")
    @DictionaryCode("merchant_attribute")
    @Length(min = 1, max = 1, message = "商户属性 merchantAttribute 参数长度只能为1")
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
    @NotNull(message = "所属地区 areaCode 不能为空")
    private String areaCode;

    /**
     * 业务种类（数据字典 business_kind：1-互联网，2-移动支付）
     */
    @TableField("BUSINESS_KIND")
    @Length(min = 1, max = 1, message = "业务种类 businessKind 参数长度只能为1")
    @DictionaryCode("business_kind")
    private String businessKind;

    /**
     * 所属销售（关联销售信息Salesman）
     */
    @TableField("SALESMAN_NO")
//    @NotNull(message = "所属销售 salesmanNo 不能为空")
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

    /**
     * 商户业务类型
     * (数据字典 merchant_business_type：1-普通业务，2-跨境进口，3-跨境出口，4-跨境通关)
     */
    @TableField("BUSINESS_TYPE")
    private String businessType;


    /**
     * 商户业务类型(中文名称，以逗号分隔)
     *
     */
    @TableField("BUSINESS_TYPE_DIC")
    private String businessTypeDic;


    /**
     * MCC代码（关联MCC表）
     */
    @TableField("MCC_CODE")
//    @NotNull(message = "MCC代码 mccCode 不能为空")
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
//    @NotNull(message = "微信经营类目代码 wechatBusinessKindCode 不能为空")
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
//    @NotNull(message = "网联业务种类代码 nuccBusinessKindCode 不能为空")
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
//    @NotNull(message = "支付宝经营类目 alipayBusinessKind 不能为空")
    @TableField("ALIPAY_BUSINESS_KIND")
    private String alipayBusinessKind;


    /**
     * 法人代表
     */
    @TableField("LEGAL_PERSON")
    @Length(min = 2, message = "法人代表 legalPerson 长度至少为2位")
    private String legalPerson;

    /**
     * 法人职业（数据字典 occupation）
     */
    @TableField("LEGAL_PERSON_CAREER")
    @NotNull(message = "法人职业 legalPersonCareer 不能为空")
    @DictionaryCode("occupation")
    private String legalPersonCareer;

    /**
     * 法人国籍（数据字典 nationality）
     */
    @TableField("LEGAL_PERSON_NATIONALITY")
    @NotNull(message = "法人国籍 legalPersonNationality 不能为空")
    @DictionaryCode("nationality")
    private String legalPersonNationality;

    /**
     * 法人证件类型（数据字典 certificate_type：0-身份证 1-护照）
     */
    @TableField("LEGAL_PERSON_CERT_TYPE")
    @Length(min = 1, max = 1, message = "法人证件类型 legalPersonCertType 参数长度只能为1")
    @DictionaryCode("certificate_type")
    private String legalPersonCertType;

    /**
     * 法人证件号码
     */
    @TableField("LEGAL_PERSON_CERT_NUM")
    private String legalPersonCertNum;

    /**
     * 法人证件有效期开始时间
     */
    @TableField("LEGAL_PERSON_CERT_PERIOD_START")
    @NotNull(message = "法人证件有效期开始时间 legalPersonCertPeriodStart 不能为空")
    private String legalPersonCertPeriodStart;

    /**
     * 法人证件有效期结束时间
     */
    @TableField("LEGAL_PERSON_CERT_PERIOD_END")
//    @NotNull(message = "法人证件有效期结束时间 legalPersonCertPeriodEnd 不能为空")
    private String legalPersonCertPeriodEnd;

    /**
     * 法人性别（数据字典 sex：0-男 1-女）
     */
    @TableField("LEGAL_PERSON_SEX")
    @Length(min = 1, max = 1, message = "法人性别 legalPersonSex 参数长度只能为1")
    @DictionaryCode("sex")
    private String legalPersonSex;


    /**
     * 商户状态（数据字典 merchant_status：1-活动 2-锁定 3-注销）
     *
     * 用于商户中心合作商户时：商户状态 01-草稿，02-待审核，99-通过，98-拒绝
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
    @NotNull(message = "[营业执照号businessLicenceNo]不能为空")
    @TableField("BUSINESS_LICENCE_NO")
    private String businessLicenceNo;

    /**
     * 营业执照有效期开始时间
     */
    @TableField("BUSINESS_LICENCE_PERIOD_START")
    @NotNull(message = "[营业执照有效期开始时间 businessLicencePeriodStart]不能为空")
    private String businessLicencePeriodStart;

    /**
     * 营业执照有效期结束时间
     */
    @TableField("BUSINESS_LICENCE_PERIOD_END")
//    @NotNull(message = "[营业执照有效期结束时间 businessLicencePeriodEnd]不能为空")
    private String businessLicencePeriodEnd;

    /**
     * 注册资金
     */
    @TableField("REGISTERED_FUND")
    @NotNull(message = "[注册资金 registeredFund]不能为空")
    private String registeredFund;

    /**
     * 经营范围
     */
    @TableField("BUSINESS_SCOPE")
    @NotNull(message = "[经营范围 businessScope]不能为空")
    private String businessScope;

    /**
     * 联系人
     */
    @TableField("CONTACT_PERSON")
//    @Length(min = 2, message = "[联系人 contactPerson]长度至少2位")
    private String contactPerson;

    /**
     * 联系电话
     */
    @TableField("CONTACT_PHONE")
    private String contactPhone;

    /**
     * 联系人邮箱
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
//    @NotNull(message = "[联系人身份证有效期开始时间 contactIdPeriodStart]不能为空")
    private String contactIdPeriodStart;

    /**
     * 联系人身份证有效期结束时间
     */
    @TableField("CONTACT_ID_PERIOD_END")
//    @NotNull(message = "[联系人身份证有效期结束时间 contactIdPeriodEnd]不能为空")
    private String contactIdPeriodEnd;

    /**
     * 商户地址
     */
    @TableField("ADDRESS")
    @NotNull(message = "[商户地址 address]不能为空")
    private String address;

    /**
     * 商户域名
     */
    @TableField("MERCHANT_DOMAIN")
    @NotNull(message = "[商户域名 merchantDomain]不能为空")
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
    @NotNull(message = "[ICP备案号 icp]不能为空")
    private String icp;

    /**
     * ICP备案有效日期开始时间
     */
    @TableField("ICP_PERIOD_START")
    @NotNull(message = "[ICP备案有效日期开始时间 icpPeriodStart]不能为空")
    private String icpPeriodStart;

    /**
     * ICP备案有效日期结束时间
     */
    @TableField("ICP_PERIOD_END")
//    @NotNull(message = "[ICP备案有效日期结束时间 icpPeriodEnd]不能为空")
    private String icpPeriodEnd;


    /**
     * 授权办理业务人员的姓名
     */
    @TableField("STAFF")
    private String staff;

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
    @TableField("STAFF_CERT_PERIOD_END")
    private String staffCertPeriodEnd;


    /**
     * 是否报送海关：0-报送，1-不报送
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

    /**
     * 锁定时间时间
     */
    @TableField("LOCK_TIME")
    private String lockTime;

    /**
     * 注销时间
     */
    @TableField("CANCEL_TIME")
    private String cancelTime;

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
     * 修改记录
     */
    @TableField("UPDATE_RECORD")
    private String updateRecord;

    /**
     * 客服电话
     */
    @TableField("CUSTOMER_TEL")
    private String customerTel;

    /**
     * 审核意见
     * 商户中心合作商户录入后，运营复核时填写
     */
    @TableField("AUDIT_ADVICE")
    private String auditAdvice;

    @TableField(exist = false)
    private String platformCustomerName;

}
