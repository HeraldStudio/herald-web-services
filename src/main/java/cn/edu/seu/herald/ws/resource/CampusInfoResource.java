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
import com.sun.jersey.api.NotFoundException;
import org.apache.wink.common.model.rss.RssFeed;
import org.apache.wink.common.model.synd.SyndFeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Component
@Path("/campus")
public class CampusInfoResource extends AbstractResource {

    @Autowired
    private CampusInfoDataAccess campusInfoDataAccess;

    public void setCampusInfoDataAccess(
            CampusInfoDataAccess campusInfoDataAccess) {
        this.campusInfoDataAccess = campusInfoDataAccess;
    }

    /**
     * 获取所有可用的订阅目录
     * @return
     */
    @GET
    @Path("/")
    @Produces({"application/rss+xml", MediaType.APPLICATION_XML})
    public RssFeed getAvailableFeeds() {
        SyndFeed syndFeed = campusInfoDataAccess.getAvailableFeeds();
        return new RssFeed(syndFeed);
    }

    @GET
    @Path("/{name}")
    @Produces({"application/rss+xml", MediaType.APPLICATION_XML})
    public RssFeed getRssFeedByName(
            @PathParam("name") String name,
            @QueryParam("before") String before,
            @QueryParam("after") String after,
            @QueryParam("limit") @DefaultValue("10") int limit)
            throws IOException {
        checkParamNotNull(name);
        checkIsAvailable(name);

        if (before != null && after == null) {
            SyndFeed syndFeed = campusInfoDataAccess.getFeedBeforeByName(
                    name, before, limit);
            return new RssFeed(syndFeed);
        }

        if (before == null && after != null) {
            SyndFeed syndFeed = campusInfoDataAccess.getFeedAfterByName(
                    name, after, limit);
            return new RssFeed(syndFeed);
        }

        if (before == null && after == null) {
            SyndFeed syndFeed = campusInfoDataAccess.getFeedByName(
                    name, limit);
            return new RssFeed(syndFeed);
        }

        throw new WebApplicationException(400);
    }

    private void checkIsAvailable(String feedName) {
        if (!campusInfoDataAccess.containsFeed(feedName)) {
            throw new NotFoundException();
        }
    }
}
