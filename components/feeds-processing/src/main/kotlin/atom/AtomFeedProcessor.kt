package com.somanyfeeds.feedsprocessing.atom

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.somanyfeeds.articlesdataaccess.Article
import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.feedsprocessing.FeedProcessor
import com.somanyfeeds.httpgateway.HttpGateway
import com.somanyfeeds.kotlinextensions.tap
import org.slf4j.LoggerFactory
import java.time.ZoneId
import java.time.ZonedDateTime

public class AtomFeedProcessor(val httpGateway: HttpGateway) : FeedProcessor {
    private val logger = LoggerFactory.getLogger(javaClass<AtomFeedProcessor>())
    private val xmlMapper = XmlMapper().tap { it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) }
    private val utc = ZoneId.of("UTC")

    override fun process(feed: Feed): List<Article> {
        logger.debug("Processing Feed: {}", feed)

        val atomString = httpGateway.get(feed.url)
        val atom = xmlMapper.readValue(atomString, javaClass<Atom>())
        val articles = atom.entries.map {
            Article(
                title = it.title.text,
                link = it.link.href,
                date = ZonedDateTime.ofInstant(it.published.toInstant(), utc),
                content = it.content.text
            )
        }

        logger.debug("Processed articles: {}", articles)

        return articles
    }
}
