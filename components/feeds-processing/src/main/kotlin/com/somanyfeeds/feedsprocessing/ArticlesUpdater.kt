package com.somanyfeeds.feedsprocessing

import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.articlesdataaccess.ArticlesDataGateway
import com.somanyfeeds.feedsdataaccess.Feed

public interface ArticlesUpdater {
    fun updateArticles(articles: List<Article>, feed: Feed)
}

public class DefaultArticlesUpdater(val articlesDataGateway: ArticlesDataGateway, val limit: Int): ArticlesUpdater {
    override fun updateArticles(articles: List<Article>, feed: Feed) {
        articlesDataGateway.removeAllByFeed(feed)

        var count = 0
        for (article in articles) {
            if (count >= limit) break

            articlesDataGateway.create(article, feed)
            count++
        }
    }
}
