import FakeHttpGateway
import buildFeed
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.somanyfeeds.feedsdataaccess.FeedType
import com.somanyfeeds.feedsprocessing.atom.AtomFeedProcessor
import com.somanyfeeds.kotlinextensions.asString
import com.somanyfeeds.kotlinextensions.getResourceAsStream
import com.somanyfeeds.kotlinextensions.tap
import org.jetbrains.spek.api.Spek
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AtomFeedProcessorSpec: Spek() { init {
    val httpGateway = FakeHttpGateway()
    val processor = AtomFeedProcessor(httpGateway)

    given("an ATOM feed") {
        val feed = buildFeed(
            url = "http://example.com/feed/atom",
            type = FeedType.ATOM
        )

        httpGateway.stubbedResponse = getResourceAsStream("sample.atom.xml")!!.asString()

        on("feed processing") {
            val articles = processor.process(feed)

            it("returns the correct numbet of articles") {
                assertEquals(30, articles.size())
            }

            it("builds an article correctly") {
                val article = articles.get(0)
                val expectedDate = ZonedDateTime.of(LocalDateTime.of(2014, Month.JULY, 27, 15, 57, 56), ZoneId.of("UTC"))

                assertEquals(article.link, "https://github.com/dam5s/somayfeeds.java/compare/master")
                assertEquals(article.date, expectedDate)
                assertTrue(article.title.contains("dam5s created branch master at dam5s/somayfeeds.java"))
                assertTrue(article.content.contains("<!-- create -->\n            <div class=\"simple\">\n"))
            }
        }
    }
}}
