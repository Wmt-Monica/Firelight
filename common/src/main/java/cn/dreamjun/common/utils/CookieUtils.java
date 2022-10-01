package cn.dreamjun.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname CookieUtils
 * @Description TODO
 * @Date 2022/9/11 10:55
 * @Created by 翊
 */
@Slf4j
public class CookieUtils {

    //1年
    public static final int default_age = 60 * 60 * 24 * 365;

    public static final String domain = "dreamjun.cn";

    /**
     * 设置cookie值
     *
     * @param response
     * @param key
     * @param value
     */
    public static void setUpCookie(HttpServletResponse response, String key, String value) {
        setUpCookie(response, key, value, default_age);
    }


    public static void setUpCookie(HttpServletResponse response, String key, String value, int age) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        /**
         * 域名：如果是单点登录，就设置顶级域名
         * sunofbeach.net
         * https://www.sunofbeach.net/
         * https://mp.sunofbeach.net/
         */
        cookie.setMaxAge(age);
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     *
     * @param response
     * @param key
     */
    public static void deleteCookie(HttpServletResponse response, String key) {
        setUpCookie(response, key, null, 0);
    }

    /**
     * 获取cookie
     *
     * @param request
     * @param key
     * @return
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.info("cookies is nuLL...");
            return null;
        }
        for (Cookie cookie : cookies) {
            if (key.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
