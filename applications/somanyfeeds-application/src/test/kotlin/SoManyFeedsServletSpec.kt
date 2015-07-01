import FakeFeedUpdatesScheduler
import TestHttpServletRequest
import TestHttpServletResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.somanyfeeds.aggregator.ArticlesController
import com.somanyfeeds.application.SoManyFeedsComponent
import com.somanyfeeds.application.SoManyFeedsServlet
import com.somanyfeeds.feedsprocessing.FeedUpdatesScheduler
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SoManyFeedsServletSpec : Spek() { init {
    given("a SoManyFeedsServlet") {
        val articlesController = SoManyFeedsComponent.build().articlesController()
        val fakeScheduler = FakeFeedUpdatesScheduler()
        val servlet = SoManyFeedsServlet(FakeSoManyFeedsComponent(articlesController, fakeScheduler))

        on("init") {
            it("starts the feed updates scheduler") {
                assertTrue(fakeScheduler.didStart)
            }
        }

        on("destroy") {
            servlet.destroy()

            it("stops the scheduler") {
                assertTrue(fakeScheduler.didStop)
            }
        }

        on("articles JSON request") {
            val reqHeaders = mapOf("Accept" to listOf("application/json"))
            val articlesReq = TestHttpServletRequest(path = "/", headers = reqHeaders)
            val articlesResp = TestHttpServletResponse()

            servlet.doGet(articlesReq, articlesResp)

            it("returns articles JSON") {
                val articles = ObjectMapper().readValue(articlesResp.getBody(), javaClass<List<Map<String, String>>>())

                assertEquals(articlesResp.getContentType(), "application/json")
                assertTrue(articles.size() >= 0)
            }
        }
    }
}}

class FakeSoManyFeedsComponent(val articlesController: ArticlesController, val feedUpdatesScheduler: FeedUpdatesScheduler) : SoManyFeedsComponent {
    override fun articlesController(): ArticlesController = articlesController

    override fun feedUpdatesScheduler(): FeedUpdatesScheduler = feedUpdatesScheduler
}

class FakeFeedUpdatesScheduler : FeedUpdatesScheduler {
    var didStart = false
    var didStop = false

    override fun start() { didStart = true }
    override fun stop() { didStop = true }
}
