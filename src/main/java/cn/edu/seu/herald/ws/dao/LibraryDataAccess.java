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
}
