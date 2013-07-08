package cn.edu.seu.herald.ws.dao;

import cn.edu.seu.herald.ws.api.library.Book;
import cn.edu.seu.herald.ws.api.library.Booklist;
import cn.edu.seu.herald.ws.api.library.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
public class LibraryDataAccessImpl extends AbstractHttpDataAccess
        implements LibraryDataAccess {

    private String endPointUri = "http://58.192.117.11:8989/" +
            "androidSchoolLibInterface/controller.php";

    public LibraryDataAccessImpl(HttpClient client) {
        super(client);
    }

    public void setEndPointUri(String endPointUri) {
        this.endPointUri = endPointUri;
    }

    @Override
    public Booklist search(String keyword, int page)
            throws DataAccessException {
        GetMethod getMethod = newGetMethod(endPointUri);
        NameValuePair[] query = new NameValuePair[] {
                new NameValuePair("control", "opac"),
                new NameValuePair("action", "user_check"),
                new NameValuePair("searchtype", "0"),
                new NameValuePair("sourceType", "0"),
                new NameValuePair("page", String.valueOf(page)),
                new NameValuePair("k", keyword)
        };
        getMethod.setQueryString(query);

        try {
            String responseBody = getResponseBody(getMethod);
            Booklist booklist = new Booklist();
            JSONObject jsonObject = JSONObject.fromObject(responseBody);
            JSONArray jsonArray = JSONArray.fromObject(
                    jsonObject.get("contents"));
            for (Object item : jsonArray) {
                JSONObject bookObj = JSONObject.fromObject(item);
                String title = bookObj.getString("title");
                String author = bookObj.getString("author");
                Book book = new Book();
                book.setName(title);
                book.setAuthor(author);
                booklist.getBooks().add(book);
            }
            return booklist;
        } finally {
            releaseConnection(getMethod);
        }
    }

    @Override
    public Book getBookByMarcNo(String marcNo) throws DataAccessException {
        GetMethod getMethod = newGetMethod(endPointUri);
        NameValuePair[] query = new NameValuePair[] {
                new NameValuePair("control", "search_opac_detail"),
                new NameValuePair("action", "user_check"),
                new NameValuePair("marc_no", marcNo)
        };
        getMethod.setQueryString(query);
        executeMethod(getMethod);

        try {
            String responseBody = getResponseBody(getMethod);
            Book book = new Book();
            JSONObject jsonObject = JSONObject.fromObject(responseBody);
            String cnt1 = jsonObject.getString("cnt1");
            String cnt2 = jsonObject.getString("cnt2");
            // TODO parse html from cnt
            return book;
        } finally {
            releaseConnection(getMethod);
        }
    }

    @Override
    public User getUserIfAuthenticated(String username, String password)
            throws DataAccessException {
        GetMethod getMethod = newGetMethod(endPointUri);
        NameValuePair[] query = new NameValuePair[] {
                new NameValuePair("control", "CheckUser"),
                new NameValuePair("action", "user_check"),
                new NameValuePair("username", username),
                new NameValuePair("password", password)
        };
        getMethod.setQueryString(query);
        executeMethod(getMethod);

        try {
            String responseBody = getResponseBody(getMethod);
            // TODO check, get the cookie token, set urls with the token
            if (false) {
                return null;
            }
            String token = null;
            return getUserWithToken(token);
        } finally {
            releaseConnection(getMethod);
        }
    }

    @Override
    public User getUserWithToken(String token) throws DataAccessException {
        GetMethod getMethod = newGetMethod(endPointUri);
        NameValuePair[] query = new NameValuePair[] {
                new NameValuePair("control", "redr_info"),
                new NameValuePair("action", "user_check"),
                new NameValuePair("act", "1")
        };
        getMethod.setQueryString(query);
        addTokenToRequest(getMethod, token);
        executeMethod(getMethod);

        try {
            String responseBody = getResponseBody(getMethod);
            User user = new User();
            // TODO parse JSON, get user info
            return user;
        } finally {
            releaseConnection(getMethod);
        }
    }

    @Override
    public Booklist getBooksBorrowedByUser(String token)
            throws AuthenticationFailure, DataAccessException {
        return null;
    }

    @Override
    public Booklist getBooksReservedByUser(String token)
            throws AuthenticationFailure, DataAccessException {
        return null;
    }

    @Override
    public Booklist getBorrowHistory(String token)
            throws AuthenticationFailure, DataAccessException {
        return null;
    }

    @Override
    public boolean reserveBookByMarcNo(String marcNo, String token)
            throws AuthenticationFailure, DataAccessException {
        return false;
    }

    @Override
    public boolean renewBookByMarcNo(String marcNo, String token)
            throws AuthenticationFailure, DataAccessException {
        return false;
    }

    private void addTokenToRequest(HttpMethod httpMethod, String token) {
        httpMethod.setRequestHeader("Cookie",
                String.format("PHPSESSID=%s", token));

    }
}
