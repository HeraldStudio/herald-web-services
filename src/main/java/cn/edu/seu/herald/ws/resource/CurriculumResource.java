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

import cn.edu.seu.herald.ws.api.curriculum.Course;
import cn.edu.seu.herald.ws.api.curriculum.Curriculum;
import cn.edu.seu.herald.ws.api.curriculum.Student;
import cn.edu.seu.herald.ws.api.curriculum.StudentList;
import cn.edu.seu.herald.ws.dao.CurriculumDataAccess;
import java.io.IOException;
import java.net.URI;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Component
@Path("/curriculum")
public class CurriculumResource {

    @Autowired
    private CurriculumDataAccess curriculumDataAccess;

    public void setCurriculumDataAccess(
            CurriculumDataAccess curriculumDataAccess) {
        this.curriculumDataAccess = curriculumDataAccess;
    }

    @GET
    @Path("/")
    @Produces("application/vnd.herald.curriculum+xml")
    public Curriculum getCurriculum(
            @QueryParam("cardNumber") String cardNumber,
            @QueryParam("term") String term,
            @Context ServletContext context,
            @Context HttpServletResponse response) throws IOException {
        if (!curriculumDataAccess.containsStudent(cardNumber)) {
            response.sendError(404, "Curriculum not found");
            return null;
        }
        String contextPath = context.getContextPath();
        Curriculum curriculum = (term == null)
                ? curriculumDataAccess.getCurriculum(cardNumber)
                : curriculumDataAccess.getCurriculum(cardNumber, term);
        for (Course course : curriculum.getCourses().getCourses()) {
            URI uri = UriBuilder
                    .fromPath(getAbstractPath(contextPath,
                            "/curriculum/course/{id}"))
                    .build(course.getId());
            course.setStudents(uri.toString());
        }
        return curriculum;
    }

    @GET
    @Path("/course/{id}")
    @Produces("application/vnd.herald.curriculum+xml")
    public StudentList getStudentsOfCourse(@PathParam("id") int id,
            @Context ServletContext context,
            @Context HttpServletResponse response) throws IOException {
        if (!curriculumDataAccess.containsCourse(id)) {
            response.sendError(404, "Course not found");
            return null;
        }
        String contextPath = context.getContextPath();
        StudentList studentList = curriculumDataAccess.getStudentsOfCourse(id);
        for (Student student : studentList.getStudents()) {
            URI uri = UriBuilder
                    .fromPath(getAbstractPath(contextPath, "/curriculum"))
                    .queryParam("cardNumber", student.getCardNumber())
                    .build();
            student.setCurriculum(uri.toString());
        }
        return studentList;
    }

    private String getAbstractPath(String ...path) {
        StringBuilder pathBuilder = new StringBuilder();
        for (String p : path) {
            pathBuilder.append(p);
        }
        return pathBuilder.toString();
    }
}
