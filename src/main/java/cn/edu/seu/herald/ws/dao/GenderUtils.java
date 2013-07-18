package cn.edu.seu.herald.ws.dao;

import cn.edu.seu.herald.ws.api.library.GenderType;
import org.springframework.util.Assert;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
class GenderUtils {

    public static GenderType fromChinese(String v) {
        Assert.notNull(v);

        if ("男".equals(v)) {
            return GenderType.MALE;
        }
        if ("女".equals(v)) {
            return GenderType.FEMALE;
        }
        return null;
    }
}
