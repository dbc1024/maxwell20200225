package com.mobao360.base.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(description="数据字典实体")
@TableName("BAS_DICTIONARY")
@KeySequence("SEQ_BAS_DICTIONARY")
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    @ApiModelProperty(value="数据字典类型ID",example="12")
    @NotNull(message = "数据字典类型ID不能为空")
    @TableField("DIC_TYPE_ID")
    private Long dicTypeId;

    @TableField("VALUE")
    private String value;

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
