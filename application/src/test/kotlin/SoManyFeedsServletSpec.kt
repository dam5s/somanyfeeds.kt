import org.jetbrains.spek.api.Spek
import com.somanyfeeds.application.SoManyFeedsServlet
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.somanyfeeds.aggregator.FeedUpdatesScheduler

class SoManyFeedsServletSpec : Spek() {{
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
                assertEquals(articlesResp.getContentType(), "application/json")
                assertEquals(articlesResp.getBody(), "[]")
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
