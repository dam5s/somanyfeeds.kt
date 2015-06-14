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
    val dataGateway = PostgresArticlesDataGateway(articleDataMapper = dataMapper)

    given("some articles in the database") {
        dbConfig.executeSql("delete from article")
        dbConfig.executeSql("""
            insert into article (id, title, link, content, date) values
            (101, 'Article #1', 'http://example.com/article-1', 'Article #1 content...', '2010-01-29 01:02:03'),
            (102, 'Article #2', 'http://example.com/article-2', 'Article #2 content...', '2010-02-28 01:02:03'),
            (103, 'Article #3', 'http://example.com/article-3', 'Article #3 content...', '2010-03-29 01:02:03');
        """)

        on("selectAll") {
            val articles = dataGateway.selectAll()

            it("returns a list of articles in the database") {
                val expectedArticles = listOf(
                    Article(
                        id = 101,
                        title = "Article #1",
                        link = "http://example.com/article-1",
                        content = "Article #1 content...",
                        date = ZonedDateTime.parse("2010-01-29T01:02:03Z[UTC]")
                    ),
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
                    )
                )
                assertEquals(expectedArticles, articles)
            }
        }

        on("create with a new article") {
            dbConfig.executeSql("delete from feed")
            dbConfig.executeSql("""
                insert into feed (id, name, slug, url, type) values
                (110, 'Github', 'github', 'https://github.com/dam5s.atom', 'ATOM');
            """)

            val newArticle = Article(
                title = "Article #4",
                link = "http://example.com/article-4",
                content = "Article #4 content...",
                date = ZonedDateTime.parse("2010-04-29T02:02:03Z[UTC]").withZoneSameLocal(ZoneId.of("+0100"))
            )
            val feed = Feed(id = 110, name = "", slug = "", url = "", type = FeedType.RSS)

            dataGateway.create(newArticle, feed)

            it("inserts the new record in the database") {
                val updatedArticles = dataGateway.selectAll()
                assertEquals(4, updatedArticles.size())

                val createdArticle = updatedArticles.get(3)
                assertTrue(createdArticle.id != null)
                assertEquals("Article #4", createdArticle.title)
                assertEquals("http://example.com/article-4", createdArticle.link)
                assertEquals("Article #4 content...", createdArticle.content)
                assertEquals(ZonedDateTime.parse("2010-04-29T01:02:03Z[UTC]"), createdArticle.date)
            }
        }
    }
}}
