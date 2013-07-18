/*
 * The MIT License
 *
 * Copyright 2013 Herald Studio, Southeast University.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cn.edu.seu.herald.ws.dao;

import junit.framework.TestCase;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.*;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class CurriculumDataAccessImplTest extends TestCase {

    private BasicDataSource dataSource;
    private CurriculumDataAccess instance;

    public CurriculumDataAccessImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306"
                + "/herald_curriculum?characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        instance = new CurriculumDataAccessImpl(dataSource);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getCurriculum method, of class CurriculumDataAccessImpl.
     */
    @Test
    public void testGetCurriculum_String() {
    }

    /**
     * Test of getCurriculum method, of class CurriculumDataAccessImpl.
     */
    @Test
    public void testGetCurriculum_String_String() {
    }
}
