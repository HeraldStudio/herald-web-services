package cn.edu.seu.herald.ws.resource;

import cn.edu.seu.herald.ws.dao.CampusInfoDataAccess;
import com.sun.jersey.api.NotFoundException;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;

public class CampusInfoResourceTest {

    @Tested
    private CampusInfoResource campusInfoResource;
    @Injectable
    private CampusInfoDataAccess campusInfoDataAccess;

    @Test
    public void testGetAvailableFeeds() throws Exception {
        new Expectations() {
            {
                campusInfoDataAccess.getAvailableFeeds();
                times = 1;
            }
        };
        campusInfoResource.getAvailableFeeds();
    }

    @Test(expected = WebApplicationException.class)
    public void testGetRssFeedByNameCheckNameNotNull() throws Exception {
        final String feedNameToBeCalled = null;
        final String beforeUuidToBeCalled = "123456";
        final String afterUuidToBeCalled = null;
        final int limitToBeCalled = 10;

        campusInfoResource.getRssFeedByName(feedNameToBeCalled,
                beforeUuidToBeCalled, afterUuidToBeCalled, limitToBeCalled);
    }

    @Test(expected = NotFoundException.class)
    public void testGetRssFeedByNameNotFound() throws Exception {
        final String feedNameToBeCalled = "jwc";
        final String beforeUuidToBeCalled = "123456";
        final String afterUuidToBeCalled = null;
        final int limitToBeCalled = 10;

        new Expectations() {
            {
                campusInfoDataAccess.containsFeed(feedNameToBeCalled);
                times = 1;
                result = false;
            }
        };
        campusInfoResource.getRssFeedByName(feedNameToBeCalled,
                beforeUuidToBeCalled, afterUuidToBeCalled, limitToBeCalled);
    }

    @Test
    public void testGetRssFeedByNameBefore() throws Exception {
        final String feedNameToBeCalled = "jwc";
        final String beforeUuidToBeCalled = "123456";
        final String afterUuidToBeCalled = null;
        final int limitToBeCalled = 10;

        new Expectations() {
            {
                campusInfoDataAccess.containsFeed(feedNameToBeCalled);
                times = 1;
                result = true;

                campusInfoDataAccess.getFeedBeforeByName(feedNameToBeCalled,
                        beforeUuidToBeCalled, limitToBeCalled);
                times = 1;
            }
        };
        campusInfoResource.getRssFeedByName(feedNameToBeCalled,
                beforeUuidToBeCalled, afterUuidToBeCalled, limitToBeCalled);
    }

    @Test
    public void testGetRssFeedByNameAfter() throws Exception {
        final String feedNameToBeCalled = "jwc";
        final String beforeUuidToBeCalled = null;
        final String afterUuidToBeCalled = "123456";
        final int limitToBeCalled = 10;

        new Expectations() {
            {
                campusInfoDataAccess.containsFeed(feedNameToBeCalled);
                times = 1;
                result = true;

                campusInfoDataAccess.getFeedAfterByName(feedNameToBeCalled,
                        afterUuidToBeCalled, limitToBeCalled);
                times = 1;
            }
        };
        campusInfoResource.getRssFeedByName(feedNameToBeCalled,
                beforeUuidToBeCalled, afterUuidToBeCalled, limitToBeCalled);
    }

    @Test(expected = WebApplicationException.class)
    public void testGetRssFeedByNameBetween() throws Exception {
        final String feedNameToBeCalled = "jwc";
        final String beforeUuidToBeCalled = "123";
        final String afterUuidToBeCalled = "456";
        final int limitToBeCalled = 10;

        new Expectations() {
            {
                campusInfoDataAccess.containsFeed(feedNameToBeCalled);
                times = 1;
                result = true;
            }
        };
        campusInfoResource.getRssFeedByName(feedNameToBeCalled,
                beforeUuidToBeCalled, afterUuidToBeCalled, limitToBeCalled);
    }

    @Test
    public void testGetRssFeedByNameDefault() throws Exception {
        final String feedNameToBeCalled = "jwc";
        final String beforeUuidToBeCalled = null;
        final String afterUuidToBeCalled = null;
        final int limitToBeCalled = 10;

        new Expectations() {
            {
                campusInfoDataAccess.containsFeed(feedNameToBeCalled);
                times = 1;
                result = true;

                campusInfoDataAccess.getFeedByName(feedNameToBeCalled,
                        limitToBeCalled);
                times = 1;
            }
        };
        campusInfoResource.getRssFeedByName(feedNameToBeCalled,
                beforeUuidToBeCalled, afterUuidToBeCalled, limitToBeCalled);
    }
}
