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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
class MorningExerciseQuery {

    private static final String POST_URL =
            "http://58.192.114.239:8088/sms2/studentLogin.do";

    private static final String INFO_URL =
            "http://58.192.114.239:8088/sms2/studentQueryListChecks.do" +
                    "?method=listChecks";

    private String username;

    private String password;

    private int times = -1;

    private List<Date> latestRecords;

    public MorningExerciseQuery(String username, String password) {
        this.username = username;
        this.password = password;
        latestRecords = new ArrayList<Date>(5);
    }

    public void execute() throws IOException, QueryFailure {
        String sessionCookie = step1(username, password);
        String htmlCode = step2(sessionCookie);
        times = step3(htmlCode);
        step4(htmlCode);
    }

    public int getTimes() {
        return times;
    }

    public List<Date> getLatestRecords() {
        return latestRecords;
    }

    private String step1(String username, String password) throws IOException {
        URL url = new URL(POST_URL);
        HttpURLConnection httpurlconnection =
                (HttpURLConnection) url.openConnection();
        httpurlconnection.setDoOutput(true);
        httpurlconnection.setRequestMethod("POST");

        StringBuilder builder = new StringBuilder();
        builder.append("xh=")
                .append(username)
                .append("&mm=")
                .append(password)
                .append("&method=login");

        String message = builder.toString();

        httpurlconnection.getOutputStream().write(message.getBytes());
        httpurlconnection.getOutputStream().flush();
        httpurlconnection.getOutputStream().close();

        httpurlconnection.getInputStream();

        String setCookie = httpurlconnection.getHeaderField("Set-Cookie");
        String cookie = setCookie.substring(0, setCookie.indexOf(";"));
        return cookie;
    }

    private String step2(String cookie) throws IOException {
        URL url = new URL(INFO_URL);
        HttpURLConnection httpurlconnection =
                (HttpURLConnection) url.openConnection();
        httpurlconnection.setRequestMethod("GET");
        httpurlconnection.setRequestProperty("Cookie", cookie);

        InputStream in = httpurlconnection.getInputStream();
        InputStreamReader reader = new InputStreamReader(in);

        StringBuilder builder = new StringBuilder();
        while (true) {
            int c = reader.read();
            if (c <= 0) {
                break;
            }
            builder.append((char) c);
            if (!reader.ready()) {
                break;
            }
        }
        String htmlCode = builder.toString();
        return htmlCode.trim();
    }

    private int step3(String htmlCode) throws QueryFailure {
        String headTag = "<td class=\"Content_Form\">";
        Pattern pattern = Pattern.compile(
                headTag +
                        "[0-9]*</td>[\\s]*</tr></table></td></tr></table>");
        Matcher matcher1 = pattern.matcher(htmlCode);
        if (matcher1.find()) {
            String matched = matcher1.group();
            Pattern numPattern = Pattern.compile("(\\d+)");
            Matcher matcher2 = numPattern.matcher(matched);
            if (matcher2.find()) {
                return new Integer(matcher2.group());
            }
        }
        throw new QueryFailure();
    }

    private void step4(String htmlCode) {
        // TODO parse html,
        // TODO and add the five latest exercise records into latestRecords
    }

}
