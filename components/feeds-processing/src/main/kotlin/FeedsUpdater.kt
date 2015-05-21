package com.somanyfeeds.feedsprocessing

import com.somanyfeeds.feedsdataaccess.FeedsDataGateway
import com.somanyfeeds.feedsdataaccess.FeedType
import com.somanyfeeds.feedsprocessing.FeedProcessor


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
