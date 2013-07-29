package cn.edu.seu.herald.ws.resource.aspect;

import cn.edu.seu.herald.ws.api.library.Book;
import cn.edu.seu.herald.ws.api.library.Booklist;
import cn.edu.seu.herald.ws.api.library.BooklistLinkType;
import cn.edu.seu.herald.ws.api.library.User;
import cn.edu.seu.herald.ws.resource.LibraryResource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
@Aspect
public class LibraryResourceAspect {

    @AfterReturning(
            pointcut =
                    "execution(" +
                    "cn.edu.seu.herald.ws.api.library.Booklist " +
                    "cn.edu.seu.herald.ws.resource.LibraryResource." +
                    "*(..))",
            returning = "result"
    )
    public void afterReturningBooklist(JoinPoint joinPoint, Object result) {
        if (result == null) {
            return;
        }
        Assert.isInstanceOf(Booklist.class, result);

        Booklist booklist = (Booklist) result;
        for (Book book : booklist.getBooks()) {
            addBookHref(book);
        }
    }

    @AfterReturning(
            pointcut =
                    "execution(" +
                    "cn.edu.seu.herald.ws.api.library.User " +
                    "cn.edu.seu.herald.ws.resource.LibraryResource." +
                    "*(..))",
            returning = "result"
    )
    public void afterReturningUser(JoinPoint joinPoint, Object result) {
        if (result == null) {
            return;
        }
        Assert.isInstanceOf(User.class, result);

        User user = (User) result;
        addUserBasedHref(user);
    }

    private void addBookHref(Book book) {
        Assert.notNull(book);

        String marcNo = book.getMarcNo();
        URI uri = UriBuilder.fromPath("./book/{id}").build(marcNo);
        book.setHref(uri.toString());
    }

    private void addUserBasedHref(User user) {
        Assert.notNull(user);

        String token = user.getToken();
        user.setBorrowed(getBooklistLinkType("./books/history", token));
        user.setReserving(getBooklistLinkType("./books/reserved", token));
        user.setBorrowing(getBooklistLinkType("./books/borrowing", token));
    }

    private BooklistLinkType getBooklistLinkType(String path, String token) {
        BooklistLinkType link = new BooklistLinkType();
        link.setType(LibraryResource.APPLICATION_VND_HERALD_LIB);
        URI uri = UriBuilder.fromPath(path)
                .queryParam("token", token)
                .build();
        link.setHref(uri.toString());
        return link;
    }
}
