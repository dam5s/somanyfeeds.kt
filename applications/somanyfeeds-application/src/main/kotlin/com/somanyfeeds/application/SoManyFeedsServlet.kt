package com.somanyfeeds.application

import com.somanyfeeds.aggregator.ArticlesController
import com.somanyfeeds.feedsprocessing.FeedUpdatesScheduler
import dagger.Component
import javax.inject.Singleton
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "SoManyFeeds", value = "/")
public class SoManyFeedsServlet(component: SoManyFeedsComponent = SoManyFeedsComponent.build()) : HttpServlet() {
    val articlesController: ArticlesController = component.articlesController()
    val feedUpdatesScheduler: FeedUpdatesScheduler = component.feedUpdatesScheduler()

    init {
        feedUpdatesScheduler.start()
    }

    override fun destroy() {
        feedUpdatesScheduler.stop()
        super.destroy()
    }

    override public fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        if (req.getMethod() != "GET") {
            return
        }
        articlesController.listArticles(req, resp)
    }
}
