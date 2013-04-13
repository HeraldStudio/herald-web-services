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
import cn.edu.seu.herald.ws.api.Period;
import cn.edu.seu.herald.ws.api.Schedule;
import cn.edu.seu.herald.ws.api.StrategyType;
import cn.edu.seu.herald.ws.api.TimeTable;
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

    public CurriculumDataAccessImpl(DataSource dataSource) {
        super(dataSource);
    }

    private static final String GET_COURSES =
            "SELECT * FROM `course` WHERE course_id IN ("
            + "SELECT course_id FROM `select` WHERE card_no=? "
            + "AND term=(SELECT MAX(term) FROM `select`));";
    private static final String GET_ATTENDS =
            "SELECT name, day, strategy, place, period_from, period_to "
            + "FROM `attend` NATURAL JOIN `select` NATURAL JOIN `course` "
            + "WHERE select_id IN ("
            + "SELECT select_id FROM `select` NATURAL JOIN `course` "
            + "WHERE card_no=? AND term=("
            + "SELECT MAX(term) FROM `select`));";
    private static final String GET_MAX_TERM =
            "SELECT MAX(term) FROM `select`;";
    @Override
    public Curriculum getCurriculum(String cardNumber)
            throws DataAccessException {
        int cardNo = Integer.valueOf(cardNumber);
        Connection connection = getConnection();
        try {
            // get courses selected
            PreparedStatement ps4Courses =
                    connection.prepareStatement(GET_COURSES);
            ps4Courses.setInt(1, cardNo);
            ResultSet rs1 = ps4Courses.executeQuery();
            List<Course> courses = getCoursesFromResultSet(rs1);
            ps4Courses.close();

            // get schedules
            PreparedStatement ps4Schedules =
                    connection.prepareStatement(GET_ATTENDS);
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
            curr.setCourses(courses);
            curr.setTimeTable(timeTable);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Curriculum getCurriculum(String cardNumber, String term)
            throws DataAccessException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Course getCourseFromResultSet(ResultSet rs) throws SQLException {
        String courseName = rs.getString("course_name");
        String lecturer = rs.getString("lecturer");
        double credit = rs.getDouble("credit");
        int weekFrom = rs.getInt("week_from");
        int weekTo = rs.getInt("week_to");
        Period p = new Period(weekFrom, weekTo);
        Course course = new Course();
        course.setName(courseName);
        course.setCredit(credit);
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
        StrategyType str = StrategyType.valueOf(rs.getString("strategy"));
        int from = rs.getInt("period_from");
        int to = rs.getInt("period_to");
        Period p = new Period(from, to);
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
}
