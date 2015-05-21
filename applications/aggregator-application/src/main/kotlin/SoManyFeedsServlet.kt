package com.somanyfeeds.application

import com.somanyfeeds.aggregator.ArticlesController
import com.somanyfeeds.feedsprocessing.FeedUpdatesScheduler
import com.somanyfeeds.frontend.StaticAssetsController
import java.util.ArrayList
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

WebServlet(name = "SoManyFeeds", value = array("/*"))
public class SoManyFeedsServlet(
    val staticAssetsController: StaticAssetsController = ServiceLocator.staticAssetsController(),
    val articlesController: ArticlesController = ServiceLocator.articlesController(),
    val feedUpdatesScheduler: FeedUpdatesScheduler = ServiceLocator.feedUpdatesScheduler(),
    val router: Router = Router()
) : HttpServlet() {

    init {
        feedUpdatesScheduler.start()

        router.map({ req -> staticAssetsController.canServe(req) }, { req, resp -> staticAssetsController.serveStaticAsset(req, resp) })
        router.map({ req -> true }, { req, resp -> articlesController.listArticles(req, resp) })
    }

    override fun destroy() {
        feedUpdatesScheduler.stop()
        super.destroy()
    }

    override public fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        if (req.getMethod() != "GET") { return }
        router.route(req, resp)
    }
}

public class Router {
    val routes: MutableList<Route> = ArrayList();

    fun map(predicate: (HttpServletRequest) -> Boolean, servletFunction: (HttpServletRequest, HttpServletResponse) -> Unit) {
        routes.add(Route(predicate = predicate, servletFunction = servletFunction))
    }

    fun route(req: HttpServletRequest, resp: HttpServletResponse) {
        routes.firstOrNull { it.predicate(req) }?.let {
            it.servletFunction(req, resp)
            return
        }

        resp.getWriter().write("""{"error": "not found"}""")
        resp.setStatus(404);
    }
}

public data class Route(
    val predicate: (HttpServletRequest) -> Boolean,
    val servletFunction: (HttpServletRequest, HttpServletResponse) -> Unit
)
