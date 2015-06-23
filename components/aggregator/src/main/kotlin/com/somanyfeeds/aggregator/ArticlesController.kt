package com.somanyfeeds.aggregator

import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.articlesdataaccess.ArticlesDataGateway
import com.somanyfeeds.jsonserialization.ObjectMapperProvider
import java.util.Enumeration
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public interface ArticlesController {
    public fun listArticles(req: HttpServletRequest, resp: HttpServletResponse)
}

class DefaultArticlesController(val articlesDataGateway: ArticlesDataGateway) : ArticlesController {
    private val objectMapper = ObjectMapperProvider().get()
    private val DEFAULT_FEED_SLUGS = setOf("gplus", "pivotal")

    override fun listArticles(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.setContentType("application/json")

        val path = req.getRequestURI()
        val isRoot = path.length() <= 1
        val slugs = if (isRoot) DEFAULT_FEED_SLUGS else path.removePrefix("/").splitBy(",").toSet()
        val articles = articlesDataGateway.selectAllByFeedSlugs(slugs)

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
        for (accept in req.getHeaders("Accept") as Enumeration<String>) {
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
        JSON,
        HTML,
        UNKNOWN
    }
}
