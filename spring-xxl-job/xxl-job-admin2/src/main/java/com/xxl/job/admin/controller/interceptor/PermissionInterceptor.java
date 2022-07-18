package com.xxl.job.admin.controller.interceptor;

import com.xxl.job.admin.controller.anotation.PermissionLimit;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName PermissionInterceptor
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/17 22:21
 * @Version 1.0
 **/
@Component
public class PermissionInterceptor implements AsyncHandlerInterceptor {
    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }

        boolean needLogin = true;
        boolean needAdminuser = false;
        HandlerMethod method = (HandlerMethod) handler;
        PermissionLimit permissionLimit = method.getMethodAnnotation(PermissionLimit.class);
        if (permissionLimit != null){
            needLogin = permissionLimit.limit();
            needAdminuser = permissionLimit.adminuser();
        }
        if (needLogin){
            XxlJobUser loginUser = loginService.ifLogin(request,response);
            if (loginUser == null){
                response.setStatus(302);
                response.setHeader("Location",request.getContextPath()+"/toLogin");
                return false;
            }
            if (needAdminuser && loginUser.getRole()!=1){
                throw new RuntimeException(I18nUtil.getString("system_premission_limit"));
            }
            request.setAttribute(LoginService.LOGIN_IDENTITY_KEY,loginUser);
        }
        return true;
    }
}
