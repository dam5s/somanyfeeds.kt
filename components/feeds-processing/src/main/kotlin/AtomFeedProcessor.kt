package com.somanyfeeds.feedsprocessing

import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.feedsprocessing.FeedProcessor

class AtomFeedProcessor : FeedProcessor {
    override fun process(feed: Feed) {
        throw UnsupportedOperationException()
    }
}
