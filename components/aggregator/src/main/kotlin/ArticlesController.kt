package com.somanyfeeds.aggregator

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.somanyfeeds.jsonserialization.ObjectMapperProvider

public trait ArticlesController {
    public fun listArticles(req: HttpServletRequest, resp: HttpServletResponse)
}

class DefaultArticlesController(val articlesDataGateway: ArticlesDataGateway) : ArticlesController {
    val objectMapper = ObjectMapperProvider().get()

    override fun listArticles(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.setContentType("application/json")

        objectMapper.writeValue(
            resp.getWriter(),
            articlesDataGateway.selectArticles()
        )
    }
}
