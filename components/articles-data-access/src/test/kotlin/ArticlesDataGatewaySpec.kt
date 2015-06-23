import TestDatabaseConfig
import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.articlesdataaccess.ArticleDataMapper
import com.somanyfeeds.articlesdataaccess.PostgresArticlesDataGateway
import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.feedsdataaccess.FeedType
import org.jetbrains.spek.api.Spek
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ArticlesDataGatewaySpec : Spek() { init {
    val dbConfig = TestDatabaseConfig()
    val dataMapper = dbConfig.buildTestDataMapper(javaClass<ArticleDataMapper>())
    val dataGateway = PostgresArticlesDataGateway(articleDataMapper = dataMapper, dataSource = dbConfig.dataSource)

    given("some articles and feeds") {
        dbConfig.executeSql("delete from article")
        dbConfig.executeSql("delete from feed")

        dbConfig.executeSql("""
          insert into feed(id, name, slug, url, type) values
          (90, 'Github', 'github', 'https://github.com/dam5s.atom', 'ATOM'),
          (91, 'Blog', 'blog', 'http://example.com/blog/feed', 'RSS'),
          (92, 'Photos', 'photos', 'http://example.com/photos/feed', 'RSS');
        """)

        dbConfig.executeSql("""
            insert into article (id, feed_id, title, link, content, date) values
            (101, 90, 'Article #1', 'http://example.com/article-1', 'Article #1 content...', '2010-01-29 01:02:03'),
            (102, 91, 'Article #2', 'http://example.com/article-2', 'Article #2 content...', '2010-02-28 01:02:03'),
            (103, 91, 'Article #3', 'http://example.com/article-3', 'Article #3 content...', '2010-03-29 01:02:03'),
            (104, 92, 'Photo #1', 'http://example.com/photo-1', 'Photo #1 content...', '2010-03-29 01:02:03');
        """)


        on("select all by feed slugs") {
            val articlesByFeed = dataGateway.selectAllByFeedSlugs(setOf("photos", "blog"))

            it("returns a list of all articles for matching feeds in the database") {
                val expectedArticles = listOf(
                    Article(
                        id = 102,
                        title = "Article #2",
                        link = "http://example.com/article-2",
                        content = "Article #2 content...",
                        date = ZonedDateTime.parse("2010-02-28T01:02:03Z[UTC]")
                    ),
                    Article(
                        id = 103,
                        title = "Article #3",
                        link = "http://example.com/article-3",
                        content = "Article #3 content...",
                        date = ZonedDateTime.parse("2010-03-29T01:02:03Z[UTC]")
                    ),
                    Article(
                        id = 104,
                        title = "Photo #1",
                        link = "http://example.com/photo-1",
                        content = "Photo #1 content...",
                        date = ZonedDateTime.parse("2010-03-29T01:02:03Z[UTC]")
                    )
                )
                assertEquals(expectedArticles, articlesByFeed)
            }
        }

        on("create with a new article") {
            val newArticle = Article(
                title = "Article #4",
                link = "http://example.com/article-4",
                content = "Article #4 content...",
                date = ZonedDateTime.parse("2010-04-29T02:02:03Z[UTC]").withZoneSameLocal(ZoneId.of("+0100"))
            )

            dataGateway.create(newArticle, buildFeed(id = 90, slug = ""))

            val updatedArticles = dataGateway.selectAllByFeedSlugs(setOf("github"))

            it("inserts the new record in the database") {
                assertEquals(2, updatedArticles.size())

                val createdArticle = updatedArticles.sortBy { it.title }.get(1)
                assertTrue(createdArticle.id != null)
                assertEquals("Article #4", createdArticle.title)
                assertEquals("http://example.com/article-4", createdArticle.link)
                assertEquals("Article #4 content...", createdArticle.content)
                assertEquals(ZonedDateTime.parse("2010-04-29T01:02:03Z[UTC]"), createdArticle.date)
            }
        }

        on("remove all by feed") {
            dataGateway.removeAllByFeed(buildFeed(id = 90))

            val remainingArticles = dataGateway.selectAllByFeed(buildFeed(id = 90))

            it("removes all articles for that feed") {
                assertTrue(remainingArticles.isEmpty())
            }
        }
    }
}}

fun buildFeed(
    id: Long? = null,
    name: String = "",
    slug: String = "",
    url: String = "",
    type: FeedType = FeedType.RSS
) = Feed(id, name, slug, url, type)
