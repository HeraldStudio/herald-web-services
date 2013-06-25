package cn.edu.seu.herald.ws.dao;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
public class AuthenticationFailure extends Exception {

    public AuthenticationFailure() {
        super("Not authenticated");
    }
}
