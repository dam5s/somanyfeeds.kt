package com.somanyfeeds.application

import javax.servlet.annotation.WebServlet
import javax.servlet.http.*
import com.somanyfeeds.aggregator.ArticlesController

WebServlet(name = "SoManyFeeds", value = array("/*"))
public class SoManyFeedsServlet(
    val staticAssetsController: StaticAssetsController = ServiceLocator.staticAssetsController(),
    val articlesController: ArticlesController = ServiceLocator.articlesController()
) : HttpServlet() {

    override public fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        if (staticAssetsController.serveStaticAsset(req, resp)) {
            return;
        }

        articlesController.listArticles(req, resp)
    }
}
