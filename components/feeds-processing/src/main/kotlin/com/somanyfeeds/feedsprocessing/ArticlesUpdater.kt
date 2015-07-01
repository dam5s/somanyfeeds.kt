package com.somanyfeeds.feedsprocessing

import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.articlesdataaccess.ArticlesDataGateway
import com.somanyfeeds.feedsdataaccess.Feed
import javax.inject.Inject

public interface ArticlesUpdater {
    fun updateArticles(articles: List<Article>, feed: Feed)
}

public class DefaultArticlesUpdater
    @Inject constructor(val articlesDataGateway: ArticlesDataGateway, val articlesLimit: Int): ArticlesUpdater {

    override fun updateArticles(articles: List<Article>, feed: Feed) {
        articlesDataGateway.removeAllByFeed(feed)

        var count = 0
        for (article in articles) {
            if (count >= articlesLimit) break

            articlesDataGateway.create(article, feed)
            count++
        }
    }
}
