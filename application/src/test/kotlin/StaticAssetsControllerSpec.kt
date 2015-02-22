import kotlin.test.assertTrue
import kotlin.test.assertEquals
import org.jetbrains.spek.api.Spek
import com.somanyfeeds.application.DefaultStaticAssetsController
import kotlin.test.assertFalse
import kotlin.test.assertNull

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

        on("request that does not match an asset") {
            val noMatchReq = TestHttpServletRequest(path = "/i-dont-exist.css")
            val noMatchResp = TestHttpServletResponse()

            val result = controller.serveStaticAsset(noMatchReq, noMatchResp)

            it("returns false") {
                assertFalse(result)
            }
            it("body is empty") {
                assertTrue(noMatchResp.getBody().isEmpty())
            }
            it("content-type is not set") {
                assertNull(noMatchResp.getContentType())
            }
        }
    })
}}
