package com.somanyfeeds.aggregator

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.somanyfeeds.jsonserialization.ObjectMapperProvider
import com.somanyfeeds.articlesdataaccess.ArticlesDataGateway

public trait ArticlesController {
    public fun listArticles(req: HttpServletRequest, resp: HttpServletResponse)
}

class DefaultArticlesController(val articlesDataGateway: ArticlesDataGateway) : ArticlesController {
    val objectMapper = ObjectMapperProvider().get()

    override fun listArticles(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.setContentType("application/json")

        val articles = articlesDataGateway.selectArticles()

        when (expectedContentType(req)) {
            ContentType.JSON -> {
                objectMapper.writeValue(resp.getWriter(), articles)
            }
            ContentType.HTML -> {
                req.setAttribute("articles", articles)
                req.getRequestDispatcher("/WEB-INF/articles.jsp").forward(req, resp)
            }
            ContentType.UNKNOWN -> {
                resp.setStatus(406)
            }
        }
    }

    private fun expectedContentType(req: HttpServletRequest) : ContentType {
        for (accept in req.getHeaders("Accept")) {
            if (accept.contains("application/json")) {
                return ContentType.JSON
            }
            if (accept.contains("text/html") || accept.contains("application/xhtml+xml")) {
                return ContentType.HTML
            }
        }
        return ContentType.UNKNOWN
    }

    private enum class ContentType {
        JSON
        HTML
        UNKNOWN
    }
}
