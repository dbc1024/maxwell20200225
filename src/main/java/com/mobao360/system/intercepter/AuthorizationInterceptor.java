package com.mobao360.system.intercepter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 权限(Token)验证
 * @author yhq
 * @email
 * @date 20180726
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

//    @Autowired
//    private AuthManager authManager;
//    @Autowired
//    private MoboIntercerptConstant moboIntercerptConstant;
//
//    public static final String USER_KEY = "userInfo";
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //是否开启登录验证
//        if(!moboIntercerptConstant.getFlag()){
//            return true;
//        }
//        IgnoreCheck annotation;
//        if(handler instanceof HandlerMethod) {
//            annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreCheck.class);
//        }else{
//            return true;
//        }
//        //放有IGNORECHECK标签的直接通过
//        if(annotation != null){
//            return true;
//        }
//        String header = authManager.getJwtUtils().getHeader();
//        //获取用户凭证
//        String token = request.getHeader(header);
//        if(StringUtils.isBlank(token)){
//            token = request.getParameter(header);
//        }
//
//        //凭证为空
//        if(StringUtils.isBlank(token)){
//            throw new MobaoException("用户未登陆或登陆过期,请重新登陆！", "xxxx");
//        }
//
//        SysUserEntity userT = authManager.getUserInfo();
//        if(userT == null){
//            throw new MobaoException("用户未登陆或登陆过期,请重新登陆！", "xxxx");
//        }
//        authManager.refreshUserInfo();
//        //设置userId到request里，后续根据userId，获取用户信息
//        request.setAttribute(USER_KEY,userT);
//        return true;
//    }
}