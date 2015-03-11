package com.somanyfeeds.feedsprocessing

import com.somanyeeds.feedsdataaccess.FeedsDataGateway
import com.somanyeeds.feedsdataaccess.FeedType
import com.somanyeeds.feedsprocessing.FeedProcessor


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
