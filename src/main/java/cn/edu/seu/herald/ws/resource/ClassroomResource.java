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
import cn.edu.seu.herald.ws.dao.ClassroomDataAccess;
import java.util.List;
import javax.ws.rs.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Component
@Path("/classroom")
public class ClassroomResource extends AbstractResource {

    @Autowired
    private ClassroomDataAccess classroomDataAccess;
    private CsvBuilder csvBuilder = new CsvBuilder();

    @GET
    @Path("/unused")
    @Produces("text/csv")
    public String getUnusedClassrooms(
            @QueryParam("day") Day day,
            @DefaultValue("1") @QueryParam("from") int from,
            @DefaultValue("13") @QueryParam("to") int to) {
        checkParamNotNull(day);

        List<String> classrooms = classroomDataAccess
                .getClassroomsUnused(day, from, to);
        return csvBuilder.getCsv(classrooms);
    }
}
