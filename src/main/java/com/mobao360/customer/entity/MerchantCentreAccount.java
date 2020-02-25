package com.mobao360.customer.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2018-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("BAS_MERCHANT_CENTRE_ACCOUNT")
@KeySequence("SEQ_M_C_A")
public class MerchantCentreAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private Long id;

        /**
     * 客户号
     */
         @TableField("CUSTOMER_NO")
    private String customerNo;

        /**
     * 商户管理员邮箱
     */
         @TableField("MANAGER_EMAIL")
    private String managerEmail;

        /**
     * 商户管理员绑定手机号
     */
         @TableField("MANAGER_TEL")
    private String managerTel;

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


    /** 向商户中心推送账号标识：0-未推送成功，1-已推送成功 */
    @TableField("TASK1")
    private String task1;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getManagerTel() {
        return managerTel;
    }

    public void setManagerTel(String managerTel) {
        this.managerTel = managerTel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTask1() {
        return task1;
    }

    public void setTask1(String task1) {
        this.task1 = task1;
    }
}
