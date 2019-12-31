package com.thankjava.wchat.util;

import com.thankjava.wchat.consts.CookieName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    static Logger logger = LoggerFactory.getLogger(Utils.class);

    /**
     * 从cookie字符串中解析出指定的cookie值
     *
     * @param cookieStr
     * @return
     */
    public static String getValueForCookieStr(String cookieStr, CookieName cookieName) {
        logger.debug("cookieStr = [" + cookieStr + "] get cookieName = " + cookieName.code);
        String[] cookies = cookieStr.split("; ");
        Map<String, String> cookieKV = new HashMap<>();
        String[] tmp;
        for (String cookie : cookies) {
            tmp = cookie.trim().split("=");
            if (tmp == null || tmp.length == 0) {
                return null;
            }
            cookieKV.put(tmp[0].trim(), tmp[1].trim());
        }
        return cookieKV.get(cookieName.code);
    }
}
