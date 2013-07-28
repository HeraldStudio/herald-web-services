package cn.edu.seu.herald.ws.resource;

import cn.edu.seu.herald.ws.api.update.Update;
import cn.edu.seu.herald.ws.dao.AndroidClientUpdateDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
@Component
@Path("/update")
public class AndroidClientUpdateResource {

    private static final String APPLICATION_VND_HERALD_UPDATE =
            "application/vnd.herald.update+xml";
    @Autowired
    private AndroidClientUpdateDataAccess androidClientUpdateDataAccess;

    @GET
    @Path("/")
    @Produces(APPLICATION_VND_HERALD_UPDATE)
    public Update getUpdateInfo()
            throws IOException {
        return androidClientUpdateDataAccess.getUpdateInfo();
    }
}
