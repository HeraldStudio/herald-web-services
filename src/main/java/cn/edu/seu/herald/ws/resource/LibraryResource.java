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

import cn.edu.seu.herald.ws.api.library.Book;
import cn.edu.seu.herald.ws.api.library.Booklist;
import cn.edu.seu.herald.ws.api.library.User;
import cn.edu.seu.herald.ws.dao.AuthenticationFailure;
import cn.edu.seu.herald.ws.dao.LibraryDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
@Component
@Path("/library")
public class LibraryResource {

    public static final String APPLICATION_VND_HERALD_LIB =
            "application/vnd.herald.library+xml";
    @Autowired
    private LibraryDataAccess libraryDataAccess;

    @POST
    @Path("/user")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public User login(@QueryParam("username") String username,
                      @QueryParam("password") String password,
                      @Context HttpServletResponse response)
            throws IOException {
        User user = libraryDataAccess.getUserIfAuthenticated(
                username, password);
        if (user == null) {
            response.sendError(401, "Not authorized");
            return null;
        }
        return user;
    }

    @GET
    @Path("/books")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public Booklist searchBooks(@QueryParam("keyword") String keyword,
                                @QueryParam("page")
                                @DefaultValue("1") int page,
                                @Context HttpServletResponse response)
            throws IOException {
        if (keyword == null) {
            response.sendError(400, "keyword is null");
            return null;
        }
        Booklist result = libraryDataAccess.search(keyword, page);
        return result;
    }

    @GET
    @Path("/books/borrowed")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public Booklist getBooksBorrowed(@QueryParam("token") String token,
                                     @Context HttpServletResponse response)
            throws IOException {
        if (token == null) {
            response.sendError(400, "token is null");
            return null;
        }

        try {
            return libraryDataAccess.getBooksBorrowedByUser(token);
        } catch (AuthenticationFailure failure) {
            response.sendError(401, "Not authenticated");
            return null;
        }
    }

    @GET
    @Path("/books/reserved")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public Booklist getBooksReserved(@QueryParam("token") String token,
                                     @Context HttpServletResponse response)
            throws IOException {
        if (token == null) {
            response.sendError(400, "token is null");
            return null;
        }

        try {
            return libraryDataAccess.getBooksReservedByUser(token);
        } catch (AuthenticationFailure failure) {
            response.sendError(401, "Not authenticated");
            return null;
        }
    }

    @GET
    @Path("/books/history")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public Booklist getBorrowHistory(@QueryParam("token") String token,
                                     @Context HttpServletResponse response)
            throws IOException {
        if (token == null) {
            response.sendError(400, "token is null");
            return null;
        }

        try {
            return libraryDataAccess.getBorrowHistory(token);
        } catch (AuthenticationFailure failure) {
            response.sendError(401, "Not authenticated");
            return null;
        }
    }

    @GET
    @Path("/book/{id}")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public Book getBook(@PathParam("id") String id,
                        @Context HttpServletResponse response)
            throws IOException {
        if (id == null) {
            response.sendError(400, "book id is null");
            return null;
        }

        return libraryDataAccess.getBookByMarcNo(id);
    }

    @POST
    @Path("/book/{id}/reservation")
    public Response reserve(@PathParam("id") String id,
                        @QueryParam("token") String token,
                        @Context HttpServletResponse response)
            throws IOException {
        if (id == null) {
            response.sendError(400, "book id is null");
            return null;
        }
        if (token == null) {
            response.sendError(400, "token is null");
            return null;
        }

        try {
            boolean accepted = libraryDataAccess.reserveBookByMarcNo(id, token);
            return (accepted)
                    ? Response.status(Response.Status.ACCEPTED).build()
                    : Response.status(Response.Status.NOT_ACCEPTABLE).build();
        } catch (AuthenticationFailure failure) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/book/{id}/renewal")
    public Response renew(@PathParam("id") String id,
                      @QueryParam("token") String token,
                      @Context HttpServletResponse response)
            throws IOException {
        if (id == null) {
            response.sendError(400, "book id is null");
            return null;
        }
        if (token == null) {
            response.sendError(400, "token is null");
            return null;
        }

        try {
            boolean accepted = libraryDataAccess.renewBookByMarcNo(id, token);
            return (accepted)
                    ? Response.status(Response.Status.ACCEPTED).build()
                    : Response.status(Response.Status.NOT_ACCEPTABLE).build();
        } catch (AuthenticationFailure failure) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
