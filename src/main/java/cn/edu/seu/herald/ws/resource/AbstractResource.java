package cn.edu.seu.herald.ws.resource;

import javax.ws.rs.WebApplicationException;
import java.util.Date;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
abstract class AbstractResource {

    protected void checkParamNotNull(Object param)
            throws WebApplicationException {
        if (param == null) {
            throw new WebApplicationException(400);
        }
    }

    protected Date dateOfNextHour() {
        return new Date(new Date().getTime() + 1000 * 60 * 60);
    }
}
