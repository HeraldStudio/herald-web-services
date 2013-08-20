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
    public void addLinksToAvailableFeedsReturned(JoinPoint joinPoint,
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
