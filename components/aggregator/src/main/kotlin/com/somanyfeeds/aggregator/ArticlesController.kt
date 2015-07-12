package com.somanyfeeds.aggregator

import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.articlesdataaccess.ArticlesDataGateway
import com.somanyfeeds.feedsdataaccess.FeedsDataGateway
import com.somanyfeeds.jsonserialization.ObjectMapperProvider
import java.util.Enumeration
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public interface ArticlesController {
    public fun listArticles(req: HttpServletRequest, resp: HttpServletResponse)
}

class DefaultArticlesController @Inject constructor(
    val articlesDataGateway: ArticlesDataGateway,
    val feedsDataGateway: FeedsDataGateway,
    val feedPresenter: FeedPresenter
) : ArticlesController {

    private val objectMapper = ObjectMapperProvider().get()
    private val DEFAULT_FEED_SLUGS = setOf("gplus", "pivotal")

    override fun listArticles(req: HttpServletRequest, resp: HttpServletResponse) {
        val path = req.getRequestURI()
        val isRoot = path.length() <= 1
        val slugs = if (isRoot) DEFAULT_FEED_SLUGS else path.removePrefix("/").splitBy(",").toSet()
        val articles = articlesDataGateway
            .selectAllByFeedSlugs(slugs)
            .sortDescendingBy { it.date }

        when (expectedContentType(req)) {
            ContentType.JSON -> {
                renderJSONArticles(articles, resp)
            }
            ContentType.HTML -> {
                renderHTMLArticles(articles, slugs, req, resp)
            }
            ContentType.UNKNOWN -> {
                resp.setStatus(406)
            }
        }
    }

    private fun renderJSONArticles(articles: List<Article>, resp: HttpServletResponse) {
        resp.setContentType("application/json")
        objectMapper.writeValue(resp.getWriter(), articles)
    }

    private fun renderHTMLArticles(articles: List<Article>, slugs: Set<String>, req: HttpServletRequest, resp: HttpServletResponse) {
        val feeds = feedsDataGateway
            .selectAllFeeds()
            .map { feedPresenter.present(it, slugs) }

        req.setAttribute("feeds", feeds)
        req.setAttribute("articles", articles)
        req.getRequestDispatcher("/WEB-INF/articles.jsp").forward(req, resp)
    }

    private fun expectedContentType(req: HttpServletRequest) : ContentType {
        for (accept in req.getHeaders("Accept") as Enumeration<String>) {
            if (accept.contains("application/json")) {
                return ContentType.JSON
            }
            if (accept.contains("text/html") || accept.contains("application/xhtml+xml") || accept.contains("*/*")) {
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
