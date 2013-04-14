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

import cn.edu.seu.herald.ws.api.Attendance;
import cn.edu.seu.herald.ws.api.Curriculum;
import cn.edu.seu.herald.ws.api.Day;
import cn.edu.seu.herald.ws.api.Schedule;
import cn.edu.seu.herald.ws.api.TimeTable;
import cn.edu.seu.herald.ws.dao.CurriculumDataAccess;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Component
@Path("/")
public class CurriculumResource {

    @Autowired
    private CurriculumDataAccess curriculumDataAccess;

    public void setCurriculumDataAccess(
            CurriculumDataAccess curriculumDataAccess) {
        this.curriculumDataAccess = curriculumDataAccess;
    }

    @GET
    @Path("/curriculum")
    @Produces(MediaType.APPLICATION_XML)
    public Curriculum getCurriculum(
            @MatrixParam("cardNumber") String cardNumber,
            @MatrixParam("term") String term) {
        return (term == null)
                ? curriculumDataAccess.getCurriculum(cardNumber)
                : curriculumDataAccess.getCurriculum(cardNumber, term);
    }
}
