import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.feedsdataaccess.FeedType
import java.time.ZonedDateTime

fun buildArticle(
    id: Long? = null,
    title: String = "My Article",
    link: String = "http://example.com/articles/my-article",
    content: String = "Hello World",
    date: ZonedDateTime = ZonedDateTime.parse("2010-01-02T03:04:05Z[UTC]")
) = Article(id, title, link, content, date)

fun buildFeed(
    id: Long? = null,
    name: String = "My Feed",
    slug: String = "my-feed",
    url: String = "http://example.com/feed/rss",
    type: FeedType = FeedType.RSS
) = Feed(id, name, slug, url, type)
