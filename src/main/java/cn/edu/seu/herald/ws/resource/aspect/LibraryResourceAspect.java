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
    public void addLinksTogBooklistReturned(JoinPoint joinPoint, Object result) {
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
    public void addLinkToUserReturned(JoinPoint joinPoint, Object result) {
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
        URI uri = UriBuilder.fromPath("/library/book/{id}").build(marcNo);
        book.setHref(uri.toString());
    }

    private void addUserBasedHref(User user) {
        Assert.notNull(user);

        String token = user.getToken();
        user.setBorrowed(getBooklistLinkType("/library/books/history",
                token));
        user.setReserving(getBooklistLinkType("/library/books/reserved",
                token));
        user.setBorrowing(getBooklistLinkType("/library/books/borrowing",
                token));
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
