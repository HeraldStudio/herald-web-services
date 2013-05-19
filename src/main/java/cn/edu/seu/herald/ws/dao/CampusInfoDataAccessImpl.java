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

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.wink.common.model.synd.SyndEntry;
import org.apache.wink.common.model.synd.SyndFeed;
import org.apache.wink.common.model.synd.SyndLink;
import org.apache.wink.common.model.synd.SyndText;

/**
 *
 * @author rAy <predator.ray@gmail.com>
 */
public class CampusInfoDataAccessImpl extends AbstractDataAccess
        implements CampusInfoDataAccess {

    private static final String GET_FEED_BY_NAME =
            "SELECT uuid, title, url, updated "
            + "FROM `herald_campus_info`.`feed` WHERE name=?;";
    private static final String GET_FEED_BY_NAME_AFTER_UUID =
            "SELECT uuid, title, url, updated "
            + "FROM `herald_campus_info`.`feed` "
            + "WHERE name=? AND updated > ("
            + "SELECT updated FROM `herald_campus_info`.`feed` WHERE uuid=?);";
    private static final String GET_ENTRIES_BY_FEED_UUID =
            "SELECT uuid, title, url, updated, summary "
            + "FROM `herald_campus_info`.`entry` WHERE feed_uuid=?";
    private static final String GET_LATEST_UUID_BY_NAME =
            "SELECT MAX(uuid) FROM `herald_campus_info`.`feed` WHERE name=?;";

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
            ResultSet rs1 = ps1.executeQuery();
            if (!rs1.next()) {
                return null;
            }
            String uuid = rs1.getString("uuid");
            String title = rs1.getString("title");
            String url = rs1.getString("url");
            Date updated = rs1.getDate("updated");

            SyndFeed syndFeed = new SyndFeed();
            syndFeed.setId(getUrnUuid(uuid));
            syndFeed.setTitle(new SyndText(title));
            syndFeed.setUpdated(updated);
            syndFeed.addLink(getAltLink(url));

            PreparedStatement ps2 = connection.prepareStatement(
                    GET_ENTRIES_BY_FEED_UUID);
            ps2.setString(1, uuid);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                SyndEntry entry = new SyndEntry();
                entry.setId(getUrnUuid(rs2.getString("uuid")));
                entry.setTitle(new SyndText(rs2.getString("title")));
                entry.setPublished(rs2.getDate("updated"));
                entry.setSummary(new SyndText(rs2.getString("summary")));
                syndFeed.addEntry(entry);
            }
            return syndFeed;
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
            ResultSet rs1 = ps1.executeQuery();
            if (!rs1.next()) {
                return null;
            }
            String uuid = rs1.getString("uuid");
            String title = rs1.getString("title");
            String url = rs1.getString("url");
            Date updated = rs1.getDate("updated");

            SyndFeed syndFeed = new SyndFeed();
            syndFeed.setId(getUrnUuid(uuid));
            syndFeed.setTitle(new SyndText(title));
            syndFeed.setUpdated(updated);
            syndFeed.addLink(getAltLink(url));

            PreparedStatement ps2 = connection.prepareStatement(
                    GET_ENTRIES_BY_FEED_UUID);
            ps2.setString(1, uuid);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                SyndEntry entry = new SyndEntry();
                entry.setId(getUrnUuid(rs2.getString("uuid")));
                entry.setTitle(new SyndText(rs2.getString("title")));
                entry.setPublished(rs2.getDate("updated"));
                entry.setSummary(new SyndText(rs2.getString("summary")));
                syndFeed.addEntry(entry);
            }
            return syndFeed;
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

    private String getUrnUuid(String uuid) {
        return "urn:uuid:" + uuid;
    }

    private SyndLink getAltLink(String url) {
        return new SyndLink(null, "text/html", url);
    }
}
