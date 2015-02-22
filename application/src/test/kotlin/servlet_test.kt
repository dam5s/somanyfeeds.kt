import org.jetbrains.spek.api.Spek
import com.somanyfeeds.application.SoManyFeedsServlet
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SoManyFeedsServletSpecks : Spek() {{
    given("a SoManyFeedsServlet") {
        val servlet = SoManyFeedsServlet()

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
