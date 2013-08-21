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
package cn.edu.seu.herald.ws.dao.impl;

import cn.edu.seu.herald.ws.api.exercise.RunTime;
import cn.edu.seu.herald.ws.api.exercise.RunTimesData;
import cn.edu.seu.herald.ws.dao.AuthenticationFailure;
import cn.edu.seu.herald.ws.dao.DataAccessException;
import cn.edu.seu.herald.ws.dao.MorningExerciseDataAccess;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
@Repository("morningExerciseDataAccess")
public class MorningExerciseDataAccessImpl
        implements MorningExerciseDataAccess, ServletContextAware {

    private static final String EXERCISE_INFO_PATH =
            "/WEB-INF/morning-exercise.properties";
    private DatatypeFactory datatypeFactory;
    private File morningExerciseInfo;

    public MorningExerciseDataAccessImpl()
            throws DatatypeConfigurationException {
        datatypeFactory = DatatypeFactory.newInstance();
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        morningExerciseInfo = new File(servletContext.getRealPath(
                EXERCISE_INFO_PATH));
    }

    @Override
    public RunTimesData getRunTimesData(String username, String password)
            throws AuthenticationFailure, DataAccessException {
        MorningExerciseQuery query = new MorningExerciseQuery(
                username, password);
        try {
            query.execute();
            RunTimesData runTimesData = new RunTimesData();
            runTimesData.setTimes(BigInteger.valueOf(query.getTimes()));
            RunTime runTime = new RunTime();
            List<Date> dates = query.getLatestRecords();
            for (Date date : dates) {
                XMLGregorianCalendar calendar = parseXMLGregorianCalendar(date);
                runTime.getTimes().add(calendar);
            }
            return runTimesData;
        } catch (QueryFailure queryFailure) {
            throw new AuthenticationFailure();
        } catch (IOException ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public int getRemainDays() throws DataAccessException {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(morningExerciseInfo);
            properties.load(in);
            String format = properties.getProperty("dateformat", "yyyy/MM/dd");
            String firstDayStr = properties.getProperty("semester.firstday");
            String lastDayStr = properties.getProperty("semester.lastday");
            if (firstDayStr == null || lastDayStr == null) {
                throw new DataAccessException("The properties are not valid");
            }

            DateFormat dateFormat = new SimpleDateFormat(format);
            Date firstDay = dateFormat.parse(firstDayStr);
            Date lastDay = dateFormat.parse(lastDayStr);


            return DateUtils.workdaysBetween(firstDay, lastDay);
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
            safeClose(in);
        }
    }

    private void safeClose(InputStream in) throws DataAccessException {
        if (in == null) {
            return;
        }

        try {
            in.close();
        } catch (IOException ex) {
            throw new DataAccessException(ex);
        }
    }

    private XMLGregorianCalendar parseXMLGregorianCalendar(Date date) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return datatypeFactory.newXMLGregorianCalendar(c);
    }
}
