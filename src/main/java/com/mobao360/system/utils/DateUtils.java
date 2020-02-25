package com.mobao360.system.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * 日期处理工具类。
 *
 * 备注：建议大部分日期处理直接使用hutool的DateUtil, api非常全面, 可自行查阅。
 *      部分特殊的简化处理，可封装到此类中。
 *
 * @author: CSZ 991587100@qq.com
 * @date: 2018/12/26 15:03
 */
public class DateUtils {

	public final static String DATE_PATTERN = "yyyy-MM-dd";
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


    /**
     * 日期【天】加减
     * @param date 字符串 yyyy-MM-dd
     * @param offset 正数：加日期；负数：减日期
     * @return
     */
    public static String offsetDay(String date, int offset) {

        DateTime dateTime = DateUtil.offsetDay(DateUtil.parseDate(date), offset);

        return DateUtil.formatDate(dateTime);
    }


    /**
     * 比较两个字符串类型日期时间的大小
     * yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss 都可以
     *
     * 若: dateTime1 > dateTime2, 返回 1
     * 若: dateTime1 = dateTime2, 返回 0
     * 若: dateTime1 < dateTime2, 返回 -1
     *
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static int compare(String dateTime1, String dateTime2) {

        int i = dateTime1.compareTo(dateTime2);
        if(i<0){
            i = -1;
        }else if (i>0){
            i = 1;
        }

        return i;
    }


    /**
     * 得到本周第一天
     * @param date
     * @return
     */
    public static String beginOfWeek(Date date) {

        DateTime beginOfWeek = DateUtil.beginOfWeek(date);

        return DateUtil.formatDate(beginOfWeek);
    }


    /**
     * 得到本月第一天
     * @param date
     * @return
     */
    public static String beginOfMonth(Date date) {

        DateTime beginOfMonth = DateUtil.beginOfMonth(date);

        return DateUtil.formatDate(beginOfMonth);
    }

}
