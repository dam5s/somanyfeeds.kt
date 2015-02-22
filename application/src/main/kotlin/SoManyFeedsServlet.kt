package com.somanyfeeds.application

import javax.servlet.annotation.WebServlet
import javax.servlet.http.*
import com.somanyfeeds.aggregator.ArticlesController
import com.somanyfeeds.aggregator.DefaultArticlesController

WebServlet(name = "SoManyFeeds", value = array("/*"))
public class SoManyFeedsServlet(
    val staticAssetsController: StaticAssetsController = DefaultStaticAssetsController(),
    val articlesController: ArticlesController = DefaultArticlesController(InMemoryDataGateway)
) : HttpServlet() {

    override public fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        if (staticAssetsController.serveStaticAsset(req, resp)) {
            return;
        }

        articlesController.listArticles(req, resp)
    }
}
