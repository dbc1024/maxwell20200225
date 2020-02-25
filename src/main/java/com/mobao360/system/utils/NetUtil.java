package com.mobao360.system.utils;

import com.mobao360.system.exception.MobaoException;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/3/20 10:34
 */
public class NetUtil {

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null){
            return null;
        }

        return requestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static String getHeader(String key){
        HttpServletRequest request = getRequest();
        if(request == null){
            return null;
        }

        return request.getHeader(key);
    }

    public static String getCustomerNoFromHeader(){
        HttpServletRequest request = getRequest();
        if(request == null){
            return null;
        }

        return request.getHeader("custNo");
    }


    public static String getAccessIp(){
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("ip");
        if(StringUtils.isBlank(ip)){

            ip = request.getHeader("X-Forwarded-For");
            if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
                //多次反向代理后会有多个ip值，第一个ip才是真实ip
                int index = ip.indexOf(",");
                if(index != -1){
                    return ip.substring(0,index);
                }else{
                    return ip;
                }
            }

            ip = request.getHeader("X-Real-IP");
            if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
                return ip;
            }

            return request.getRemoteAddr();
        }

        return ip;
    }


    public static String getLocalIp(){

        String localIp;
        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new MobaoException("获取本地IP异常");
        }

        return localIp;
    }

}
