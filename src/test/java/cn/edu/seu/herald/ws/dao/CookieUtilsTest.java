package cn.edu.seu.herald.ws.dao;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
public class CookieUtilsTest extends TestCase {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    public void testGetCookieValue() {
        String value = CookieUtils.getCookieValue(
                "PHPSESSID=ps05t8l4d801d48pak178vmv33; path=/", "PHPSESSID");
        assertEquals("ps05t8l4d801d48pak178vmv33", value);
    }
}
