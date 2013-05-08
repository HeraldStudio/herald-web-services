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

import cn.edu.seu.herald.ws.api.Day;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class ClassroomDataAccessImpl extends AbstractDataAccess
        implements ClassroomDataAccess {

    private static final String GET_CLASSROOMS_UNUSED =
            "SELECT DISTINCT place FROM `herald_curriculum`.`attend` "
            + "WHERE place NOT IN ("
            + "SELECT DISTINCT place FROM `herald_curriculum`.`attend` "
            + "WHERE attend_id IN ("
            + "SELECT attend_id FROM `herald_curriculum`.`attend`"
            + "WHERE day=? AND ("
            + "(period_from<=? AND period_from>=?) OR "
            + "(period_to<=? AND period_to>=?)"
            + ")));";

    public ClassroomDataAccessImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<String> getClassroomsUnused(Day day, int from, int to) {
        Connection connection = getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    GET_CLASSROOMS_UNUSED);
            ps.setString(1, day.toString());
            ps.setInt(2, to);
            ps.setInt(3, from);
            ps.setInt(4, from);
            ps.setInt(5, to);
            ResultSet rs = ps.executeQuery();
            List<String> classrooms = new LinkedList<String>();
            while (rs.next()) {
                String place = rs.getString(1);
                classrooms.add(place);
            }
            return classrooms;
        } catch (SQLException ex) {
            closeConnection(connection);
            throw new DataAccessException(ex);
        }
    }
}