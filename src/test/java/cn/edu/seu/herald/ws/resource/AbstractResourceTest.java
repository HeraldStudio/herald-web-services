package cn.edu.seu.herald.ws.resource;

import org.junit.Test;

import javax.ws.rs.WebApplicationException;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
public class AbstractResourceTest {

    private AbstractResource abstractResourceMockImpl = new AbstractResource() {
    };

    @Test(expected = WebApplicationException.class)
    public void testCheckParamNotNullWithNullParam() throws Exception {
        abstractResourceMockImpl.checkParamNotNull(null);
    }

    @Test
    public void testCheckParamNotNull() throws Exception {
        abstractResourceMockImpl.checkParamNotNull(new Object());
    }
}
