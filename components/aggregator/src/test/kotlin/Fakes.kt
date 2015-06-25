import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.articlesdataaccess.ArticlesDataGateway
import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.feedsdataaccess.FeedsDataGateway

class FakeArticlesDataGateway(var articles: List<Article> = emptyList()) : ArticlesDataGateway {

    override fun create(article: Article, feed: Feed) {
        throw UnsupportedOperationException()
    }

    var didSelectByFeedSlugs: Set<String>? = null
    override fun selectAllByFeedSlugs(slugs: Set<String>): List<Article> {
        didSelectByFeedSlugs = slugs
        return articles
    }

    override fun removeAllByFeed(feed: Feed) {
        throw UnsupportedOperationException()
    }
}

class FakeFeedsDataGateway(var feeds: List<Feed> = emptyList()): FeedsDataGateway {
    override fun selectAllFeeds(): List<Feed> = feeds
}
