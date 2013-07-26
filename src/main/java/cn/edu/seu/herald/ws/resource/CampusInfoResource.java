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
package cn.edu.seu.herald.ws.resource;

import cn.edu.seu.herald.ws.dao.CampusInfoDataAccess;
import org.apache.wink.common.model.rss.RssFeed;
import org.apache.wink.common.model.synd.SyndFeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Component
@Path("/campus")
public class CampusInfoResource {

    @Autowired
    private CampusInfoDataAccess campusInfoDataAccess;

    public void setCampusInfoDataAccess(
            CampusInfoDataAccess campusInfoDataAccess) {
        this.campusInfoDataAccess = campusInfoDataAccess;
    }

    @GET
    @Path("/{name}")
    @Produces("application/rss+xml")
    public RssFeed getRssFeedByName(@PathParam("name") String name,
            @HeaderParam("If-None-Match") String clientUUID,
            @Context HttpServletResponse response) throws IOException {
        if (!campusInfoDataAccess.containsFeed(name)) {
            response.sendError(404);
            return null;
        }
        String latestUUID = campusInfoDataAccess.getLatestUUID(name);
        boolean match = (latestUUID != null) && latestUUID.equals(clientUUID);
        if (match) {
            response.sendError(304);
            return null;
        }

        SyndFeed syndFeed = (clientUUID == null)
                ? campusInfoDataAccess.getFeedByName(name)
                : campusInfoDataAccess.getFeedByName(name, clientUUID);
        response.setHeader("ETag", latestUUID);
        return new RssFeed(syndFeed);
    }
}
