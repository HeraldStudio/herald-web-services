package cn.edu.seu.herald.ws.dao;

import cn.edu.seu.herald.ws.api.update.Update;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
public interface AndroidClientUpdateDataAccess {

    Update getUpdateInfo() throws DataAccessException;
}