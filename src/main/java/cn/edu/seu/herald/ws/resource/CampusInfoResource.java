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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
@Component
@Path("/campus")
public class CampusInfoResource extends AbstractResource {

    @Autowired
    private CampusInfoDataAccess campusInfoDataAccess;
    @Autowired
    private JsonpParser jsonpParser;

    public void setCampusInfoDataAccess(
            CampusInfoDataAccess campusInfoDataAccess) {
        this.campusInfoDataAccess = campusInfoDataAccess;
    }

    @GET
    @Path("/jwc")
    @Produces({MediaType.WILDCARD, "text/json"})
    public Response getJwcFeed() {
        return Response.status(200)
                .entity(campusInfoDataAccess.getJwcFeed().toString())
                .expires(dateOfNextHour())
                .build();
    }

//    @GET
//    @Path("/jwc")
//    @Produces("application/javascript")
//    public Response getJwcFeed(@QueryParam("callback") String callback) {
//        try {
//            String json = campusInfoDataAccess.getJwcFeed().toString();
//            String jsonp = jsonpParser.jsonp(callback, json);
//            return Response.status(200)
//                    .entity(jsonp)
//                    .expires(dateOfNextHour())
//                    .build();
//        } catch (IllegalArgumentException ex) {
//            throw new WebApplicationException(400);
//        }
//    }
}
