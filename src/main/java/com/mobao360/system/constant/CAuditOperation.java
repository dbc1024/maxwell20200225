package com.mobao360.system.constant;

/**
 * 审核操作常量定义
 * @author: CSZ 991587100@qq.com
 * @date: 2018/12/10 19:59
 */
public class CAuditOperation {

    /** 11-提交审核 */
    public final static String COMMIT_AUDIT = "11";

    /** 12-通过 */
    public final static String PASS = "12";

    /** 01-退回 */
    public final static String RETURN = "01";

    /** 02-拒绝 */
    public final static String REFUSED = "02";

    /** 03-作废 */
    public final static String SCRAP = "03";

    //以下三个操作是非审流程核操作，但需作为日志存入审核明细表中。定义在此，以便统一。
    /** 07-删除 */
    public final static String DELETE = "07";
    /** 08-新增 */
    public final static String CREATE = "09";
    /** 09-修改 */
    public final static String UPDATE = "08";
}
