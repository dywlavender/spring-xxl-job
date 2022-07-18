package com.xxl.job.admin.controller.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @ClassName CookieInterceptor
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/17 22:33
 * @Version 1.0
 **/
@Component
public class CookieInterceptor implements AsyncHandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView!=null && request.getCookies()!=null && request.getCookies().length>0){
            HashMap<String, Cookie> cookieMap = new HashMap<>();
            for (Cookie ck: request.getCookies()){
                cookieMap.put(ck.getName(),ck);
            }
            modelAndView.addObject("cookieMap",cookieMap);
        }
        if (modelAndView!=null){
            modelAndView.addObject("I18nUtil",FtUtil.generateStaticModel(I18nUTil.class.getName()));
        }
    }
}
