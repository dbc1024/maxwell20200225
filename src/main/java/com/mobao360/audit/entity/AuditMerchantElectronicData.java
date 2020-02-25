package com.mobao360.audit.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mobao360.system.utils.valid.ValidGroup2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
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
@TableName("AUDIT_MERCHANT_ELECTRONIC_DATA")
@KeySequence("SEQ_AUDIT_M_E_D")
public class AuditMerchantElectronicData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private Long id;

    @TableField("AUDIT_EVENT_ID")
    @NotNull(message = "审核事件id auditEventId  不能为空",groups = ValidGroup2.class)
    private Long auditEventId;

    /**
     * 客户号
     */
    @NotNull(message = "customerNo不能为空")
    @TableField("CUSTOMER_NO")
    private String customerNo;

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
    private String stockChannelCertify;

    /**
     * 收款银行账户证明
     */
    @TableField("DUE_BANK_ACCOUNT_CERTIFY ")
    private String dueBankAccountCertify;

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
     * 修改记录
     */
    @TableField("UPDATE_RECORD")
    private String updateRecord;

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

    /** 非数据库字段(用于补充电子资料时，传入明细描述)*/
    @TableField(exist = false)
    private String description;
}
