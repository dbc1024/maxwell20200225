package com.mobao360.customer.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

/**
 * 销售人员信息
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BAS_SALESMAN")
@KeySequence("SEQ_BAS_SALESMAN")
public class Salesman implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

    /** 销售编号*/
    @Length(min = 4,max = 4,message = "销售编号长度必须4位")
    @TableField("SALES_NO")
    private String salesNo;

    @TableField("NAME")
    private String name;

    @Email
    @TableField("EMAIL")
    private String email;

    @TableField("TEL")
    private String tel;


}
