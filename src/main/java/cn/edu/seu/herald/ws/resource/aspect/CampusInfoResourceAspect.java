package cn.edu.seu.herald.ws.resource.aspect;

import org.apache.wink.common.model.rss.RssChannel;
import org.apache.wink.common.model.rss.RssFeed;
import org.apache.wink.common.model.rss.RssItem;
import org.apache.wink.common.model.synd.SyndEntry;
import org.apache.wink.common.model.synd.SyndFeed;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.Assert;

import javax.ws.rs.core.UriBuilder;
import java.util.List;

@Aspect
public class CampusInfoResourceAspect {

    @AfterReturning(
            pointcut =
                    "execution(" +
                        "org.apache.wink.common.model.rss.RssFeed " +
                        "cn.edu.seu.herald.ws.resource.CampusInfoResource." +
                        "getAvailableFeeds(..))",
            returning = "result"
    )
    public void afterReturningAvailableFeeds(JoinPoint joinPoint,
                                             Object result) {
        if (result == null) {
            return;
        }
        Assert.isInstanceOf(RssFeed.class, result);
        RssFeed rssFeed = (RssFeed) result;
        RssChannel rssChannel = rssFeed.getChannel();
        if (rssChannel == null) {
            return;
        }

        List<RssItem> rssItems = rssChannel.getItems();
        SyndFeed syndFeed = new SyndFeed();
        rssFeed.toSynd(syndFeed);

        List<SyndEntry> syndEntryList = syndFeed.getEntries();
        Assert.state(syndEntryList.size() == rssItems.size());

        for (int index = 0; index < syndEntryList.size(); ++index) {
            SyndEntry syndEntry = syndEntryList.get(index);
            String name = syndEntry.getId();
            RssItem item = rssItems.get(index);
            item.setLink(getLinkByName(name));
        }
    }

    private String getLinkByName(String name) {
        return UriBuilder.fromPath("/campus/{name}").build(name).toString();
    }
}
