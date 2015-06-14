package com.somanyfeeds.feedsprocessing

import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.articlesdataaccess.ArticlesDataGateway
import com.somanyfeeds.feedsdataaccess.Feed

public trait ArticlesUpdater {
    fun updateArticles(articles: List<Article>, feed: Feed)
}

public class DefaultArticlesUpdater(val articlesDataGateway: ArticlesDataGateway): ArticlesUpdater {
    override fun updateArticles(articles: List<Article>, feed: Feed) {
        for (article in articles) {
            articlesDataGateway.create(article, feed)
        }
    }
}
