package cn.edu.seu.herald.ws.dao;

import cn.edu.seu.herald.ws.api.library.Book;
import cn.edu.seu.herald.ws.api.library.Booklist;
import cn.edu.seu.herald.ws.api.library.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
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
        PostMethod postMethod = new PostMethod(endPointUri);
        postMethod.setParameter("control", "opac");
        postMethod.setParameter("action", "user_check");
        postMethod.setParameter("searchtype", "0");
        postMethod.setParameter("sourceType", "0");
        postMethod.setParameter("page", String.valueOf(page));
        postMethod.setParameter("k", keyword);
        try {
            int status = executeMethod(postMethod);
            if (status != HttpStatus.SC_OK) {
                throw new DataAccessException("Unexpected status: " + status);
            }

            String responseBody = postMethod.getResponseBodyAsString();
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
        } catch (IOException ex) {
            throw new DataAccessException(ex);
        } finally {
            releaseConnection(postMethod);
        }
    }

    @Override
    public Book getBookByMarcNo(String marcNo) throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User getUserIfAuthenticated(String username, String password)
            throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User getUserWithToken(String token) throws DataAccessException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
