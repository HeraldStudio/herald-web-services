package cn.edu.seu.herald.ws.dao.impl;

import cn.edu.seu.herald.ws.api.curriculum.Strategy;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

class CourseParser {

    private int textNodeCounter = 0;
    private String[] courseBuffer = new String[3];

    public JSONArray getEachCourseFromElement(Element element) {
        JSONArray courseArr = new JSONArray();

        boolean prevIsBr = false;
        for (Node node : element.childNodes()) {
            if (!(node instanceof TextNode)) {
                if (!prevIsBr) {
                    prevIsBr = true;
                    continue;
                }
                int bufferIndex = textNodeCounter % 3;
                courseBuffer[bufferIndex] = "";
            } else {
                String text = ((TextNode) node).text().trim();
                int bufferIndex = textNodeCounter % 3;
                courseBuffer[bufferIndex] = text;
                prevIsBr = false;
            }
            if (textNodeCounter % 3 == 2) {
                Strategy strategy = Strategy.NONE;
                String location = courseBuffer[2];

                if (courseBuffer[2].startsWith("(单)")) {
                    strategy = Strategy.ODD;
                    location = location.substring("(单)".length(),
                            location.length());
                } else if (courseBuffer[2].startsWith("(双)")) {
                    strategy = Strategy.EVEN;
                    location = location.substring("(双)".length(),
                            location.length());
                }
                JSONObject course = newCourse(courseBuffer[0],
                        courseBuffer[1], location, strategy);
                courseArr.add(course);
            }
            ++textNodeCounter;
        }
        return courseArr;
    }

    private JSONObject newCourse(String name, String time,
                                 String location, Strategy strategy) {
        JSONObject course = new JSONObject();
        course.put("name", name);
        course.put("time", time);
        course.put("location", location);
        course.put("strategy", strategy);
        return course;
    }
}
