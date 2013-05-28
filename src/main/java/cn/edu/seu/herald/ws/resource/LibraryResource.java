package cn.edu.seu.herald.ws.resource;

import cn.edu.seu.herald.ws.api.library.Book;
import cn.edu.seu.herald.ws.api.library.Booklist;
import cn.edu.seu.herald.ws.api.library.User;
import cn.edu.seu.herald.ws.dao.LibraryDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
@Component
@Path("/library")
public class LibraryResource {

    protected static final String APPLICATION_VND_HERALD_LIB =
            "application/vnd.herald.library+xml";
    @Autowired
    private LibraryDataAccess libraryDataAccess;

    @POST
    @Path("/user")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public User login(@QueryParam("username") String username,
                      @QueryParam("password") String password) {
        return null;
    }
    @GET
    @Path("/books")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public Booklist searchBooks(@QueryParam("keyword") String keyword) {
        return null;
    }

    @GET
    @Path("/books/borrowed")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public Booklist getBooksBorrowed(@QueryParam("token") String token) {
        return null;
    }

    @GET
    @Path("/books/reserved")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public Booklist getBooksReserved(@QueryParam("token") String token) {
        return null;
    }

    @GET
    @Path("/books/history")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public Booklist getBorrowHistory(@QueryParam("token") String token) {
        return null;
    }

    @GET
    @Path("/book/{id}")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public Book getBook(@PathParam("id") String id) {
        return null;
    }

    @POST
    @Path("/book/{id}/reservation")
    public Response reserve(@PathParam("id") String id,
                        @QueryParam("token") String token) {
        return null;
    }

    @POST
    @Path("/book/{id}/renewal")
    public Response renew(@PathParam("id") String id,
                      @QueryParam("token") String token) {
        return null;
    }
}
