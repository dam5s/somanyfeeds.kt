package com.somanyfeeds.feedsprocessing

import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.feedsdataaccess.Feed

public interface FeedProcessor {
    fun process(feed: Feed): List<Article>
}
