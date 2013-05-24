package cn.edu.seu.herald.ws.resource.aspect;

import cn.edu.seu.herald.ws.api.curriculum.Course;
import cn.edu.seu.herald.ws.api.curriculum.Curriculum;
import cn.edu.seu.herald.ws.api.curriculum.Student;
import cn.edu.seu.herald.ws.api.curriculum.StudentList;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
@Aspect
public class CurriculumResourceAspect implements ServletContextAware {

    private String contextPath;

    @Override
    public void setServletContext(ServletContext servletContext) {
        contextPath = servletContext.getContextPath();
    }

    @AfterReturning(
            pointcut =
                    "execution(" +
                    "cn.edu.seu.herald.ws.api.curriculum.Course " +
                    "cn.edu.seu.herald.ws.resource.CurriculumResource." +
                    "getCourseById(..))",
            returning= "result"
    )
    public void afterReturningCourse(JoinPoint joinPoint, Object result) {
        Course course = (Course) result;
        addCourseHref(course);
    }

    @AfterReturning(
            pointcut =
                    "execution(" +
                    "cn.edu.seu.herald.ws.api.curriculum.Curriculum " +
                    "cn.edu.seu.herald.ws.resource.CurriculumResource." +
                    "getCurriculum(..))",
            returning= "result"
    )
    public void afterReturningCurriculum(JoinPoint joinPoint, Object result) {
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
                        .fromPath(linkPath(contextPath, "/curriculum"))
                        .queryParam("cardNumber", student.getCardNumber())
                        .build();
                student.setCurriculum(uri.toString());
            }
        }
        URI uri = UriBuilder
                .fromPath(linkPath(contextPath, "/curriculum/course/{id}"))
                .build(course.getId());
        course.setHref(uri.toString());
    }

    private String linkPath(String... path) {
        StringBuilder pathBuilder = new StringBuilder();
        for (String p : path) {
            pathBuilder.append(p);
        }
        return pathBuilder.toString();
    }
}
