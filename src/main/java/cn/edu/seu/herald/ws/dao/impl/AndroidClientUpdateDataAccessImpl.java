package cn.edu.seu.herald.ws.dao.impl;

import cn.edu.seu.herald.ws.api.update.Update;
import cn.edu.seu.herald.ws.dao.AndroidClientUpdateDataAccess;
import cn.edu.seu.herald.ws.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
            properties.load(in);
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
