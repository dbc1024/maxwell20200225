package com.mobao360.base.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.sql.Clob;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商户中心公告
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BAS_MERCHANT_CENTER_NOTICE")
@KeySequence("SEQ_MERCHANT_CENTER_NOTICE")
public class MerchantCenterNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

        /**
     * 公告标题
     */
         @TableField("TITLE")
    private String title;

        /**
     * 公告内容
     */
         @TableField("CONTENT")
    private String content;

    @TableField("CREATE_TIME")
    private String createTime;

    @TableField("UPDATE_TIME")
    private String updateTime;


}
