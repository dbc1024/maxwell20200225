package com.mobao360.system.constant;

/**
 * 审核事件类型常量定义
 * @author: CSZ 991587100@qq.com
 * @date: 2018/12/8 16:51
 */
public class CAuditEventType {

    /** 商户入网 */
    public final static String MERCHANT_ACCESS_NETWORK = "01";

    /** 商户锁定 */
    public final static String MERCHANT_LOCK = "02";

    /** 商户解锁 */
    public final static String MERCHANT_UNLOCK = "03";

    /** 商户注销 */
    public final static String MERCHANT_LOGOUT = "04";

    /** 商户基本信息修改 */
    public final static String MERCHANT_INFO_UPDATE = "05";

    /** 商户配置修改 */
    public final static String MERCHANT_CONFIG_UPDATE = "06";

    /** 商户交易关闭 */
    public final static String MERCHANT_TRADE_CLOSE = "07";

    /** 商户结算关闭 */
    public final static String MERCHANT_SETTLEMENT_CLOSE = "08";

    /** 商户指定路由变更 */
    public final static String MERCHANT_ROUTE_CHANGE = "09";

    /** 商户费率变更 */
    public final static String MERCHANT_FEERATE_CHANGE = "10";

    /** 商户结算信息修改 */
    public final static String MERCHANT_SETTLEMENT_UPDATE = "11";

    /** 商户垫资结算变更 */
    public final static String MERCHANT_ADVANCE_SETTLEMENT_CHANGE = "12";

    /** 代理商资料变更 */
    public final static String AGENT_CHANGE = "13";

    /** 商户秘钥修改 */
    public final static String MERCHANT_SECRET_KEY_UPDATE = "14";

    /** 商户中心账号修改 */
    public final static String MERCHANT_CENTRE_ACCOUNT_UPDATE = "15";

    /** 商户电子资料变更 */
    public final static String MERCHANT_ELECTRONIC_DATA_CHANGE = "33";

    /** 支付账户锁定 */
    public final static String MERCHANT_ACCOUNT_LOCK = "25";

    /** 支付账户解锁 */
    public final static String MERCHANT_ACCOUNT_UNLOCK = "26";

    /** 支付账户注销 */
    public final static String MERCHANT_ACCOUNT_LOGOUT = "27";

    /** 个人锁定 */
    public final static String PERSONAL_LOCK = "28";

    /** 个人解锁 */
    public final static String PERSONAL_UNLOCK = "29";

    /** 个人注销 */
    public final static String PERSONAL_LOGOUT = "30";

}
