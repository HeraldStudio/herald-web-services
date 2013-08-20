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
package cn.edu.seu.herald.ws.resource.aspect;

import cn.edu.seu.herald.ws.api.curriculum.Course;
import cn.edu.seu.herald.ws.api.curriculum.Curriculum;
import cn.edu.seu.herald.ws.api.curriculum.Student;
import cn.edu.seu.herald.ws.api.curriculum.StudentList;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
@Aspect
public class CurriculumResourceAspect {

    @AfterReturning(
            pointcut =
                    "execution(" +
                    "cn.edu.seu.herald.ws.api.curriculum.Course " +
                    "cn.edu.seu.herald.ws.resource.CurriculumResource." +
                    "getCourseById(..))",
            returning = "result"
    )
    public void afterReturningCourse(JoinPoint joinPoint, Object result) {
        if (result == null) {
            return;
        }
        Assert.isInstanceOf(Course.class, result);

        Course course = (Course) result;
        addCourseHref(course);
    }

    @AfterReturning(
            pointcut =
                    "execution(" +
                    "cn.edu.seu.herald.ws.api.curriculum.Curriculum " +
                    "cn.edu.seu.herald.ws.resource.CurriculumResource." +
                    "getCurriculum(..))",
            returning = "result"
    )
    public void addLinkToCurriculumReturned(JoinPoint joinPoint, Object result) {
        if (result == null) {
            return;
        }
        Assert.isInstanceOf(Curriculum.class, result);

        Curriculum curriculum = (Curriculum) result;
        for (Course course : curriculum.getCourses().getCourses()) {
            addCourseHref(course);
        }
    }

    private void addCourseHref(Course course) {
        StudentList studentList = course.getStudents();
        if (studentList != null) {
            for (Student student : course.getStudents().getStudents()) {
                URI uri = UriBuilder
                        .fromPath("./curriculum")
                        .queryParam("cardNumber", student.getCardNumber())
                        .build();
                student.setCurriculum(uri.toString());
            }
        }
        URI uri = UriBuilder
                .fromPath("./curriculum/course/{id}")
                .build(course.getId());
        course.setHref(uri.toString());
    }
}
