import TestHttpServletRequest
import TestHttpServletResponse
import com.somanyfeeds.frontend.DefaultStaticAssetsController
import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StaticAssetsControllerSpec : Spek() { init {
    val controller = DefaultStaticAssetsController()

    given("a request for the homepage") {
        val homePageReq = TestHttpServletRequest(path = "/")
        val homePageResp = TestHttpServletResponse()

        on("canServe") {
            val result = controller.canServe(homePageReq)

            it("returns true") {
                assertTrue(result)
            }
        }

        on("serveStaticAsset") {
            controller.serveStaticAsset(homePageReq, homePageResp)

            it("renders index.html page") {
                assertTrue(homePageResp.getBody().contains("<h1>Hello World</h1>"))
            }
            it("sets the content-type") {
                assertEquals("text/html", homePageResp.getContentType())
            }
        }
    }

    given("a request for a CSS file") {
        val cssReq = TestHttpServletRequest(path = "/css/base.css")
        val cssResp = TestHttpServletResponse()

        on("canServe") {
            val result = controller.canServe(cssReq)

            it("returns true") {
                assertTrue(result)
            }
        }

        on("serveStaticAsset") {
            controller.serveStaticAsset(cssReq, cssResp)

            it("renders the css file") {
                assertTrue(cssResp.getBody().contains("font-family"))
            }
            it("sets the content-type") {
                assertEquals("text/css", cssResp.getContentType())
            }
        }
    }

    given("a request that does not match an asset") {
        val noMatchReq = TestHttpServletRequest(path = "/resource.json")
        val noMatchResp = TestHttpServletResponse()

        on("canServe") {
            val result = controller.canServe(noMatchReq)

            it("returns false") {
                assertFalse(result)
            }
        }

        on("serveStaticAsset") {
            controller.serveStaticAsset(noMatchReq, noMatchResp)

            it("does not change body") {
                assertTrue(noMatchResp.getBody().isEmpty())
            }
            it("does not set content-type") {
                assertNull(noMatchResp.getContentType())
            }
            it("sets status 404") {
                assertEquals(404, noMatchResp.getStatus())
            }
        }
    }
}}
