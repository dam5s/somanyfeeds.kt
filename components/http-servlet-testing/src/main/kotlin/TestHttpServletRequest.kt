import java.io.BufferedReader
import java.security.Principal
import java.util.*
import javax.servlet.*
import javax.servlet.http.*

class TestHttpServletRequest(
    val path: String = "/",
    val headers: Map<String, List<String>> = emptyMap(),
    val httpMethod: String = "GET"
) : HttpServletRequest {

    val attributes: MutableMap<String, Any> = HashMap()
    val requestDispatcher = TestRequestDispatcher()

    override fun getAttribute(name: String?): Any? = attributes.get(name!!)

    override fun getAttributeNames(): Enumeration<String>? {
        throw UnsupportedOperationException()
    }

    override fun getCharacterEncoding(): String? {
        throw UnsupportedOperationException()
    }

    override fun setCharacterEncoding(env: String?) {
        throw UnsupportedOperationException()
    }

    override fun getContentLength(): Int {
        throw UnsupportedOperationException()
    }

    override fun getContentLengthLong(): Long {
        throw UnsupportedOperationException()
    }

    override fun getContentType(): String? {
        throw UnsupportedOperationException()
    }

    override fun getInputStream(): ServletInputStream? {
        throw UnsupportedOperationException()
    }

    override fun getParameter(name: String?): String? {
        throw UnsupportedOperationException()
    }

    override fun getParameterNames(): Enumeration<String>? {
        throw UnsupportedOperationException()
    }

    override fun getParameterValues(name: String?): Array<out String>? {
        throw UnsupportedOperationException()
    }

    override fun getParameterMap(): MutableMap<String, Array<String>>? {
        throw UnsupportedOperationException()
    }

    override fun getProtocol(): String? {
        throw UnsupportedOperationException()
    }

    override fun getScheme(): String? {
        throw UnsupportedOperationException()
    }

    override fun getServerName(): String? {
        throw UnsupportedOperationException()
    }

    override fun getServerPort(): Int {
        throw UnsupportedOperationException()
    }

    override fun getReader(): BufferedReader? {
        throw UnsupportedOperationException()
    }

    override fun getRemoteAddr(): String? {
        throw UnsupportedOperationException()
    }

    override fun getRemoteHost(): String? {
        throw UnsupportedOperationException()
    }

    override fun setAttribute(name: String?, o: Any?) {
        attributes.put(name!!, o!!)
    }

    override fun removeAttribute(name: String?) {
        attributes.remove(name!!)
    }

    override fun getLocale(): Locale? {
        throw UnsupportedOperationException()
    }

    override fun getLocales(): Enumeration<Locale>? {
        throw UnsupportedOperationException()
    }

    override fun isSecure(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun getRequestDispatcher(path: String?): RequestDispatcher? {
        requestDispatcher.path = path
        return requestDispatcher
    }

    override fun getRealPath(path: String?): String? {
        throw UnsupportedOperationException()
    }

    override fun getRemotePort(): Int {
        throw UnsupportedOperationException()
    }

    override fun getLocalName(): String? {
        throw UnsupportedOperationException()
    }

    override fun getLocalAddr(): String? {
        throw UnsupportedOperationException()
    }

    override fun getLocalPort(): Int {
        throw UnsupportedOperationException()
    }

    override fun getServletContext(): ServletContext? {
        throw UnsupportedOperationException()
    }

    override fun startAsync(): AsyncContext? {
        throw UnsupportedOperationException()
    }

    override fun startAsync(servletRequest: ServletRequest?, servletResponse: ServletResponse?): AsyncContext? {
        throw UnsupportedOperationException()
    }

    override fun isAsyncStarted(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isAsyncSupported(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun getAsyncContext(): AsyncContext? {
        throw UnsupportedOperationException()
    }

    override fun getDispatcherType(): DispatcherType? {
        throw UnsupportedOperationException()
    }

    override fun getAuthType(): String? {
        throw UnsupportedOperationException()
    }

    override fun getCookies(): Array<out Cookie>? {
        throw UnsupportedOperationException()
    }

    override fun getDateHeader(name: String?): Long {
        throw UnsupportedOperationException()
    }

    override fun getHeader(name: String?): String? = headers.get(name)?.get(0)

    override fun getHeaders(name: String?): Enumeration<String>? = Vector(headers.get(name) ?: emptyList()).elements()

    override fun getHeaderNames(): Enumeration<String>? {
        throw UnsupportedOperationException()
    }

    override fun getIntHeader(name: String?): Int {
        throw UnsupportedOperationException()
    }

    override fun getMethod(): String? = httpMethod

    override fun getPathInfo(): String? {
        throw UnsupportedOperationException()
    }

    override fun getPathTranslated(): String? {
        throw UnsupportedOperationException()
    }

    override fun getContextPath(): String? {
        throw UnsupportedOperationException()
    }

    override fun getQueryString(): String? {
        throw UnsupportedOperationException()
    }

    override fun getRemoteUser(): String? {
        throw UnsupportedOperationException()
    }

    override fun isUserInRole(role: String?): Boolean {
        throw UnsupportedOperationException()
    }

    override fun getUserPrincipal(): Principal? {
        throw UnsupportedOperationException()
    }

    override fun getRequestedSessionId(): String? {
        throw UnsupportedOperationException()
    }

    override fun getRequestURI(): String? = path

    override fun getRequestURL(): StringBuffer? {
        throw UnsupportedOperationException()
    }

    override fun getServletPath(): String? {
        throw UnsupportedOperationException()
    }

    override fun getSession(create: Boolean): HttpSession? {
        throw UnsupportedOperationException()
    }

    override fun getSession(): HttpSession? {
        throw UnsupportedOperationException()
    }

    override fun changeSessionId(): String? {
        throw UnsupportedOperationException()
    }

    override fun isRequestedSessionIdValid(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isRequestedSessionIdFromCookie(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isRequestedSessionIdFromURL(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun isRequestedSessionIdFromUrl(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun authenticate(response: HttpServletResponse?): Boolean {
        throw UnsupportedOperationException()
    }

    override fun login(username: String?, password: String?) {
        throw UnsupportedOperationException()
    }

    override fun logout() {
        throw UnsupportedOperationException()
    }

    override fun getParts(): MutableCollection<Part>? {
        throw UnsupportedOperationException()
    }

    override fun getPart(name: String?): Part? {
        throw UnsupportedOperationException()
    }

    override fun <T : HttpUpgradeHandler?> upgrade(handlerClass: Class<T>?): T {
        throw UnsupportedOperationException()
    }
}
