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

import cn.edu.seu.herald.ws.api.curriculum.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class CurriculumDataAccessImpl
        extends AbstractDataAccess
        implements CurriculumDataAccess {

    private static final Logger LOGGER = Logger.getLogger(
            CurriculumDataAccessImpl.class.getName());
    private static final String GET_COURSES_1 =
            "SELECT * FROM `herald_curriculum`.`course` NATURAL JOIN "
            + "`herald_curriculum`.`select` "
            + "WHERE card_no=? AND term=(SELECT MAX(term) FROM "
            + "`herald_curriculum`.`select`);";
    private static final String GET_ATTENDS_1 =
            "SELECT name, day, strategy, place, period_from, period_to "
            + "FROM `herald_curriculum`.`attend` "
            + "NATURAL JOIN `herald_curriculum`.`select` "
            + "NATURAL JOIN `herald_curriculum`.`course` "
            + "WHERE select_id IN ("
            + "SELECT select_id FROM `herald_curriculum`.`select` "
            + "NATURAL JOIN `herald_curriculum`.`course` "
            + "WHERE card_no=? AND term=("
            + "SELECT MAX(term) FROM `herald_curriculum`.`select`));";
    private static final String GET_MAX_TERM =
            "SELECT MAX(term) FROM `herald_curriculum`.`select`;";
    private static final String GET_COURSES_2 =
            "SELECT * FROM `herald_curriculum`.`course` "
            + "NATURAL JOIN `herald_curriculum`.`select` "
            + "WHERE card_no=? AND term=?;";
    private static final String GET_ATTENDS_2 =
            "SELECT name, day, strategy, place, period_from, period_to "
            + "FROM `herald_curriculum`.`attend` "
            + "NATURAL JOIN `herald_curriculum`.`select` "
            + "NATURAL JOIN `herald_curriculum`.`course` "
            + "WHERE select_id IN ("
            + "SELECT select_id FROM `herald_curriculum`.`select` "
            + "NATURAL JOIN `herald_curriculum`.`course` "
            + "WHERE card_no=? AND term=?);";
    private static final String CONTAINS_CARD_NO =
            "SELECT COUNT(1) FROM `herald_curriculum`.`student` "
            + "WHERE card_no=? LIMIT 1;";
    private static final String GET_STUDENTS_OF_COURSE =
            "SELECT card_no, student_no, name "
            + "FROM `herald_curriculum`.`student` NATURAL JOIN ("
            + "SELECT card_no FROM `herald_curriculum`.`select` "
            + "WHERE course_id = ?"
            + ") AS t;";
    private static final String GET_COURSE_BY_ID =
            "SELECT course_id, name, lecturer, credit, week_from, week_to "
            + "FROM `herald_curriculum`.`course` WHERE course_id=?";
    private static final String CONTAINS_COURSE =
            "SELECT COUNT(1) FROM `herald_curriculum`.`course` "
            + "WHERE course_id=? LIMIT 1;";

    public CurriculumDataAccessImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Curriculum getCurriculum(String cardNumber)
            throws DataAccessException {
        int cardNo = Integer.valueOf(cardNumber);
        Connection connection = getConnection();
        try {
            // get courses selected
            PreparedStatement ps4Courses =
                    connection.prepareStatement(GET_COURSES_1);
            ps4Courses.setInt(1, cardNo);
            ResultSet rs1 = ps4Courses.executeQuery();
            List<Course> courseList = getCoursesFromResultSet(rs1);
            ps4Courses.close();

            // get schedules
            PreparedStatement ps4Schedules =
                    connection.prepareStatement(GET_ATTENDS_1);
            ps4Schedules.setInt(1, cardNo);
            ResultSet rs2 = ps4Schedules.executeQuery();
            TimeTable timeTable = getTimeTableFromResultSet(rs2);

            // get max term
            PreparedStatement ps4MaxTerm =
                    connection.prepareStatement(GET_MAX_TERM);
            ResultSet rs3 = ps4MaxTerm.executeQuery();
            String term = getMaxTermFromResultSet(rs3);

            Curriculum curr = new Curriculum();
            curr.setCardNumber(cardNumber);
            curr.setTerm(term);
            Courses courses = new Courses();
            courses.setCourses(courseList);
            curr.setCourses(courses);
            curr.setTimeTable(timeTable);
            return curr;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public Curriculum getCurriculum(String cardNumber, String term)
            throws DataAccessException {
        int cardNo = Integer.valueOf(cardNumber);
        Connection connection = getConnection();
        try {
            // get courses selected
            PreparedStatement ps4Courses =
                    connection.prepareStatement(GET_COURSES_2);
            ps4Courses.setInt(1, cardNo);
            ps4Courses.setString(2, term);
            ResultSet rs1 = ps4Courses.executeQuery();
            List<Course> courseList = getCoursesFromResultSet(rs1);
            ps4Courses.close();

            // get schedules
            PreparedStatement ps4Schedules =
                    connection.prepareStatement(GET_ATTENDS_2);
            ps4Schedules.setInt(1, cardNo);
            ps4Schedules.setString(2, term);
            ResultSet rs2 = ps4Schedules.executeQuery();
            TimeTable timeTable = getTimeTableFromResultSet(rs2);

            Curriculum curr = new Curriculum();
            curr.setCardNumber(cardNumber);
            curr.setTerm(term);
            Courses courses = new Courses();
            courses.setCourses(courseList);
            curr.setTimeTable(timeTable);
            return curr;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public boolean containsCourse(int courseId) throws DataAccessException {
        Connection connection = getConnection();
        try {
            PreparedStatement ps =
                    connection.prepareStatement(CONTAINS_COURSE);
            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new DataAccessException();
            }
            int count = rs.getInt(1);
            return (count >= 1);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public Course getCourseById(int courseId)
            throws DataAccessException {
        Connection connection = getConnection();
        try {
            PreparedStatement ps1 =
                    connection.prepareStatement(GET_STUDENTS_OF_COURSE);
            ps1.setInt(1, courseId);
            ResultSet rs1 = ps1.executeQuery();
            StudentList studentList = getStudentListFromResultSet(rs1);

            PreparedStatement ps2 =
                    connection.prepareStatement(GET_COURSE_BY_ID);
            ps2.setInt(1, courseId);
            ResultSet rs2 = ps2.executeQuery();
            if (!rs2.next()) {
                throw new DataAccessException();
            }

            Course course = getCourseFromResultSet(rs2);
            course.setStudents(studentList);
            return course;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public boolean containsStudent(String cardNumber) throws DataAccessException {
        Connection connection = getConnection();
        try {
            PreparedStatement ps =
                    connection.prepareStatement(CONTAINS_CARD_NO);
            ps.setString(1, cardNumber);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new DataAccessException();
            }
            int count = rs.getInt(1);
            return (count >= 1);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    private Course getCourseFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("course_id");
        String courseName = rs.getString("name");
        String lecturer = rs.getString("lecturer");
        double credit = rs.getDouble("credit");
        int weekFrom = rs.getInt("week_from");
        int weekTo = rs.getInt("week_to");
        Period p = new Period();
        p.setFrom(BigInteger.valueOf(weekFrom));
        p.setTo(BigInteger.valueOf(weekTo));
        Course course = new Course();
        course.setId(id);
        course.setName(courseName);
        course.setCredit(BigDecimal.valueOf(credit));
        course.setLecturer(lecturer);
        course.setWeek(p);
        return course;
    }

    private List<Course> getCoursesFromResultSet(ResultSet rs)
            throws SQLException {
        List<Course> courses = new LinkedList<Course>();
        while (rs.next()) {
            Course course = getCourseFromResultSet(rs);
            courses.add(course);
        }
        return courses;
    }

    private Attendance getAttendanceFromResultSet(ResultSet rs)
            throws SQLException {
        String courseName = rs.getString("name");
        Day day = Day.valueOf(rs.getString("day"));
        String place = rs.getString("place");
        Strategy str = Strategy.valueOf(rs.getString("strategy"));
        int from = rs.getInt("period_from");
        int to = rs.getInt("period_to");
        Period p = new Period();
        p.setFrom(BigInteger.valueOf(from));
        p.setTo(BigInteger.valueOf(to));
        Attendance att = new Attendance();
        att.setCourseName(courseName);
        att.setPeriod(p);
        att.setPlace(place);
        att.setStrategy(str);
        return att;
    }

    private TimeTable getTimeTableFromResultSet(ResultSet rs)
            throws SQLException {
        Map<Day, List<Attendance>> atts = new HashMap<Day, List<Attendance>>();
        while (rs.next()) {
            Day day = Day.valueOf(rs.getString("day"));
            if (!atts.containsKey(day)) {
                atts.put(day, new LinkedList<Attendance>());
            }
            atts.get(day).add(getAttendanceFromResultSet(rs));
        }

        List<Schedule> schLst = new LinkedList<Schedule>();
        for (Entry<Day, List<Attendance>> ent : atts.entrySet()) {
            Day day = ent.getKey();
            List<Attendance> attLst = ent.getValue();
            Schedule sch = new Schedule();
            sch.setAttendances(attLst);
            sch.setDay(day);
            schLst.add(sch);
        }

        TimeTable tt = new TimeTable();
        tt.setSchedules(schLst);
        return tt;
    }

    private String getMaxTermFromResultSet(ResultSet rs)
            throws SQLException, DataAccessException {
        if (!rs.next()) {
            throw new DataAccessException();
        }
        String term = rs.getString(1);
        return term;
    }

    private StudentList getStudentListFromResultSet(ResultSet rs)
            throws SQLException, DataAccessException {
        StudentList studentList = new StudentList();
        while (rs.next()) {
            studentList.getStudents().add(getStudentFromResultSet(rs));
        }
        return studentList;
    }

    private Student getStudentFromResultSet(ResultSet rs)
            throws SQLException, DataAccessException {
        String cardNo = rs.getString("card_no");
        String studentNo = rs.getString("student_no");
        String name = rs.getString("name");
        Student student = new Student();
        student.setCardNumber(cardNo);
        student.setStudentNumber(studentNo);
        student.setName(name);
        return student;
    }

}
