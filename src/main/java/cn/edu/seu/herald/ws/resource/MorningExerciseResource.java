package cn.edu.seu.herald.ws.resource;

import cn.edu.seu.herald.ws.api.exercise.RunTimesData;
import cn.edu.seu.herald.ws.dao.AuthenticationFailure;
import cn.edu.seu.herald.ws.dao.MorningExerciseDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

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
    @Path("/runtimes")
    @Produces(APPLICATION_VND_HERALD_EXCS)
    public RunTimesData getRunTimesData(
            @QueryParam("username") String username,
            @QueryParam("password") String password,
            @Context HttpServletResponse response) throws IOException {
        try {
            return morningExerciseDataAccess.getRunTimesData(username, password);
        } catch (AuthenticationFailure failure) {
            response.sendError(401);
            return null;
        }
    }

    @GET
    @Path("/remain")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getRemainDays() {
        int days = morningExerciseDataAccess.getRemainDays();
        return Response.ok(String.valueOf(days)).build();
    }
}
