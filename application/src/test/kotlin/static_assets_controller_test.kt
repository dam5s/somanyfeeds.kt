import com.somanyfeeds.application.SoManyFeedsServlet
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import org.jetbrains.spek.api.Spek
import com.somanyfeeds.application.DefaultStaticAssetsController

class StaticAssetsControllerSpec : Spek() {{
    given("a static assets controller", {
        val controller = DefaultStaticAssetsController()

        on("request for the homepage") {
            val homePageReq = TestHttpServletRequest(path = "/")
            val homePageResp = TestHttpServletResponse()

            val result = controller.serveStaticAsset(homePageReq, homePageResp)

            it("returns true") {
                assertTrue(result)
            }
            it("renders index.html page") {
                assertTrue(homePageResp.getBody().contains("<h1>damo.io</h1>"))
            }
            it("sets the content-type") {
                assertEquals("text/html", homePageResp.getContentType())
            }
        }

        on("request for a CSS file") {
            val cssReq = TestHttpServletRequest(path = "/css/base.css")
            val cssResp = TestHttpServletResponse()

            val result = controller.serveStaticAsset(cssReq, cssResp)

            it("returns true") {
                assertTrue(result)
            }
            it("renders the css file") {
                assertTrue(cssResp.getBody().contains("font-family"))
            }
            it("sets the content-type") {
                assertEquals("text/css", cssResp.getContentType())
            }
        }
    })
}}
