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

import cn.edu.seu.herald.ws.api.Attendance;
import cn.edu.seu.herald.ws.api.Course;
import cn.edu.seu.herald.ws.api.Curriculum;
import cn.edu.seu.herald.ws.api.Day;
import cn.edu.seu.herald.ws.api.Schedule;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class CurriculumDataAccessImplTest {

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
        System.out.println("getCurriculum");
        String cardNumber = "213100434";
        String lastestTerm = "12-13-3";
        Curriculum result = instance.getCurriculum(cardNumber);
        testGetCurriculum(result, cardNumber, lastestTerm);
    }

    /**
     * Test of getCurriculum method, of class CurriculumDataAccessImpl.
     */
    @Test
    public void testGetCurriculum_String_String() {
        System.out.println("getCurriculum");
        String cardNumber = "213100434";
        String term = "12-13-3";
        Curriculum result = instance.getCurriculum(cardNumber, term);
        testGetCurriculum(result, cardNumber, term);
    }

    private void testGetCurriculum(Curriculum result,
            String expCardNumber, String expTerm) {
        System.out.println("getCurriculum");
        assertCurriculumInfo(result, expCardNumber, expTerm);
        List<Course> courses = result.getCourses();
        assertContainsCourses(courses, "IT新技术讲座", "Web技术及其应用", "XML技术");
        List<Schedule> schLst = result.getTimeTable().getSchedules();
        Map<Day, List<String>> containsAttCourse =
                new HashMap<Day, List<String>>();
        List<String> courseList = new LinkedList<String>();
        courseList.add("XML技术");
        courseList.add("Web技术及其应用");
        containsAttCourse.put(Day.MON, courseList);
        assertContainsAttendances(schLst, containsAttCourse);
    }

    private void assertCurriculumInfo(Curriculum curr,
            String expCardNumber, String expTerm) {
        String cardNumber = curr.getCardNumber();
        assertEquals(expCardNumber, cardNumber);
        String term = curr.getTerm();
        assertEquals(expTerm, term);
    }

    private void assertContainsCourses(List<Course> courses,
            String ...courseNames) {
        Set<String> courseNameSet = new HashSet<String>();
        for (Course course : courses) {
            String courseName = course.getName();
            courseNameSet.add(courseName);
        }

        for (String courseName : courseNames) {
            if (!courseNameSet.contains(courseName)) {
                fail(String.format("Doesn\'t contain course with name: %s",
                        courseName));
            }
        }
    }

    private void assertContainsAttendances(List<Schedule> schLst,
            Map<Day, List<String>> containsAttCourse) {
        Map<Day, Set<String>> attMap =
                new HashMap<Day, Set<String>>();
        for (Schedule sch : schLst) {
            Day day = sch.getDay();
            if (!attMap.containsKey(day)) {
                attMap.put(day, new HashSet<String>());
            }
            Set<String> attLst = attMap.get(day);
            for (Attendance att : sch.getAttendances()) {
                String courseName = att.getCourseName();
                attLst.add(courseName);
            }
        }

        for (Entry<Day, List<String>> ent : containsAttCourse.entrySet()) {
            Day day = ent.getKey();
            List<String> courseNames = ent.getValue();
            for (String courseName : courseNames) {
                Set<String> courseNameSet = attMap.get(day);
                if (!courseNameSet.contains(courseName)) {
                    fail(String.format(
                            "Doesn\'t contain attendance with course name: %s",
                            courseName));
                }
            }
        }
    }
}
