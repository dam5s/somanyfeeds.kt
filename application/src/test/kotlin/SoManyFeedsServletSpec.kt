import FakeFeedUpdatesScheduler
import TestHttpServletRequest
import TestHttpServletResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.somanyfeeds.application.SoManyFeedsServlet
import com.somanyfeeds.feedsprocessing.FeedUpdatesScheduler
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SoManyFeedsServletSpec : Spek() { init {
    given("a SoManyFeedsServlet") {
        val fakeScheduler = FakeFeedUpdatesScheduler()
        val servlet = SoManyFeedsServlet(feedUpdatesScheduler = fakeScheduler)

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

        on("asset request") {
            val assetReq = TestHttpServletRequest(path = "/css/base.css")
            val assetResp = TestHttpServletResponse()

            servlet.doGet(assetReq, assetResp)

            it("renders the static asset") {
                assertEquals(assetResp.getContentType(), "text/css")
                assertTrue(assetResp.getBody().contains("font-family"))
            }
        }

        on("articles request") {
            val reqHeaders = mapOf("accept" to listOf("application/json"))
            val articlesReq = TestHttpServletRequest(path = "/articles", headers = reqHeaders)
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

class FakeFeedUpdatesScheduler : FeedUpdatesScheduler {
    var didStart = false
    var didStop = false

    override fun start() {
        didStart = true
    }

    override fun stop() {
        didStop = true
    }
}
