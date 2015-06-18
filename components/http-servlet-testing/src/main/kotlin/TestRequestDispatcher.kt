import javax.servlet.RequestDispatcher
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

class TestRequestDispatcher(): RequestDispatcher {
    var path: String? = null
    var forwardedReq: ServletRequest? = null
    var forwardedResp: ServletResponse? = null

    override fun forward(request: ServletRequest?, response: ServletResponse?) {
        forwardedReq = request
        forwardedResp = response
    }

    override fun include(request: ServletRequest?, response: ServletResponse?) {
        throw UnsupportedOperationException()
    }
}
