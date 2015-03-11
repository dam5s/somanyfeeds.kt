import org.jetbrains.spek.api.Spek
import com.somanyfeeds.aggregator.PostgresArticlesDataGateway
import kotlin.test.assertEquals
import com.somanyfeeds.aggregator.Article
import java.time.ZonedDateTime
import com.somanyfeeds.aggregator.ArticleDataMapper

class ArticleDataGatewaySpec : Spek() {{
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

        on("selectArticles") {
            val articles = dataGateway.selectArticles()

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
    }
}}
