import FakeArticlesDataGateway
import TestHttpServletRequest
import TestHttpServletResponse
import com.somanyfeeds.aggregator.DefaultArticlesController
import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.articlesdataaccess.ArticlesDataGateway
import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.jsonserialization.ObjectMapperProvider
import org.jetbrains.spek.api.Spek
import java.time.ZonedDateTime
import kotlin.test.assertEquals

class ArticlesControllerSpec : Spek() { init {
    given("an ArticlesController and some articles available") {
        val fakeDataGateway = FakeArticlesDataGateway(articles = listOf(
            Article(
                title = "Some great article",
                link = "http://example.com/article-1",
                content = "Some great article content...",
                date = ZonedDateTime.parse("2010-01-30T02:03:04Z")
            ),
            Article(
                title = "Some other article",
                link = "http://example.com/article-2",
                content = "Some other article content...",
                date = ZonedDateTime.parse("2011-01-30T02:03:04Z")
            )
        ))
        val controller = DefaultArticlesController(articlesDataGateway = fakeDataGateway)
        val objectMapper = ObjectMapperProvider().get()


        on("GET /articles") {
            val listArticlesReq = TestHttpServletRequest(
                path = "/articles",
                headers = mapOf("Accept" to listOf("application/json"))
            )
            val listArticlesResp = TestHttpServletResponse()

            controller.listArticles(listArticlesReq, listArticlesResp)


            it("renders articles from the gateway as JSON") {
                val articles = objectMapper.readValue(listArticlesResp.getBody(), javaClass<List<Map<String, String>>>())
                assertEquals(2, articles.size())

                assertEquals("Some great article", articles[0]["title"])
                assertEquals("http://example.com/article-1", articles[0]["link"])
                assertEquals("Some great article content...", articles[0]["content"])
                assertEquals("2010-01-30T02:03:04Z", articles[0]["date"])

                assertEquals("Some other article", articles[1]["title"])
                assertEquals("http://example.com/article-2", articles[1]["link"])
                assertEquals("Some other article content...", articles[1]["content"])
                assertEquals("2011-01-30T02:03:04Z", articles[1]["date"])
            }

            it("sets the content-type") {
                assertEquals("application/json", listArticlesResp.getContentType())
            }
        }
    }
}}

class FakeArticlesDataGateway(var articles: List<Article> = emptyList()) : ArticlesDataGateway {

    var createdArticle: Article? = null
    var createdArticleForFeed: Feed? = null
    override fun create(article: Article, feed: Feed) {
        createdArticle = article
        createdArticleForFeed = feed
    }

    override fun selectAll(): List<Article> = articles

    var selectedFeed: Feed? = null
    override fun selectAllByFeed(feed: Feed): List<Article> {
        selectedFeed = feed
        return articles
    }

    override fun removeAllByFeed(feed: Feed) {
        throw UnsupportedOperationException()
    }
}
