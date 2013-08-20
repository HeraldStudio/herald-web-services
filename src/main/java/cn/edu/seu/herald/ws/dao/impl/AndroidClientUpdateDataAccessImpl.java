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
package cn.edu.seu.herald.ws.dao.impl;

import cn.edu.seu.herald.ws.api.update.Update;
import cn.edu.seu.herald.ws.dao.AndroidClientUpdateDataAccess;
import cn.edu.seu.herald.ws.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Properties;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
@Repository("androidClientUpdateDataAccess")
public class AndroidClientUpdateDataAccessImpl
        implements AndroidClientUpdateDataAccess, ServletContextAware {
    private static final String UPDATE_INFO_PATH =
            "/WEB-INF/android-client-update.properties";
    private File updateInfo;

    @Override
    public void setServletContext(ServletContext servletContext) {
        updateInfo = new File(servletContext.getRealPath(UPDATE_INFO_PATH));
    }

    @Override
    public Update getUpdateInfo() throws DataAccessException {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(updateInfo);
            InputStreamReader reader = new InputStreamReader(in, "utf-8");
            properties.load(reader);
            String version = properties.getProperty(
                    "ezherald.version", "unknown");
            String uri = properties.getProperty("ezherald.uri", "unknown");
            String info = properties.getProperty("ezherald.info", "");
            String forceStr = properties.getProperty("ezherald.force", "false");
            boolean force = Boolean.parseBoolean(forceStr);

            Update update = new Update();
            update.setVersion(version);
            update.setUri(uri);
            update.setInfo(info);
            update.setForce(force);
            return update;
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
            safeClose(in);
        }
    }

    private void safeClose(InputStream in) throws DataAccessException {
        if (in == null) {
            return;
        }

        try {
            in.close();
        } catch (IOException ex) {
            throw new DataAccessException(ex);
        }
    }
}
