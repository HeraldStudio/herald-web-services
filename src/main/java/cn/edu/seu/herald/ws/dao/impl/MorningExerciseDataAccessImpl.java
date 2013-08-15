package cn.edu.seu.herald.ws.dao.impl;

import cn.edu.seu.herald.ws.api.exercise.RunTime;
import cn.edu.seu.herald.ws.api.exercise.RunTimesData;
import cn.edu.seu.herald.ws.dao.AuthenticationFailure;
import cn.edu.seu.herald.ws.dao.DataAccessException;
import cn.edu.seu.herald.ws.dao.MorningExerciseDataAccess;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
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
import java.util.*;

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


            return workdaysBetween(firstDay, lastDay);
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
            safeClose(in);
        }
    }

    private int workdaysBetween(Date from, Date to) {
        Assert.notNull(from);
        Assert.notNull(to);

        if (from.after(to)) {
            return workdaysBetween(to, from);
        }

        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(from);
        Calendar toCal = Calendar.getInstance();
        toCal.setTime(to);

        int fromDay = fromCal.get(Calendar.DAY_OF_WEEK);
        int toDay = toCal.get(Calendar.DAY_OF_WEEK);

        // TODO calculate working days
        return 0;
    }

    private int daysBetween(long from, long to) {
        return (int) (
                (to - from) / (1000 * 60 * 60 * 24)
        );
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
