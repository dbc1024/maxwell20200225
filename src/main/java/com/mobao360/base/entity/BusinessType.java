package com.mobao360.base.entity;

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
 * @since 2018-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BAS_BUSINESS_TYPE")
@KeySequence("SEQ_BAS_BUSINESS_TYPE")
public class BusinessType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("BUSINESS_TYPE")
    @DictionaryCode("business_type")
    private String businessType;

    @TableField("CODE")
    private String code;

    @TableField("NAME")
    private String name;

    @TableField("REMARK")
    private String remark;

}
