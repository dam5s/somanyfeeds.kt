package com.somanyfeeds.feedsprocessing

import com.somanyfeeds.feedsdataaccess.FeedType
import com.somanyfeeds.feedsdataaccess.FeedsDataGateway
import javax.inject.Inject


class FeedsUpdater
    @Inject constructor(
        val feedsDataGateway: FeedsDataGateway,
        val articlesUpdater: ArticlesUpdater,
        val feedProcessors: Map<FeedType, FeedProcessor>
    ) : Runnable {

    override fun run() {
        for (feed in feedsDataGateway.selectAllFeeds()) {
            val processor = feedProcessors.get(feed.type)
            val articles = processor!!.process(feed)

            articlesUpdater.updateArticles(articles, feed)
        }
    }
}
