package com.somanyfeeds.feedsprocessing.rss

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
import kotlin.text.Regex

public class RssFeedProcessor(val httpGateway: HttpGateway) : FeedProcessor {
    private val logger = LoggerFactory.getLogger(javaClass<RssFeedProcessor>())
    private val xmlMapper = XmlMapper().tap { configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) }
    private val utc = ZoneId.of("UTC")

    override fun process(feed: Feed): List<Article> {
        logger.debug("Processing Feed: {}", feed)

        val rssString = httpGateway.get(feed.url).replace("\uFEFF", "")
        val rss = xmlMapper.readValue(rssString, javaClass<Rss>())
        val articles = rss.channel.items.map {
            Article(
                title = it.title,
                link = it.link,
                date = ZonedDateTime.ofInstant(it.pubDate.toInstant(), utc),
                content = it.description
            )
        }

        logger.debug("Processed articles: {}", articles)

        return articles
    }
}
