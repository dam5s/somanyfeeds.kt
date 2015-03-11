package com.somanyfeeds.aggregator

import com.somanyeeds.aggregator.FeedProcessor
import com.somanyeeds.aggregator.Feed

class AtomFeedProcessor : FeedProcessor {
    override fun process(feed: Feed) {
        throw UnsupportedOperationException()
    }
}
