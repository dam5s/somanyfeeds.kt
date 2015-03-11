package com.somanyfeeds.aggregator

import com.somanyeeds.aggregator.FeedsDataGateway
import com.somanyeeds.aggregator.FeedType
import com.somanyeeds.aggregator.FeedProcessor

class FeedsUpdater(
    val feedsDataGateway: FeedsDataGateway,
    val feedProcessors: Map<FeedType, FeedProcessor>
) : Runnable {

    override fun run() {
        for (feed in feedsDataGateway.selectFeeds()) {
            val processor = feedProcessors.get(feed.type)
            processor!!.process(feed)
        }
    }
}
