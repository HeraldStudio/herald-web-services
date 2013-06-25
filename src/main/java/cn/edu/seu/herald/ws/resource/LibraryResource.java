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

    protected static final String APPLICATION_VND_HERALD_LIB =
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
                                @DefaultValue("1") int page) {
        Booklist result = libraryDataAccess.search(keyword, page);
        return result;
    }

    @GET
    @Path("/books/borrowed")
    @Produces(APPLICATION_VND_HERALD_LIB)
    public Booklist getBooksBorrowed(@QueryParam("token") String token,
                                     @Context HttpServletResponse response)
            throws IOException {
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
    public Book getBook(@PathParam("id") String id) {
        return libraryDataAccess.getBookByMarcNo(id);
    }

    @POST
    @Path("/book/{id}/reservation")
    public Response reserve(@PathParam("id") String id,
                        @QueryParam("token") String token,
                        @Context HttpServletResponse response)
            throws IOException {
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
                      @QueryParam("token") String token) {
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
