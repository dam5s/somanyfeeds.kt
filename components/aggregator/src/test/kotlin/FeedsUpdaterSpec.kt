import org.jetbrains.spek.api.Spek
import com.somanyfeeds.aggregator.FeedsUpdater
import com.somanyeeds.aggregator.FeedsDataGateway
import com.somanyeeds.aggregator.Feed
import com.somanyeeds.aggregator.FeedType
import com.somanyeeds.aggregator.FeedProcessor
import kotlin.test.assertEquals

class FeedsUpdaterSpec : Spek() {{
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
        val fakeRssProcessor = FakeFeedProcessor()
        val fakeAtomProcessor = FakeFeedProcessor()
        val fakeFeedProcessors: Map<FeedType, FeedProcessor> = mapOf(
            FeedType.RSS to fakeRssProcessor,
            FeedType.ATOM to fakeAtomProcessor
        )
        val feedsUpdater = FeedsUpdater(
            feedsDataGateway = fakeFeedsDataGateway,
            feedProcessors = fakeFeedProcessors
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
}
}

class FakeFeedsDataGateway(val feeds: List<Feed>) : FeedsDataGateway {
    override fun selectFeeds(): List<Feed> = feeds
}

class FakeFeedProcessor : FeedProcessor {
    var processedFeed: Feed? = null

    override fun process(feed: Feed) {
        processedFeed = feed
    }
}
