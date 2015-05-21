package com.somanyfeeds.feedsprocessing

import com.somanyfeeds.feedsdataaccess.Feed

public trait FeedProcessor {
    fun process(feed: Feed)
}
