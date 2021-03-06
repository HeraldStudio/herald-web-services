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

import cn.edu.seu.herald.ws.api.curriculum.Day;
import cn.edu.seu.herald.ws.dao.CurriculumDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Component
@Path("/curriculum")
public class CurriculumResource extends AbstractResource {

    @Autowired
    private CurriculumDataAccess curriculumDataAccess;

    public void setCurriculumDataAccess(
            CurriculumDataAccess curriculumDataAccess) {
        this.curriculumDataAccess = curriculumDataAccess;
    }

    @GET
    @Path("/{cardNumber}")
    @Produces("text/json")
    public Response getCurriculumOfWeek(
            @PathParam("cardNumber") String cardNumber,
            @QueryParam("term") String term) throws IOException {
        checkParamNotNull(cardNumber);

        String curriJsonArr = (term == null)
                ? curriculumDataAccess.getCurriculumOfWeek(
                cardNumber).toString()
                : curriculumDataAccess.getCurriculumOfWeek(
                cardNumber, term).toString();
        return Response.status(200).expires(dateOfNextHour())
                .entity(curriJsonArr).build();
    }

    @GET
    @Path("/{cardNumber}/{day}")
    @Produces("text/json")
    public Response getCurriculumOfDay(
            @PathParam("cardNumber") String cardNumber,
            @PathParam("day") Day day,
            @QueryParam("term") String term) throws IOException {
        checkParamNotNull(cardNumber);
        checkParamNotNull(day);

        String curriJson = (term == null)
                ? curriculumDataAccess.getCurriculumOfDay(
                cardNumber, day).toString()
                : curriculumDataAccess.getCurriculumOfDay(
                cardNumber, day, term).toString();
        return Response.status(200).expires(dateOfNextHour())
                .entity(curriJson).build();
    }
}
