package cn.edu.seu.herald.ws.dao.impl;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
public class QueryFailure extends Exception {

    private static final long serialVersionUID = 1L;

    public QueryFailure() {
        super("morning exercise query failed");
    }

    public QueryFailure(Exception ex) {
        super("morning exercise query failed", ex);
    }

}
