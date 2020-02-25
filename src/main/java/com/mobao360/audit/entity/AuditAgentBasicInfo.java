package com.mobao360.audit.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.mobao360.system.annotation.DictionaryCode;
import com.mobao360.system.utils.valid.ValidGroup1;
import com.mobao360.system.utils.valid.ValidGroup2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
@TableName("AUDIT_AGENT_BASIC_INFO")
@KeySequence("SEQ_AUDIT_AGENT_BASIC_INFO")
public class AuditAgentBasicInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

        /**
     * 审核事件id
     */
         @TableField("AUDIT_EVENT_ID")
         @NotNull(message = "审核事件id auditEventId 不能为空 " ,groups = {ValidGroup2.class})
    private Long auditEventId;

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
         @NotNull(message = "代理商户全称name 不能为空")
    private String name;

        /**
     * 代理商简称
     */
         @TableField("SHORT_NAME")
         @NotNull(message = "代理商简称shortName 不能为空")
    private String shortName;

        /**
     * 法人代表
     */
         @TableField("LEGAL_PERSON")
         @Length(min = 2, message = "法人代表 legalPerson 长度至少2位")
    private String legalPerson;


        /**
     * 法人证件类型（数据字典 certificate_type：0-身份证 1-护照）
     */
         @TableField("LEGAL_PERSON_CERT_TYPE")
         @DictionaryCode("certificate_type")
         @Length(min = 1,max = 1, message = "法人证件类型legalPersonCertType 参数长度只能为1，0-身份证 1-护照")
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
         @NotNull(message = "法人证件有效期开始时间legalPersonCertPeriodStart 不能为空")
    private String legalPersonCertPeriodStart;

        /**
     * 法人证件有效期结束时间
     */
         @TableField("LEGAL_PERSON_CERT_PERIOD_END")
//         @NotNull(message = "法人证件有效期结束时间legalPersonCertPeriodEnd 不能为空")
    private String legalPersonCertPeriodEnd;

        /**
     * 营业执照号
     */
         @TableField("BUSINESS_LICENCE_NO")
         @NotNull(message = "营业执照号businessLicenceNo 不能为空")
    private String businessLicenceNo;

        /**
     * 营业执照有效期开始时间
     */
         @TableField("BUSINESS_LICENCE_PERIOD_START")
         @NotNull(message = "营业执照有效期开始时间businessLicencePeriodStart 不能为空")
    private String businessLicencePeriodStart;

        /**
     * 营业执照有效期结束时间
     */
         @TableField("BUSINESS_LICENCE_PERIOD_END")
//         @NotNull(message = "营业执照有效期结束时间businessLicencePeriodEnd 不能为空")
    private String businessLicencePeriodEnd;

        /**
     * 注册资金
     */
         @TableField("REGISTERED_FUND")
         @NotNull(message = "注册资金registeredFund 不能为空")
    private String registeredFund;

        /**
     * 企业经营范围
     */
         @TableField("BUSINESS_SCOPE")
         @NotNull(message = "企业经营范围businessScope 不能为空")
    private String businessScope;

        /**
     * 代理商地址
     */
         @TableField("AGENT_ADDRESS")
         @NotNull(message = "代理商地址agentAddress 不能为空")
    private String agentAddress;

        /**
     * 代理平台网址
     */
         @TableField("AGENT_PLATFORM_URL")
         @NotNull(message = "代理平台网址agentPlatformUrl 不能为空")
    private String agentPlatformUrl;

        /**
     * 联系人
     */
         @TableField("CONTACT_PERSON")
         @Length(min = 2, message = "联系人contactPerson 长度至少为2位")
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
     * 更新记录
     */
         @TableField("UPDATE_RECORD")
    private String updateRecord;

    /**
     * 联系人身份证号码
     */
    @TableField("CONTACT_CERT_NUM")
    private String contactCertNum;

    /**
     * 联系人身份证有效期开始时间
     */
    @TableField("CONTACT_ID_PERIOD_START")
    @NotNull(message = "联系人身份证有效期开始时间contactIdPeriodStart 不能为空")
    private String contactIdPeriodStart;

    /**
     * 联系人身份证有效期结束时间
     */
    @TableField("CONTACT_ID_PERIOD_END")
//    @NotNull(message = "联系人身份证有效期结束时间contactIdPeriodEnd 不能为空")
    private String contactIdPeriodEnd;

    /**
     * 代理状态
     */
    @TableField("AGENT_STATUS")
    private String agentStatus;

    /**
     * 对应已审核通过正式数据的ID
     */
    @NotNull(message = "formalId不能为空",groups = ValidGroup1.class)
    @TableField("FORMAL_ID")
    private Long formalId;

}
