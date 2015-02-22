package com.somanyfeeds.aggregator

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public trait ArticlesController {
    public fun listArticles(req: HttpServletRequest, resp: HttpServletResponse)
}

class DefaultArticlesController : ArticlesController {
    override fun listArticles(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.setContentType("application/json")
        resp.getWriter().write("[]")
    }
}
