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

import cn.edu.seu.herald.ws.api.Course;
import cn.edu.seu.herald.ws.api.Curriculum;
import cn.edu.seu.herald.ws.api.Period;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
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
    private static final String GET_ATTENDS = null;
    @Override
    public Curriculum getCurriculum(String cardNumber)
            throws DataAccessException {
        int cardNo = Integer.valueOf(cardNumber);
        Connection connection = getConnection();
        try {
            PreparedStatement ps4Courses =
                    connection.prepareStatement(GET_COURSES);
            ps4Courses.setInt(1, cardNo);
            ResultSet rs = ps4Courses.executeQuery();
            List<Course> courses = getCoursesFromResultSet(rs);
            ps4Courses.close();

            PreparedStatement ps4Schedules =
                    connection.prepareStatement(GET_ATTENDS);
            // TODO get schdules
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
}
