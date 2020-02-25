package com.mobao360.system.utils;

import com.mobao360.api.feign.UserFeign;
import com.mobao360.system.constant.Constants;
import com.mobao360.system.exception.MobaoException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取当前用户信息工具类
 * @author: CSZ 991587100@qq.com
 * @date: 2019/2/13 16:04
 */
@Component
public class LoginUserInfoUtil {

    private static LoginUserInfoUtil util;
    @Autowired
    private UserFeign userFeign;

    @PostConstruct
    public void init(){
        util = this;
        util.userFeign = this.userFeign;
    }


    /**
     * 用户权限系统反映
     * username不是用户名，是用户账号
     * name是用户名
     * @return
     */
    public static String getUsername(){

        String username = NetUtil.getHeader("username");
        if(StringUtils.isBlank(username)){
            throw new MobaoException("Header中获取[username]为空");
        }

        return username;
    }

    
    public static String getUserId(){

        String userId = NetUtil.getHeader("userId");
        if(StringUtils.isBlank(userId)){
            throw new MobaoException("Header中获取[userId]为空");
        }

        return userId;
    }


    public static List<String> getUserRoles(){

        List<String> roles = new ArrayList<>();

        Result<List<HashMap<String, String>>> result;
        try {
            result = util.userFeign.getUserRoles(getUserId());
        }catch (Exception e){
            throw new MobaoException("用户权限服务调用失败", e);
        }

        if(!Constants.YES.equals(result.getRetCode())){
            throw new MobaoException("获取用户角色,用户权限服务返回异常："+ result.getRetMsg());
        }

        List<HashMap<String, String>> roleList = result.getRetData();

        for (HashMap<String, String> role : roleList) {
            String roleCode = role.get("code");
            roles.add(roleCode);
        }

        if(roles.size()<1){
            throw new MobaoException("获取用户角色,用户权限系统返回角色[code]为空");
        }

        return roles;
    }


    /**
     * 根据用户账号集合，获取用户名集合
     * @param userAccounts
     * @return
     */
    public static Map<String, String> getNamesByAccount(List<String> userAccounts){

        Result<HashMap<String, String>> result;
        try {
            result = util.userFeign.getNamesByAccount(userAccounts);
        }catch (Exception e){
            throw new MobaoException("用户权限服务调用失败", e);
        }

        if(!Constants.YES.equals(result.getRetCode())){
            throw new MobaoException("获取用户名,用户权限服务返回异常："+ result.getRetMsg());
        }

        return result.getRetData();
    }


}
