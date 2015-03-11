package com.somanyfeeds.application

import javax.servlet.annotation.WebServlet
import javax.servlet.http.*
import com.somanyfeeds.aggregator.ArticlesController
import com.somanyfeeds.aggregator.FeedUpdatesScheduler

WebServlet(name = "SoManyFeeds", value = array("/*"))
public class SoManyFeedsServlet(
    val staticAssetsController: StaticAssetsController = ServiceLocator.staticAssetsController(),
    val articlesController: ArticlesController = ServiceLocator.articlesController(),
    val feedUpdatesScheduler: FeedUpdatesScheduler = ServiceLocator.feedUpdatesScheduler()
) : HttpServlet() {

    {
        feedUpdatesScheduler.start()
    }

    override fun destroy() {
        feedUpdatesScheduler.stop()
        super.destroy()
    }

    override public fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        if (staticAssetsController.serveStaticAsset(req, resp)) {
            return;
        }

        articlesController.listArticles(req, resp)
    }
}
