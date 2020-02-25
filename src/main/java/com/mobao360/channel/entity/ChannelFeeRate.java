package com.mobao360.channel.entity;

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
@TableName("BAS_CHANNEL_FEE_RATE")
@KeySequence("SEQ_BAS_CHANNEL_FEE_RATE")
public class ChannelFeeRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private Long id;

    /**
     * 通道编号
     */
    @TableField("CHANNEL_CODE")
    @DictionaryCode("channel_code")
    private String channelCode;

    /**
     * 通道商户号
     */
    @TableField("CHANNEL_MERCHANT_NO")
    private String channelMerchantNo;

    /**
     * 通道商户名称
     */
    @TableField("CHANNEL_MERCHANT_NAME")
    private String channelMerchantName;

    /**
     * 产品类型
     */
    @TableField("PRODUCT_KIND")
    @DictionaryCode("product_kind")
    private String productKind;

    /**
     * 支付方式
     */
    @TableField("PAY_TYPE")
    @DictionaryCode("pay_type")
    private String payType;

    /**
     * 配置银行
     */

    @DictionaryCode("bank_code")
    @TableField("BANK_CODE")
    private String bankCode;

    /**
     * 计费方式
     */
    @TableField("FEE_TYPE")
    @DictionaryCode("fee_type")
    private String feeType;

    /**
     * 比例（计费方式为“按比例”时赋值）
     */
    @TableField("FEE_RATE")
    private String feeRate;

    /**
     * 固定金额 （计费方式为“固定值”时赋值）
     */
    @TableField("FIXED_AMOUNT")
    private String fixedAmount;

    /**
     * 保底
     */
    @TableField("MIN_FEE")
    private String minFee;

    /**
     *  正式费率id标识
     */
    @TableField("FORMAL_ID")
    private String formalId;

    /**
     * 封顶
     */
    @TableField("MAX_FEE")
    private String maxFee;

    /**
     * 生效日期
     */
    @TableField("START_DATE")
    private String startDate;

    /**
     * 失效日期
     */
    @TableField("END_DATE")
    private String endDate;

    /**
     * 状态（0-初始；1-激活；2-停用）
     */
    @TableField("STATUS")
    private String status;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

}
