package com.mobao360.system.utils;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/3/14 11:04
 */
public class StringUtil {

    /** 下划线 */
    public static final char UNDERLINE = '_';

    /**
     * 驼峰转下划线，如：userId --> user_id
     * @param param
     * @return
     */
    public static String camelToUnderline(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }

        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)){
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }

        return sb.toString();
    }


    /**
     * 下划线转驼峰，如：user_id --> userId
     * @param param
     * @return
     */
    public static String underlineToCamel(String param){
        if (param==null || "".equals(param.trim())){
            return "";
        }

        int len=param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE){
                if (++i < len){
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            }else{
                sb.append(c);
            }
        }

        return sb.toString();
    }

}
