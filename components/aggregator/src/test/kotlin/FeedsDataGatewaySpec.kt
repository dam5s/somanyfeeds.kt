import org.jetbrains.spek.api.Spek
import com.somanyeeds.aggregator.PostgresFeedsDataGateway
import com.somanyeeds.aggregator.FeedDataMapper
import kotlin.test.assertEquals
import com.somanyeeds.aggregator.Feed
import com.somanyeeds.aggregator.FeedType

class FeedsDataGatewaySpec : Spek() {{
    val dbConfig = TestDatabaseConfig()
    val dataMapper = dbConfig.buildTestDataMapper(javaClass<FeedDataMapper>())
    val dataGateway = PostgresFeedsDataGateway(dataMapper)

    given("some feeds in the database") {
        dbConfig.executeSql("delete from feed")
        dbConfig.executeSql("""
            insert into feed (id, name, slug, url, type) values
            (210, 'G+', 'g-plus', 'http://gplus.example.com/feed.rss', 'RSS'),
            (211, 'Github', 'github', 'http://github.example.com/feed.atom', 'ATOM'),
            (212, 'Tumblr', 'tumblr', 'http://tumb.example.com/feed.rss', 'RSS');
        """)

        on("select feeds") {
            val feeds = dataGateway.selectFeeds()

            it("returns available feeds") {
                val expectedFeeds = listOf(
                    Feed(
                        id = 210,
                        name = "G+",
                        slug = "g-plus",
                        url = "http://gplus.example.com/feed.rss",
                        type = FeedType.RSS
                    ),
                    Feed(
                        id = 211,
                        name = "Github",
                        slug = "github",
                        url = "http://github.example.com/feed.atom",
                        type = FeedType.ATOM
                    ),
                    Feed(
                        id = 212,
                        name = "Tumblr",
                        slug = "tumblr",
                        url = "http://tumb.example.com/feed.rss",
                        type = FeedType.RSS
                    )
                )
                assertEquals(expectedFeeds, feeds)
            }
        }
    }
}}
