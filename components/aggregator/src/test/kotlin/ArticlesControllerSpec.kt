import FakeArticlesDataGateway
import FakeFeedsDataGateway
import TestHttpServletRequest
import TestHttpServletResponse
import com.somanyfeeds.aggregator.DefaultArticlesController
import com.somanyfeeds.aggregator.FeedPresenter
import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.jsonserialization.ObjectMapperProvider
import org.jetbrains.spek.api.Spek
import java.time.ZonedDateTime
import kotlin.test.assertEquals

class ArticlesControllerSpec : Spek() { init {
    val objectMapper = ObjectMapperProvider().get()

    given("some articles available") {
        val availableArticles = listOf(
            Article(
                title = "Some great article",
                link = "http://example.com/article-1",
                content = "Some great article content...",
                date = ZonedDateTime.parse("2010-01-30T02:03:10Z")
            ),
            Article(
                title = "Some other article",
                link = "http://example.com/article-2",
                content = "Some other article content...",
                date = ZonedDateTime.parse("2011-01-30T02:03:11Z")
            )
        )

        on("GET / with Accept application/json") {
            val fakeArticlesDataGateway = FakeArticlesDataGateway(availableArticles)
            val fakeFeedsDataGateway = FakeFeedsDataGateway()
            val controller = DefaultArticlesController(fakeArticlesDataGateway, fakeFeedsDataGateway, FeedPresenter())

            val listArticlesAsJsonReq = TestHttpServletRequest(
                path = "/",
                headers = mapOf("Accept" to listOf("application/json"))
            )
            val listArticlesResp = TestHttpServletResponse()

            controller.listArticles(listArticlesAsJsonReq, listArticlesResp)

            val didSelectByFeedSlugs = fakeArticlesDataGateway.didSelectByFeedSlugs

            it("renders articles from the gateway as JSON") {
                assertEquals(setOf("gplus", "pivotal"), didSelectByFeedSlugs)

                val articles = objectMapper.readValue(listArticlesResp.getBody(), javaClass<List<Map<String, String>>>())
                assertEquals(2, articles.size())

                assertEquals("Some other article", articles[0]["title"])
                assertEquals("http://example.com/article-2", articles[0]["link"])
                assertEquals("Some other article content...", articles[0]["content"])
                assertEquals("2011-01-30T02:03:11Z", articles[0]["date"])

                assertEquals("Some great article", articles[1]["title"])
                assertEquals("http://example.com/article-1", articles[1]["link"])
                assertEquals("Some great article content...", articles[1]["content"])
                assertEquals("2010-01-30T02:03:10Z", articles[1]["date"])
            }

            it("sets the content-type") {
                assertEquals("application/json", listArticlesResp.getContentType())
            }
        }

        on("GET / with Accept text/html") {
            val fakeArticlesDataGateway = FakeArticlesDataGateway(availableArticles)
            val fakeFeedsDataGateway = FakeFeedsDataGateway()
            val controller = DefaultArticlesController(fakeArticlesDataGateway, fakeFeedsDataGateway, FeedPresenter())
            val listArticlesAsHtmlReq = TestHttpServletRequest(
                path = "/",
                headers = mapOf("Accept" to listOf("text/html"))
            )
            val listArticlesResp = TestHttpServletResponse()

            controller.listArticles(listArticlesAsHtmlReq, listArticlesResp)

            val didSelectByFeedSlugs = fakeArticlesDataGateway.didSelectByFeedSlugs


            it("gets articles by default feeds and sets them onto the request") {
                val articles = listArticlesAsHtmlReq.getAttribute("articles") as List<Article>

                assertEquals(setOf("gplus", "pivotal"), didSelectByFeedSlugs)
                assertEquals(listOf(availableArticles[1], availableArticles[0]), articles)
            }

            it("forwards the request to articles.jsp") {
                assertEquals("/WEB-INF/articles.jsp", listArticlesAsHtmlReq.requestDispatcher.path)
                assertEquals(listArticlesAsHtmlReq, listArticlesAsHtmlReq.requestDispatcher.forwardedReq)
                assertEquals(listArticlesResp, listArticlesAsHtmlReq.requestDispatcher.forwardedResp)
            }
        }

        on("GET /my-feed,my-other-feed with Accept text/html") {
            val fakeArticlesDataGateway = FakeArticlesDataGateway(availableArticles)
            val fakeFeedsDataGateway = FakeFeedsDataGateway()
            val controller = DefaultArticlesController(fakeArticlesDataGateway, fakeFeedsDataGateway, FeedPresenter())
            val listArticlesAsHtmlReq = TestHttpServletRequest(
                path = "/my-feed,my-other-feed",
                headers = mapOf("Accept" to listOf("text/html"))
            )
            val listArticlesResp = TestHttpServletResponse()

            controller.listArticles(listArticlesAsHtmlReq, listArticlesResp)

            val didSelectByFeedSlugs = fakeArticlesDataGateway.didSelectByFeedSlugs

            it("gets articles by feeds and sets them onto the request") {
                val articles = listArticlesAsHtmlReq.getAttribute("articles") as? List<Article>

                assertEquals(setOf("my-feed", "my-other-feed"), didSelectByFeedSlugs)
                assertEquals(listOf(availableArticles[1], availableArticles[0]), articles)
            }

            it("forwards the request to articles.jsp") {
                assertEquals("/WEB-INF/articles.jsp", listArticlesAsHtmlReq.requestDispatcher.path)
                assertEquals(listArticlesAsHtmlReq, listArticlesAsHtmlReq.requestDispatcher.forwardedReq)
                assertEquals(listArticlesResp, listArticlesAsHtmlReq.requestDispatcher.forwardedResp)
            }
        }
    }
}}
