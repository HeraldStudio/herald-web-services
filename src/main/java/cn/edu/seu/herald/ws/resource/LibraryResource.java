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
import com.sun.jersey.api.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
@Component
@Path("/library")
public class LibraryResource extends AbstractResource {

    public static final String APPLICATION_VND_HERALD_LIB =
            "application/vnd.herald.library+xml";
    @Autowired
    private LibraryDataAccess libraryDataAccess;

    @POST
    @Path("/user")
    @Produces({
            APPLICATION_VND_HERALD_LIB,
            MediaType.APPLICATION_XML
    })
    public User login(@QueryParam("username") String username,
                      @QueryParam("password") String password)
            throws IOException {
        User user = libraryDataAccess.getUserIfAuthenticated(
                username, password);
        if (user == null) {
            throw new WebApplicationException(401);
        }
        return user;
    }

    @GET
    @Path("/books")
    @Produces({
            APPLICATION_VND_HERALD_LIB,
            MediaType.APPLICATION_XML
    })
    public Booklist searchBooks(@QueryParam("keyword") String keyword,
                                @QueryParam("page")
                                @DefaultValue("1") int page)
            throws IOException {
        checkParamNotNull(keyword);
        Booklist result = libraryDataAccess.search(keyword, page);
        return result;
    }

    @GET
    @Path("/books/borrowed")
    @Produces({
            APPLICATION_VND_HERALD_LIB,
            MediaType.APPLICATION_XML
    })
    public Booklist getBooksBorrowed(@QueryParam("token") String token)
            throws IOException {
        checkParamNotNull(token);

        try {
            return libraryDataAccess.getBooksBorrowedByUser(token);
        } catch (AuthenticationFailure failure) {
            throw new WebApplicationException(401);
        }
    }

    @GET
    @Path("/books/reserved")
    @Produces({
            APPLICATION_VND_HERALD_LIB,
            MediaType.APPLICATION_XML
    })
    public Booklist getBooksReserved(@QueryParam("token") String token)
            throws IOException {
        checkParamNotNull(token);

        try {
            return libraryDataAccess.getBooksReservedByUser(token);
        } catch (AuthenticationFailure failure) {
            throw new WebApplicationException(401);
        }
    }

    @GET
    @Path("/books/history")
    @Produces({
            APPLICATION_VND_HERALD_LIB,
            MediaType.APPLICATION_XML
    })
    public Booklist getBorrowHistory(@QueryParam("token") String token)
            throws IOException {
        checkParamNotNull(token);

        try {
            return libraryDataAccess.getBorrowHistory(token);
        } catch (AuthenticationFailure failure) {
            throw new WebApplicationException(401);
        }
    }

    @GET
    @Path("/book/{id}")
    @Produces({
            APPLICATION_VND_HERALD_LIB,
            MediaType.APPLICATION_XML
    })
    public Book getBook(@PathParam("id") String id) throws IOException {
        checkParamNotNull(id);

        Book book = libraryDataAccess.getBookByMarcNo(id);
        if (book == null) {
            throw new NotFoundException(String.format(
                    "Book (macro: %s) not found", id));
        }
        return book;
    }

    @POST
    @Path("/book/{id}/reservation")
    @Produces({
            APPLICATION_VND_HERALD_LIB,
            MediaType.APPLICATION_XML
    })
    public Response reserve(@PathParam("id") String id,
                        @QueryParam("token") String token) throws IOException {
        checkParamNotNull(id);
        checkParamNotNull(token);

        try {
            boolean accepted = libraryDataAccess.reserveBookByMarcNo(id, token);
            return (accepted)
                    ? Response.status(Response.Status.ACCEPTED).build()
                    : Response.status(Response.Status.NOT_ACCEPTABLE).build();
        } catch (AuthenticationFailure failure) {
            throw new WebApplicationException(401);
        }
    }

    @POST
    @Path("/book/{id}/renewal")
    @Produces({
            APPLICATION_VND_HERALD_LIB,
            MediaType.APPLICATION_XML
    })
    public Response renew(@PathParam("id") String id,
                      @QueryParam("token") String token) throws IOException {
        checkParamNotNull(id);
        checkParamNotNull(token);

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
