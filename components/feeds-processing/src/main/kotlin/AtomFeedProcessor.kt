package com.somanyfeeds.feedsprocessing

import com.somanyeeds.feedsdataaccess.Feed
import com.somanyeeds.feedsprocessing.FeedProcessor

class AtomFeedProcessor : FeedProcessor {
    override fun process(feed: Feed) {
        throw UnsupportedOperationException()
    }
}
