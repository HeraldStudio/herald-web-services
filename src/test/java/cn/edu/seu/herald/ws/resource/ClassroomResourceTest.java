package cn.edu.seu.herald.ws.resource;

import cn.edu.seu.herald.ws.api.curriculum.Day;
import cn.edu.seu.herald.ws.dao.ClassroomDataAccess;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;

public class ClassroomResourceTest {

    @Tested
    private ClassroomResource classroomResource;
    @Injectable
    private ClassroomDataAccess classroomDataAccess;

    @Test(expected = WebApplicationException.class)
    public void testGetUnusedClassroomsWithNullDay() throws Exception {
        final Day day = null;
        final int from = 1;
        final int to = 13;

        classroomResource.getUnusedClassrooms(day, from, to);
    }

    @Test
    public void testGetUnusedClassrooms() throws Exception {
        final Day day = Day.MON;
        final int from = 1;
        final int to = 13;

        new Expectations() {
            {
                classroomDataAccess.getClassroomsUnused(day, from, to);
                times = 1;
            }
        };
        classroomResource.getUnusedClassrooms(day, from, to);
    }
}
