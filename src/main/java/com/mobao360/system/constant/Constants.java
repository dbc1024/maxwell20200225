package com.mobao360.system.constant;
/**
 * 定义一些不便于分类的公共常量
 * @author yanghongquan
 * @email 842592135@qq.com
 * @date 2018/10/11 14:32
 */
public class Constants {

    /** 服务名 */
    public final static String SERVER_NAME = "maxwell";

    /** 请求响应失败默认提示信息 */
    public final static String DEFAULT_FAIL_MSG = "未知异常，请联系管理员.";

    /** 请求响应成功默认提示信息 */
    public final static String DEFAULT_SUCCESS_MSG = "success";

    /** 新生成审核事件时，应位于初始节点，所有事件初始节点编码约定为1000 */
    public final static String AUDIT_EVENT_BEGIN_NODE_CODE = "1000";

    /** 审核事件第二个固定节点2000 */
    public final static String AUDIT_EVENT_SECOND_NODE_CODE = "2000";

    /** 审核事件第二个固定节点3000 */
    public final static String AUDIT_EVENT_THIRD_NODE_CODE = "3000";

    /** 系统中所有二值性字段肯定含义 */
    public final static String YES = "1";

    /** 系统中所有二值性字段否定含义 */
    public final static String NO = "0";

}
