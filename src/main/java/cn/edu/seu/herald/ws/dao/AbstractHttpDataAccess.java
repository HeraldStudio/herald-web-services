package cn.edu.seu.herald.ws.dao;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
abstract class AbstractHttpDataAccess {

    private HttpClient httpClient;

    public AbstractHttpDataAccess(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    protected String getResponseBody(HttpMethodBase httpMethodBase)
            throws DataAccessException {
        int status = executeMethod(httpMethodBase);
        if (status != HttpStatus.SC_OK) {
            throw new DataAccessException("Unexpected status: " + status);
        }

        try {
            return httpMethodBase.getResponseBodyAsString();
        } catch (IOException ex) {
            throw new DataAccessException(ex);
        }
    }

    protected int executeMethod(HttpMethodBase httpMethodBase)
            throws DataAccessException {
        try {
            return httpClient.executeMethod(httpMethodBase);
        } catch (IOException ex) {
            throw new DataAccessException(ex);
        }
    }

    protected void releaseConnection(HttpMethodBase methodBase) {
        methodBase.releaseConnection();
    }
}
