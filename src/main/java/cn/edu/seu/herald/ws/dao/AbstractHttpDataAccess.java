package cn.edu.seu.herald.ws.dao;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
abstract class AbstractHttpDataAccess {

    private HttpClient httpClient;

    public AbstractHttpDataAccess(HttpClient httpClient) {
        this.httpClient = httpClient;
        httpClient.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
    }

    protected String getResponseBody(HttpMethod httpMethod)
            throws DataAccessException {
        int status = executeMethod(httpMethod);
        if (status != HttpStatus.SC_OK) {
            throw new DataAccessException("Unexpected status: " + status);
        }

        try {
            return httpMethod.getResponseBodyAsString();
        } catch (IOException ex) {
            throw new DataAccessException(ex);
        }
    }

    protected int executeMethod(HttpMethod httpMethod)
            throws DataAccessException {
        try {
            return httpClient.executeMethod(httpMethod);
        } catch (IOException ex) {
            throw new DataAccessException(ex);
        }
    }

    protected void releaseConnection(HttpMethod httpMethod) {
        httpMethod.releaseConnection();
    }

    protected GetMethod newGetMethod(String url) {
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        return getMethod;
    }

    protected PostMethod newPostMethod(String url) {
        PostMethod postMethod = new PostMethod(url);
        postMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        return postMethod;
    }
}
