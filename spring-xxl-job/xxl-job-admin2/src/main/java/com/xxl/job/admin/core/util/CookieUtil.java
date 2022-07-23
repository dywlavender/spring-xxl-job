package com.xxl.job.admin.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName CookieUtil
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/21 17:06
 * @Version 1.0
 **/
public class CookieUtil {
    private static final int COOKIE_MAX_AVG = Integer.MAX_VALUE;
    private static final String COOKIE_PATH = "/";

    public static void set(HttpServletResponse response, String key, String value,boolean ifRemember){
        int age = ifRemember?COOKIE_MAX_AVG:-1;
        set(response,key,value,null,COOKIE_PATH,age,true);
    }

    private static void set(HttpServletResponse response,String key,String value,String domain,String path,int maxAge,boolean isHttpOnly){
        Cookie cookie = new Cookie(key,value);
        if (domain != null){
            cookie.setDomain(domain);
        }

        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }

    public static String getValue(HttpServletRequest request,String key){
        Cookie cookie = get(request, key);
        if (cookie != null){
            return cookie.getValue();
        }
        return null;
    }
    private static Cookie get(HttpServletRequest request,String key){
        Cookie[] arr_cookie = request.getCookies();
        if (arr_cookie != null && arr_cookie.length>0){
            for (Cookie cookie:arr_cookie){
                if (cookie.getName().equals(key)){
                    return cookie;
                }
            }
        }
        return null;
    }

    public static void remove(HttpServletRequest request,HttpServletResponse response,String key){
        Cookie cookie = get(request,key);
        if (cookie!=null){
            set(response,key,"",null,COOKIE_PATH,0,true);
        }
    }

}
