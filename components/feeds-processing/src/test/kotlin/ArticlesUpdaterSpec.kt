import com.somanyfeeds.feedsprocessing.DefaultArticlesUpdater
import org.jetbrains.spek.api.Spek
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArticlesUpdaterSpec : Spek() { init {
    val fakeGateway = FakeArticlesDataGateway()
    val articlesUpdater = DefaultArticlesUpdater(fakeGateway, 3)

    given("some pre-existing articles") {
        val feed = buildFeed(id = 90)
        fakeGateway.create(buildArticle(id = 101), feed)
        fakeGateway.create(buildArticle(id = 102), feed)
        fakeGateway.create(buildArticle(id = 103), feed)

        on("update with some new articles") {
            articlesUpdater.updateArticles(listOf(
                buildArticle(id = 103),
                buildArticle(id = 104),
                buildArticle(id = 105),
                buildArticle(id = 106)
            ), feed)

            val feedArticles = fakeGateway.feedsAndArticles.get(feed)!!

            it("removes all old articles") {
                assertFalse(feedArticles.contains(buildArticle(id = 101)))
                assertFalse(feedArticles.contains(buildArticle(id = 102)))
            }

            it("inserts new articles") {
                assertTrue(feedArticles.contains(buildArticle(id = 103)))
                assertTrue(feedArticles.contains(buildArticle(id = 104)))
                assertTrue(feedArticles.contains(buildArticle(id = 105)))
            }

            it("does not insert more than the limit of articles") {
                assertFalse(feedArticles.contains(buildArticle(id = 106)))
            }
        }
    }
}}
