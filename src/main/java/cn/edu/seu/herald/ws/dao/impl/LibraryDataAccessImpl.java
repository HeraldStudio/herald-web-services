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

import cn.edu.seu.herald.ws.api.library.Book;
import cn.edu.seu.herald.ws.api.library.Booklist;
import cn.edu.seu.herald.ws.api.library.User;
import cn.edu.seu.herald.ws.dao.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
@Repository("libraryDataAccess")
public class LibraryDataAccessImpl extends AbstractHttpDataAccess
        implements LibraryDataAccess {

    private String endPointUri = "http://58.192.117.11:8989/" +
            "androidSchoolLibInterface/controller.php";

    @Autowired
    public LibraryDataAccessImpl(HttpClient client) {
        super(client);
    }

    public void setEndPointUri(String endPointUri) {
        this.endPointUri = endPointUri;
    }

    @Override
    public Booklist search(String keyword, int page)
            throws DataAccessException {
        Assert.notNull(keyword);

        String responseBody = query(endPointUri, new NameValuePair[] {
                new NameValuePair("control", "opac"),
                new NameValuePair("action", "user_check"),
                new NameValuePair("searchtype", "0"),
                new NameValuePair("sourceType", "0"),
                new NameValuePair("page", String.valueOf(page)),
                new NameValuePair("k", keyword)
        });
        Booklist booklist = new Booklist();
        JSONObject jsonObject = JSONObject.fromObject(responseBody);
        JSONArray jsonArray = JSONArray.fromObject(
                jsonObject.get("contents"));
        for (Object item : jsonArray) {
            JSONObject bookObj = JSONObject.fromObject(item);
            String title = bookObj.getString("title");
            String author = bookObj.getString("author");
            String marcNo = bookObj.getString("marc_no");
            Book book = new Book();
            book.setName(title);
            book.setAuthor(author);
            book.setMarcNo(marcNo);
            booklist.getBooks().add(book);
        }
        return booklist;
    }

    @Override
    public Book getBookByMarcNo(String marcNo) throws DataAccessException {
        Assert.notNull(marcNo);

        String responseBody = query(endPointUri, new NameValuePair[] {
                new NameValuePair("control", "search_opac_detail"),
                new NameValuePair("action", "user_check"),
                new NameValuePair("marc_no", marcNo)
        });
        Book book = new Book();
        JSONObject jsonObject = JSONObject.fromObject(responseBody);
        JSONArray jsonArray = jsonObject.getJSONArray("contents");
        if (jsonArray.size() < 1) {
            return book;
        }

        JSONObject contentObj = jsonArray.getJSONObject(0);
        String cnt1 = contentObj.getString("cnt1");
        String cnt2 = contentObj.getString("cnt2");
        new BookParser(cnt1, cnt2).parse(book);
        return book;
    }

    @Override
    public User getUserIfAuthenticated(String username, String password)
            throws DataAccessException {
        Assert.notNull(username);
        Assert.notNull(password);

        GetMethod getMethod = newGetMethod(endPointUri, new NameValuePair[] {
                new NameValuePair("control", "CheckUser"),
                new NameValuePair("action", "user_check"),
                new NameValuePair("ui", username),
                new NameValuePair("up", password)
        });
        executeMethod(getMethod);

        try {
            String responseBody = getResponseBody(getMethod);
            JSONObject jsonObject = JSONObject.fromObject(responseBody);
            String status = jsonObject.getString("status");
            if ("fail!".equals(status)) { // not authenticated
                return null;
            }
            if (!"ok!".equals(status)) { // unrecognizable response
                throw new DataAccessException(String.format("Unrecognizable " +
                        "response from the library service: %s",
                        status));
            }

            // check, get the cookie token, set urls with the token
            Header cookieHeader = getMethod.getResponseHeader("Set-Cookie");
            if (cookieHeader == null) {
                throw new DataAccessException(
                        "Cannot get cookie from the response");
            }
            String cookieStr = cookieHeader.getValue();
            String token = CookieUtils.getCookieValue(cookieStr, "PHPSESSID");
            if (token == null) {
                throw new DataAccessException(String.format(
                        "Cannot get PHPSESSID from the cookies: %s",
                        cookieStr));
            }
            return getUserWithToken(token);
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
            releaseConnection(getMethod);
        }
    }

    @Override
    public User getUserWithToken(String token) throws DataAccessException {
        Assert.notNull(token);

        GetMethod getMethod = newGetMethod(endPointUri, new NameValuePair[] {
                new NameValuePair("control", "redr_info"),
                new NameValuePair("action", "user_check"),
                new NameValuePair("act", "1")
        }, token);
        executeMethod(getMethod);

        try {
            String responseBody = getResponseBody(getMethod);
            User user = new User();
            // parse JSON, get user info
            JSONObject jsonObject = JSONObject.fromObject(responseBody);
            user.setStudentNumber(jsonObject.getString("zjh"));
            user.setName(jsonObject.getString("name"));
            user.setGender(GenderUtils.fromChinese(
                    jsonObject.getString("sex")));
            user.setCollege(jsonObject.getString("pubment"));
            user.setEmail(jsonObject.getString("em"));
            return user;
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
            releaseConnection(getMethod);
        }
    }

    @Override
    public Booklist getBooksBorrowedByUser(String token)
            throws AuthenticationFailure, DataAccessException {
        Assert.notNull(token);

        GetMethod getMethod = newGetMethod(endPointUri, new NameValuePair[] {
                new NameValuePair("control", "get_record"),
                new NameValuePair("action", "user_check"),
                new NameValuePair("act", "1")
        }, token);
        executeMethod(getMethod);

        try {
            String responseBody = getResponseBody(getMethod);
            JSONObject jsonObject = JSONObject.fromObject(responseBody);
            return toBooklist(jsonObject);
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
            releaseConnection(getMethod);
        }
    }

    @Override
    public Booklist getBooksReservedByUser(String token)
            throws AuthenticationFailure, DataAccessException {
        Assert.notNull(token);

        GetMethod getMethod = newGetMethod(endPointUri, new NameValuePair[] {
                new NameValuePair("control", "userpreg"),
                new NameValuePair("action", "user_check"),
                new NameValuePair("act", "3")
        }, token);
        executeMethod(getMethod);

        try {
            String responseBody = getResponseBody(getMethod);
            JSONObject jsonObject = JSONObject.fromObject(responseBody);
            return toBooklist(jsonObject);
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
            releaseConnection(getMethod);
        }
    }

    @Override
    public Booklist getBorrowHistory(String token)
            throws AuthenticationFailure, DataAccessException {
        Assert.notNull(token);

        GetMethod getMethod = newGetMethod(endPointUri, new NameValuePair[] {
                new NameValuePair("control", "get_record"),
                new NameValuePair("action", "user_check"),
                new NameValuePair("act", "3")
        }, token);
        executeMethod(getMethod);

        try {
            String responseBody = getResponseBody(getMethod);
            JSONObject jsonObject = JSONObject.fromObject(responseBody);
            return toBooklist(jsonObject);
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
            releaseConnection(getMethod);
        }
    }

    @Override
    public boolean reserveBookByMarcNo(String marcNo, String token)
            throws AuthenticationFailure, DataAccessException {
        // TODO implement
        return false;
    }

    @Override
    public boolean renewBookByMarcNo(String marcNo, String token)
            throws AuthenticationFailure, DataAccessException {
        // TODO implement
        return false;
    }

    private GetMethod newGetMethod(String url,
                                   NameValuePair[] nameValuePairs,
                                   String token) {
        GetMethod getMethod = newGetMethod(url, nameValuePairs);
        getMethod.setRequestHeader("Cookie",
                String.format("PHPSESSID=%s", token));
        return getMethod;
    }

    private Booklist toBooklist(JSONObject booksObj) {
        Assert.notNull(booksObj);

        JSONArray bookArray = booksObj.getJSONArray("contents");
        Booklist booklist = new Booklist();
        for (int i = 0; i < bookArray.size(); ++i) {
            JSONObject bookObj = bookArray.getJSONObject(i);
            Book book = new Book();
            book.setName(bookObj.getString("title"));
            book.setAuthor(bookObj.getString("author"));
            book.setMarcNo(bookObj.getString("marc_no"));
            booklist.getBooks().add(book);
        }
        return booklist;
    }
}
