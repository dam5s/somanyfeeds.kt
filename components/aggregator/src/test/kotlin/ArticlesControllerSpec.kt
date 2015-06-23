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
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ArticlesControllerSpec : Spek() { init {
    val objectMapper = ObjectMapperProvider().get()

    given("some articles available") {
        val availableArticles = listOf(
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
        )

        on("GET / with Accept application/json") {
            val fakeDataGateway = FakeArticlesDataGateway(articles = availableArticles)
            val controller = DefaultArticlesController(articlesDataGateway = fakeDataGateway)
            val listArticlesAsJsonReq = TestHttpServletRequest(
                path = "/",
                headers = mapOf("Accept" to listOf("application/json"))
            )
            val listArticlesResp = TestHttpServletResponse()

            controller.listArticles(listArticlesAsJsonReq, listArticlesResp)

            val didSelectAll = fakeDataGateway.didSelectAll
            val didSelectByFeedSlugs = fakeDataGateway.didSelectByFeedSlugs

            it("renders articles from the gateway as JSON") {
                assertFalse(didSelectAll)
                assertEquals(setOf("gplus", "pivotal"), didSelectByFeedSlugs)

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

        on("GET / with Accept text/html") {
            val fakeDataGateway = FakeArticlesDataGateway(articles = availableArticles)
            val controller = DefaultArticlesController(articlesDataGateway = fakeDataGateway)
            val listArticlesAsHtmlReq = TestHttpServletRequest(
                path = "/",
                headers = mapOf("Accept" to listOf("text/html"))
            )
            val listArticlesResp = TestHttpServletResponse()

            controller.listArticles(listArticlesAsHtmlReq, listArticlesResp)

            val didSelectAll = fakeDataGateway.didSelectAll
            val didSelectByFeedSlugs = fakeDataGateway.didSelectByFeedSlugs


            it("gets articles by default feeds and sets them onto the request") {
                val articles = listArticlesAsHtmlReq.getAttribute("articles") as? List<Article>

                assertFalse(didSelectAll)
                assertEquals(setOf("gplus", "pivotal"), didSelectByFeedSlugs)
                assertEquals(availableArticles, articles)
            }

            it("forwards the request to articles.jsp") {
                assertEquals("/WEB-INF/articles.jsp", listArticlesAsHtmlReq.requestDispatcher.path)
                assertEquals(listArticlesAsHtmlReq, listArticlesAsHtmlReq.requestDispatcher.forwardedReq)
                assertEquals(listArticlesResp, listArticlesAsHtmlReq.requestDispatcher.forwardedResp)
            }
        }

        on("GET /my-feed,my-other-feed with Accept text/html") {
            val fakeDataGateway = FakeArticlesDataGateway(articles = availableArticles)
            val controller = DefaultArticlesController(articlesDataGateway = fakeDataGateway)
            val listArticlesAsHtmlReq = TestHttpServletRequest(
                path = "/my-feed,my-other-feed",
                headers = mapOf("Accept" to listOf("text/html"))
            )
            val listArticlesResp = TestHttpServletResponse()

            controller.listArticles(listArticlesAsHtmlReq, listArticlesResp)

            val didSelectAll = fakeDataGateway.didSelectAll
            val didSelectByFeedSlugs = fakeDataGateway.didSelectByFeedSlugs

            it("gets articles by feeds and sets them onto the request") {
                val articles = listArticlesAsHtmlReq.getAttribute("articles") as? List<Article>

                assertFalse(didSelectAll)
                assertEquals(setOf("my-feed", "my-other-feed"), didSelectByFeedSlugs)
                assertEquals(availableArticles, articles)
            }

            it("forwards the request to articles.jsp") {
                assertEquals("/WEB-INF/articles.jsp", listArticlesAsHtmlReq.requestDispatcher.path)
                assertEquals(listArticlesAsHtmlReq, listArticlesAsHtmlReq.requestDispatcher.forwardedReq)
                assertEquals(listArticlesResp, listArticlesAsHtmlReq.requestDispatcher.forwardedResp)
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

    var didSelectAll = false
    override fun selectAll(): List<Article> {
        didSelectAll = true
        return articles
    }

    var didSelectByFeed: Feed? = null
    override fun selectAllByFeed(feed: Feed): List<Article> {
        didSelectByFeed = feed
        return articles
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
