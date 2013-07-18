package cn.edu.seu.herald.ws.dao;

import org.springframework.util.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
class CookieUtils {

    private static final String COOKIE_REGEX =
            "([^=]+)=([^\\;]*);?\\s?";

    public static String getCookieValue(String cookieStr, String name) {
        Assert.notNull(cookieStr);
        Assert.notNull(name);

        Pattern cookiePattern = Pattern.compile(COOKIE_REGEX);
        Matcher matcher = cookiePattern.matcher(cookieStr);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            if (name.equals(key)) {
                return value;
            }
        }
        return null;
    }
}
