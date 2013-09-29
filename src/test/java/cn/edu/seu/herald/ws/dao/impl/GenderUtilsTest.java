package cn.edu.seu.herald.ws.dao.impl;

import cn.edu.seu.herald.ws.api.library.GenderType;
import junit.framework.TestCase;

public class GenderUtilsTest extends TestCase {

    public void testFromChinese() {
        GenderType male = GenderUtils.fromChinese("男");
        assertEquals(GenderType.MALE, male);

        GenderType female = GenderUtils.fromChinese("女");
        assertEquals(GenderType.FEMALE, female);
    }
}
