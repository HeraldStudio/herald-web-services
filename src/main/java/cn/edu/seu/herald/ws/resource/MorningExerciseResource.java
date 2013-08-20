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
package cn.edu.seu.herald.ws.resource;

import cn.edu.seu.herald.ws.api.exercise.RunTimesData;
import cn.edu.seu.herald.ws.dao.AuthenticationFailure;
import cn.edu.seu.herald.ws.dao.MorningExerciseDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
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
            @QueryParam("password") String password) throws IOException {
        try {
            return morningExerciseDataAccess.getRunTimesData(username, password);
        } catch (AuthenticationFailure failure) {
            throw new WebApplicationException(401);
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
