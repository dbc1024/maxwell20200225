package com.mobao360.audit.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.mobao360.system.annotation.DictionaryCode;
import com.mobao360.system.utils.valid.ValidGroup2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 商户结算信息
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2018-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("AUDIT_MERCHANT_SETTLEMENT_INFO")
@KeySequence("SEQ_AUDIT_SETTLEMENT_INFO")
public class AuditMerchantSettlementInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @NotNull(message = "审核事件id auditEventId不能为空",groups = ValidGroup2.class)
    @TableField("AUDIT_EVENT_ID")
    private Long auditEventId;

    /**
     * 客户号（商户1开头，个人5开头）
     */
    @TableField("CUSTOMER_NO")
    @NotBlank(message = "客户号 customerNo 不能为空")
    private String customerNo;

    /**
     * 开户户名
     */
    @TableField("BANK_ACCOUNT_NAME")
//    @NotBlank(message = "开户户名必填")
//    @Length(min = 2, message = "开户户名 bankAccountName 长度至少为2位")
    private String bankAccountName;

    /**
     * 收款人身份证号
     */
    @TableField("PAYEE_ID_NUM")
//    @NotBlank(message = "收款人身份证号必填")
//    @Length(min = 18, max = 18, message = "收款人身份证号 payeeIdNum 长度只能为18位")
    private String payeeIdNum;

    /**
     * 开户账号
     */
    @TableField("ACCOUNT_NO")
//    @NotBlank(message = "开户账号 accountNo 不能为空")
//    @Length(min = 10, message = "开户户名 accountNo 长度至少为10位")
    private String accountNo;

    /**
     * 银行预留手机号
     */
    @TableField("RESERVE_MOBILE_NO")
//    @NotBlank(message = "银行预留手机号不能为空")
//    @Length(min = 11, max = 11, message = "银行预留手机号长度必须为11位")
    private String reserveMobileNo;

    /**
     * 账户类型（1：对私 2：对公）
     */
    @TableField("ACCOUNT_TYPE")
//    @Length(min = 1,max = 1,message = "账户类型 accountType 参数长度只能为1")
    @DictionaryCode("account_type")
    private String accountType;



    /**
     * 开户银行名称
     */
    @TableField("BANK_NAME")
//    @NotNull(message = "开户银行名称 bankName 不能为空")
    private String bankName;

    /**
     * 开户银行行号
     */
    @TableField("BANK_NO")
//    @NotNull(message = "开户银行名称 bankNO 不能为空")
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
//    @NotNull(message = "省代码 provinceCode 不能为空")
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
//    @NotNull(message = "市代码 cityCode 不能为空")
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
//    @NotNull(message = "分支行行号 branchBankNo 不能为空")
    private String branchBankNo;

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
