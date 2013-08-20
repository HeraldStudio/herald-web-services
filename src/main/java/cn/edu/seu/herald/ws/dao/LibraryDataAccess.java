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
package cn.edu.seu.herald.ws.dao;

import cn.edu.seu.herald.ws.api.library.Book;
import cn.edu.seu.herald.ws.api.library.Booklist;
import cn.edu.seu.herald.ws.api.library.User;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
public interface LibraryDataAccess {

    Booklist search(String keyword, int page) throws DataAccessException;

    Book getBookByMarcNo(String marcNo) throws DataAccessException;

    User getUserIfAuthenticated(String username, String password)
            throws DataAccessException;

    User getUserWithToken(String token) throws DataAccessException;

    Booklist getBooksBorrowedByUser(String token)
            throws AuthenticationFailure, DataAccessException;

    Booklist getBooksReservedByUser(String token)
            throws AuthenticationFailure, DataAccessException;

    Booklist getBorrowHistory(String token)
            throws AuthenticationFailure, DataAccessException;

    boolean reserveBookByMarcNo(String marcNo, String token)
            throws AuthenticationFailure, DataAccessException;

    boolean renewBookByMarcNo(String marcNo, String token)
            throws AuthenticationFailure, DataAccessException;
}
