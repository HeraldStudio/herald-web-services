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

import cn.edu.seu.herald.ws.dao.CampusInfoDataAccess;
import cn.edu.seu.herald.ws.dao.DataAccessException;
import org.apache.wink.common.model.synd.SyndEntry;
import org.apache.wink.common.model.synd.SyndFeed;
import org.apache.wink.common.model.synd.SyndLink;
import org.apache.wink.common.model.synd.SyndText;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class CampusInfoDataAccessImpl extends AbstractDBDataAccess
        implements CampusInfoDataAccess {

    private static final String GET_FEED_BY_NAME =
            "SELECT uuid, title, url, updated "
            + "FROM `herald_campus_info`.`feed` WHERE name=?;";
    private static final String GET_FEED_BY_NAME_AFTER_UUID =
            "SELECT uuid, title, url, updated "
            + "FROM `herald_campus_info`.`feed` "
            + "WHERE name=? AND updated > ("
            + "SELECT updated FROM `herald_campus_info`.`feed` WHERE uuid=?);";
    private static final String CONTAINS_FEED =
            "SELECT COUNT(1) FROM `herald_campus_info`.`feed` WHERE name=?";
    private static final String GET_ENTRIES_BY_FEED_UUID =
            "SELECT uuid, title, url, updated, summary "
            + "FROM `herald_campus_info`.`entry` WHERE feed_uuid=?";
    private static final String GET_LATEST_UUID_BY_NAME =
            "SELECT MAX(uuid) FROM `herald_campus_info`.`feed` WHERE name=?;";
    private static final String GET_FEED_NAMES =
            "SELECT title, name FROM `herald_campus_info`.`feed`;";

    public CampusInfoDataAccessImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public SyndFeed getFeedByName(String name) throws DataAccessException {
        Connection connection = getConnection();
        try {
            PreparedStatement ps1 = connection.prepareStatement(
                    GET_FEED_BY_NAME);
            ps1.setString(1, name);
            PreparedStatement ps2 = connection.prepareStatement(
                    GET_ENTRIES_BY_FEED_UUID);
            return getSyndFeed(ps1, ps2);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public SyndFeed getFeedByName(String name, String afterUUID)
            throws DataAccessException {
        Connection connection = getConnection();
        try {
            PreparedStatement ps1 = connection.prepareStatement(
                    GET_FEED_BY_NAME_AFTER_UUID);
            ps1.setString(1, name);
            ps1.setString(2, afterUUID);
            PreparedStatement ps2 = connection.prepareStatement(
                    GET_ENTRIES_BY_FEED_UUID);
            return getSyndFeed(ps1, ps2);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public boolean containsFeed(String name) throws DataAccessException {
        Connection connection = getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    CONTAINS_FEED);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new DataAccessException("No result set returned");
            }
            return rs.getInt(1) > 0;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public String getLatestUUID(String name) throws DataAccessException {
        Connection connection = getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    GET_LATEST_UUID_BY_NAME);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return rs.getString(1);
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    @Override
    public SyndFeed getAvailableFeeds() throws DataAccessException {
        Connection connection = getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(GET_FEED_NAMES);
            ResultSet rs = ps.executeQuery();
            SyndFeed feed = new SyndFeed();
            List<SyndEntry> entries = feed.getEntries();
            while (rs.next()) {
                SyndEntry entry = new SyndEntry();
                entry.setTitle(new SyndText(rs.getString("title")));
                entry.setId(rs.getString("name"));
                entries.add(entry);
            }
            return feed;
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        } finally {
            closeConnection(connection);
        }
    }

    private SyndFeed getSyndFeed(PreparedStatement feed,
                                 PreparedStatement entries)
            throws SQLException {
        ResultSet feedRs = feed.executeQuery();
        if (!feedRs.next()) {
            return null;
        }
        String uuid = feedRs.getString("uuid");
        String title = feedRs.getString("title");
        String url = feedRs.getString("url");
        Date updated = feedRs.getDate("updated");

        SyndFeed syndFeed = new SyndFeed();
        syndFeed.setId(uuid);
        syndFeed.setTitle(new SyndText(title));
        syndFeed.setUpdated(updated);
        syndFeed.addLink(getAltLink(url));

        entries.setString(1, uuid);
        ResultSet entriesRs = entries.executeQuery();
        while (entriesRs.next()) {
            SyndEntry entry = new SyndEntry();
            entry.setId(getUrnUuid(entriesRs.getString("uuid")));
            entry.setTitle(new SyndText(entriesRs.getString("title")));
            entry.addLink(getAltLink(entriesRs.getString("url")));
            entry.setPublished(entriesRs.getDate("updated"));
            entry.setSummary(new SyndText(entriesRs.getString("summary")));
            syndFeed.addEntry(entry);
        }
        return syndFeed;
    }

    private String getUrnUuid(String uuid) {
        return uuid;
    }

    private SyndLink getAltLink(String url) {
        return new SyndLink("alternate", "text/html", url);
    }
}
