import FakeArticlesUpdater
import FakeFeedProcessor
import FakeFeedsDataGateway
import buildArticle
import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.feedsdataaccess.FeedType
import com.somanyfeeds.feedsdataaccess.FeedsDataGateway
import com.somanyfeeds.feedsprocessing.ArticlesUpdater
import com.somanyfeeds.feedsprocessing.FeedProcessor
import com.somanyfeeds.feedsprocessing.FeedsUpdater
import org.jetbrains.spek.api.Spek
import java.time.ZonedDateTime
import kotlin.test.assertEquals

class FeedsUpdaterSpec : Spek() { init {
    given("a FeedsUpdater and some feeds") {
        val rssFeed = Feed(
            name = "G+",
            slug = "gplus",
            url = "http://example.com/gplus",
            type = FeedType.RSS
        )
        val atomFeed = Feed(
            name = "Github",
            slug = " github",
            url = "http://example.com/github",
            type = FeedType.ATOM
        )

        val fakeFeedsDataGateway = FakeFeedsDataGateway(listOf(rssFeed, atomFeed))
        val fakeRssProcessor = FakeFeedProcessor(listOf(
            buildArticle(id = 101)
        ))
        val fakeAtomProcessor = FakeFeedProcessor(listOf(
            buildArticle(id = 201),
            buildArticle(id = 202)
        ))
        val fakeFeedProcessors: Map<FeedType, FeedProcessor> = mapOf(
            FeedType.RSS to fakeRssProcessor,
            FeedType.ATOM to fakeAtomProcessor
        )
        val fakeArticlesUpdater = FakeArticlesUpdater()
        val feedsUpdater = FeedsUpdater(
            feedsDataGateway = fakeFeedsDataGateway,
            feedProcessors = fakeFeedProcessors,
            articlesUpdater = fakeArticlesUpdater
        )

        on("run") {
            feedsUpdater.run()

            it("processes the RSS feed with the RSS processor") {
                assertEquals(rssFeed, fakeRssProcessor.processedFeed)
            }

            it("processes the ATOM feed with the ATOM processor") {
                assertEquals(atomFeed, fakeAtomProcessor.processedFeed)
            }
        }
    }
}}
