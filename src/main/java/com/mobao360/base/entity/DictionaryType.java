package com.mobao360.base.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
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
@TableName("BAS_DICTIONARY_TYPE")
@KeySequence("SEQ_BAS_DICTIONARY_TYPE")
public class DictionaryType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @TableField("CODE")
    private String code;

    @TableField("NAME")
    private String name;

    @TableField("REMARK")
    private String remark;

    /**
     * 是否可修改 1-是
     */
    @TableField("IF_EDIT")
    private String ifEdit;

}
