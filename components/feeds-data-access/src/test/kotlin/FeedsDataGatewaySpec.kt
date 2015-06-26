import org.jetbrains.spek.api.Spek
import kotlin.test.assertEquals
import com.somanyfeeds.feedsdataaccess.FeedType
import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.feedsdataaccess.PostgresFeedsDataGateway
import com.somanyfeeds.feedsdataaccess.FeedDataMapper

class FeedsDataGatewaySpec : Spek() { init {
    val dbConfig = TestDatabaseConfig(javaClass<FeedDataMapper>())
    val dataGateway = PostgresFeedsDataGateway(dbConfig.sqlSessionFactory)

    given("some feeds in the database") {
        dbConfig.executeSql("delete from article")
        dbConfig.executeSql("delete from feed")
        dbConfig.executeSql("""
            insert into feed (id, name, slug, url, type) values
            (210, 'G+', 'g-plus', 'http://gplus.example.com/feed.rss', 'RSS'),
            (211, 'Github', 'github', 'http://github.example.com/feed.atom', 'ATOM'),
            (212, 'Tumblr', 'tumblr', 'http://tumb.example.com/feed.rss', 'RSS');
        """)

        on("select feeds") {
            val feeds = dataGateway.selectAllFeeds()

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
