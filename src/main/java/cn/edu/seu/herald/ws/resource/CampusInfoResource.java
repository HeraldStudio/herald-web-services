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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.wink.common.model.atom.AtomFeed;
import org.apache.wink.common.model.rss.RssFeed;
import org.apache.wink.common.model.synd.SyndFeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    @Produces({MediaType.WILDCARD, MediaType.APPLICATION_ATOM_XML})
    public AtomFeed getAtomFeedByName(@PathParam("name") String name) {
        SyndFeed syndFeed = campusInfoDataAccess.getFeedByName(name);
        return new AtomFeed(syndFeed);
    }

    @GET
    @Path("/{name}")
    @Produces("application/rss+xml")
    public RssFeed getRssFeedByName(@PathParam("name") String name) {
        SyndFeed syndFeed = campusInfoDataAccess.getFeedByName(name);
        return new RssFeed(syndFeed);
    }
}
