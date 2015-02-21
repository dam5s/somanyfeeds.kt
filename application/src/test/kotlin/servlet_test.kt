import org.junit.Test as test
import com.somanyfeeds.application.SoManyFeedsServlet
import kotlin.test.assertTrue

class SoManyFeedsServletTest {
    test fun doGet() {
        val req = TestHttpServletRequest()
        val resp = TestHttpServletResponse()
        val servlet = SoManyFeedsServlet()

        servlet.doGet(req, resp)

        val body = resp.getBody()
        assertTrue(body.contains("<h1>damo.io</h1>"))
    }
}
