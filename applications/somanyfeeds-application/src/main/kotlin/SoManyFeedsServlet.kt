package com.somanyfeeds.application

import com.somanyfeeds.aggregator.ArticlesController
import com.somanyfeeds.feedsprocessing.FeedUpdatesScheduler
import java.util.ArrayList
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

WebServlet(name = "SoManyFeeds", value = array("/"))
public class SoManyFeedsServlet(
    val articlesController: ArticlesController = ServiceLocator.articlesController(),
    val feedUpdatesScheduler: FeedUpdatesScheduler = ServiceLocator.feedUpdatesScheduler()
) : HttpServlet() {

    init {
        feedUpdatesScheduler.start()
    }

    override fun destroy() {
        feedUpdatesScheduler.stop()
        super.destroy()
    }

    override public fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        if (req.getMethod() != "GET") { return }
        articlesController.listArticles(req, resp)
    }
}
