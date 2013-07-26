package cn.edu.seu.herald.ws.dao.impl;

import cn.edu.seu.herald.ws.api.exercise.RunTime;
import cn.edu.seu.herald.ws.api.exercise.RunTimesData;
import cn.edu.seu.herald.ws.dao.DataAccessException;
import cn.edu.seu.herald.ws.dao.MorningExerciseDataAccess;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
public class MorningExerciseDataAccessImpl
        implements MorningExerciseDataAccess {

    private DatatypeFactory datatypeFactory;

    public MorningExerciseDataAccessImpl()
            throws DatatypeConfigurationException {
        datatypeFactory = DatatypeFactory.newInstance();
    }

    @Override
    public RunTimesData getRunTimesData(String username, String password)
            throws DataAccessException {
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
                runTime.getTimes().add(null);
            }
            return runTimesData;
        } catch (QueryFailure queryFailure) {
            throw new DataAccessException(queryFailure);
        }
    }

    private XMLGregorianCalendar parseXMLGregorianCalendar(Date date) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return datatypeFactory.newXMLGregorianCalendar(c);
    }
}
