package cn.edu.seu.herald.ws.dao;

import cn.edu.seu.herald.ws.api.exercise.RunTimesData;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
public interface MorningExerciseDataAccess {

    RunTimesData getRunTimesData(String username, String password)
            throws DataAccessException, AuthenticationFailure;

    int getRemainDays() throws DataAccessException;
}
