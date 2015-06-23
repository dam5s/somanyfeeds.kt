import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.articlesdataaccess.ArticlesDataGateway
import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.feedsdataaccess.FeedsDataGateway
import com.somanyfeeds.feedsprocessing.ArticlesUpdater
import com.somanyfeeds.feedsprocessing.FeedProcessor
import com.somanyfeeds.httpgateway.HttpGateway
import java.util.*

class FakeFeedsDataGateway(val feeds: List<Feed>) : FeedsDataGateway {
    override fun selectFeeds(): List<Feed> = feeds
}

class FakeFeedProcessor(var stubbedArticles: List<Article>) : FeedProcessor {
    var processedFeed: Feed? = null

    override fun process(feed: Feed): List<Article> {
        processedFeed = feed
        return stubbedArticles
    }
}

class FakeArticlesUpdater : ArticlesUpdater {
    var updatedArticlesLists: MutableList<List<Article>> = ArrayList()
    var updatedFeeds: MutableList<Feed> = ArrayList()

    override fun updateArticles(articles: List<Article>, feed: Feed) {
        updatedArticlesLists.add(articles)
        updatedFeeds.add(feed)
    }
}

class FakeHttpGateway: HttpGateway {
    var stubbedResponse: String? = null
    var queriedUrl: String? = null

    override fun get(url: String): String {
        queriedUrl = url
        return stubbedResponse!!
    }
}



class FakeArticlesDataGateway: ArticlesDataGateway {
    var feedsAndArticles: MutableMap<Feed, MutableList<Article>> = HashMap()

    override fun create(article: Article, feed: Feed) {
        val articles = feedsAndArticles.get(feed) ?: ArrayList()
        articles.add(article)
        feedsAndArticles.put(feed, articles)
    }

    override fun selectAll(): List<Article> {
        return feedsAndArticles.values().flatten()
    }

    override fun selectAllByFeed(feed: Feed): List<Article> {
        return feedsAndArticles.get(feed) ?: emptyList()
    }

    override fun selectAllByFeedSlugs(slugs: Set<String>): List<Article> {
        throw UnsupportedOperationException()
    }

    override fun removeAllByFeed(feed: Feed) {
        feedsAndArticles.put(feed, ArrayList())
    }
}
