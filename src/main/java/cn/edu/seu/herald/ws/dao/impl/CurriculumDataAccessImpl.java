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
package cn.edu.seu.herald.ws.dao.impl;

import cn.edu.seu.herald.ws.api.curriculum.*;
import cn.edu.seu.herald.ws.dao.CurriculumDataAccess;
import cn.edu.seu.herald.ws.dao.DataAccessException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Repository("curriculumDataAccess")
public class CurriculumDataAccessImpl implements CurriculumDataAccess {

    private static final String CURRI_ENTRY_URL =
            "http://xk.urp.seu.edu.cn/jw_service/service/stuCurriculum.action";
    private static final String CURRI_QUERY_HOME =
            "http://xk.urp.seu.edu.cn/jw_service/service/lookCurriculum.action";
    private static final String CURRI_SELECTOR = "table[cellpadding=0]" +
            "[cellspacing=0][valign=top][class=tableline] " +
            "tr:nth-child(%d) > td:nth-child(%d)";
    private static final String AVA_TERM_SELECTOR =
            "#stuCurriculum_queryAcademicYear > option";
    private static final String SAT_CURRI_SELECTOR =
            "table[cellpadding=0][cellspacing=0][valign=top][class=tableline]" +
                    " tr:nth-child(14) > td:nth-child(2)";
    private static final String SUN_CURRI_SELECTOR =
            "table[cellpadding=0][cellspacing=0][valign=top][class=tableline]" +
                    " tr:nth-child(16) > td:nth-child(2)";
    private static final int MORNING = 2;
    private static final int AFTERNOON = 7;
    private static final int EVENING = 12;

    @Override
    public JSONArray getCurriculumOfWeek(String cardNumber)
            throws DataAccessException {
        try {
            return getCurriculumOfWeek(cardNumber, getCurrentTerm());
        } catch (IOException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public JSONArray getCurriculumOfWeek(String cardNumber, String term)
            throws DataAccessException {
        try {
            Document curriDoc = Jsoup.connect(CURRI_ENTRY_URL)
                    .timeout(0)
                    .data("queryStudentId", cardNumber)
                    .data("queryAcademicYear", term).post();
            JSONArray curriculum = new JSONArray();
            for (Day workDay : getWorkDays()) {
                JSONObject workDayCourses = getCourseJsonOfDay(
                        workDay, curriDoc);
                curriculum.add(workDayCourses);
            }
            return curriculum;
        } catch (IOException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public JSONObject getCurriculumOfDay(String cardNumber, Day day)
            throws DataAccessException {
        try {
            return getCurriculumOfDay(cardNumber, day, getCurrentTerm());
        } catch (IOException e) {
            throw new DataAccessException(e);
        }
    }

    private String getCurrentTerm() throws IOException {
        Document queryHome = Jsoup.connect(CURRI_QUERY_HOME)
                .timeout(5000).get();
        Elements options = queryHome.select(AVA_TERM_SELECTOR);
        Assert.state(options != null && !options.isEmpty()
                && options.get(0) != null);
        Element firstOpt = options.get(0);
        return firstOpt.attr("value");
    }

    @Override
    public JSONObject getCurriculumOfDay(String cardNumber, Day day,
                                         String term)
            throws DataAccessException {
        try {
            Document curriDoc = Jsoup.connect(CURRI_ENTRY_URL)
                    .timeout(0)
                    .data("queryStudentId", cardNumber)
                    .data("queryAcademicYear", term).post();
            return getCourseJsonOfDay(day, curriDoc);
        } catch (IOException e) {
            throw new DataAccessException(e);
        }
    }

    private JSONObject getCourseJsonOfDay(Day day, Document curriDoc) {
        switch (day) {
            case SAT:
                return getCurriculumBySeletors(curriDoc, day,
                        SAT_CURRI_SELECTOR);
            case SUN:
                return getCurriculumBySeletors(curriDoc, day,
                        SUN_CURRI_SELECTOR);
            default:
                int tdIndex = getTdIndex(day);
                return getCurriculumBySeletors(curriDoc, day,
                        String.format(CURRI_SELECTOR, MORNING, tdIndex),
                        String.format(CURRI_SELECTOR, AFTERNOON, tdIndex),
                        String.format(CURRI_SELECTOR, EVENING, tdIndex));
        }
    }

    private JSONObject getCurriculumBySeletors(Document curriDoc,
                                               Day day,
                                               String ...selectors) {
        JSONArray courses = new JSONArray();
        for (String selector : selectors) {
            Elements elements = curriDoc.select(selector);
            assertCurriElements(elements);
            JSONArray eachCourses = new CourseParser()
                    .getEachCourseFromElement(elements.get(0));
            courses.addAll(eachCourses);
        }
        JSONObject curri = new JSONObject();
        curri.put("day", day.toString());
        curri.put("courses", courses);
        return curri;
    }

    private void assertCurriElements(Elements elements) {
        Assert.state(elements != null && elements.size() == 1);
    }

    private Day[] getWorkDays() {
        return new Day[] {Day.MON, Day.TUE, Day.WED, Day.THU, Day.FRI};
    }

    private int getTdIndex(Day day) {
        switch (day) {
            case MON:
                return 3;
            case TUE:
                return 4;
            case WED:
                return 5;
            case THU:
                return 6;
            case FRI:
                return 7;
            // not sure
            case SAT:
                return 8;
            case SUN:
                return 9;
        }
        throw new IllegalStateException(String.format(
                "Day[%s] is not acceptable here.", day));
    }
}
