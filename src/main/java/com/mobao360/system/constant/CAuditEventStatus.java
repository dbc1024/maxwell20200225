package com.mobao360.system.constant;

/**
 * 审核事件状态常量定义
 * @author: CSZ 991587100@qq.com
 * @date: 2018/12/8 17:06
 */
public class CAuditEventStatus {

    /** 草稿 */
    public final static String DRAFT = "01";

    /** 审核中 */
    public final static String AUDITING = "02";

    /** 作废 */
    public final static String SCRAP = "97";

    /** 审核拒绝 */
    public final static String REFUSED = "98";

    /** 审核通过 */
    public final static String PASS = "99";

    /** 删除(逻辑删除) */
    public final static String DELETE = "44";


}
