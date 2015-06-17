
import FakeHttpGateway
import buildFeed
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.somanyfeeds.feedsdataaccess.FeedType
import com.somanyfeeds.feedsprocessing.rss.RssFeedProcessor
import com.somanyfeeds.kotlinextensions.asString
import com.somanyfeeds.kotlinextensions.getResourceAsStream
import org.jetbrains.spek.api.Spek
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RssFeedProcessorSpec : Spek() { init {
    val httpGateway = FakeHttpGateway()
    val processor = RssFeedProcessor(httpGateway)

    given("an RSS feed") {
        val feed = buildFeed(
            url = "http://example.com/feed/rss",
            type = FeedType.RSS
        )

        httpGateway.stubbedResponse = getResourceAsStream("sample.rss.xml")!!.asString()

        on("feed processing") {
            val articles = processor.process(feed)

            it("returns the correct numbet of articles") {
                assertEquals(10, articles.size())
            }

            it("builds an article correctly") {
                val article = articles.get(9)
                val expectedDate = ZonedDateTime.of(LocalDateTime.of(2013, Month.MAY, 12, 19, 33, 13), ZoneId.of("UTC"))

                assertEquals(article.link, "https://plus.google.com/105039413587880910287/posts/FiXRB9KBvYY")
                assertEquals(article.date, expectedDate)
                assertTrue(article.title.contains("Considering taking some of wednesday/thursday off to be able to follow Google I/O live streamsÂ  #io2013..."))
                assertTrue(article.content.contains("<div class='content'>Considering taking some of wednesday/thursday "))
            }
        }
    }

    given("an RSS feed from gplus.com with strange encoding") {
        val feed = buildFeed()

        httpGateway.stubbedResponse = getResourceAsStream("gplus.rss.xml")!!.asString()

        on("feed processing") {
            val articles = processor.process(feed)

            it("cleans up the article content") {
                assertTrue(articles.get(0).content.contains("not the opposite."))
                assertTrue(articles.get(0).content.contains("not the opposite.</div>"))
            }
        }
    }
}}
