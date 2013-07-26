package cn.edu.seu.herald.ws.resource;

import cn.edu.seu.herald.ws.api.exercise.RunTimesData;
import cn.edu.seu.herald.ws.dao.MorningExerciseDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

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

    @GET
    @Path("/")
    @Produces(APPLICATION_VND_HERALD_EXCS)
    public RunTimesData getRunTimesData(
            @QueryParam("username") String username,
            @QueryParam("password") String password) {
        return morningExerciseDataAccess.getRunTimesData(username, password);
    }
}
