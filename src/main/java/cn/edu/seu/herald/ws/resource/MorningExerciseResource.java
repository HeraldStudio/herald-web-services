package cn.edu.seu.herald.ws.resource;

import cn.edu.seu.herald.ws.dao.MorningExerciseDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
@Component
@Path("/exercise")
public class MorningExerciseResource {

    public static final String APPLICATION_VND_HERALD_EXCS =
            "application/vnd.herald.exercise+xml";
    @Autowired
    private MorningExerciseDataAccess morningExerciseDataAccess;
}
