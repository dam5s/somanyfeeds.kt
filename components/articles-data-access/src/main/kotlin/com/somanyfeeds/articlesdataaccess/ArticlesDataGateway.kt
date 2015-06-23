package com.somanyfeeds.articlesdataaccess

import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.kotlinextensions.toUtcZonedDateTime
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import java.sql.Timestamp
import java.time.ZoneId
import javax.sql.DataSource

public interface ArticlesDataGateway {
    fun create(article: Article, feed: Feed)

    fun selectAll(): List<Article>

    fun selectAllByFeed(feed: Feed): List<Article>

    fun selectAllByFeedSlugs(slugs: Set<String>): List<Article>

    fun removeAllByFeed(feed: Feed)
}

class PostgresArticlesDataGateway(val articleDataMapper: ArticleDataMapper, val dataSource: DataSource) : ArticlesDataGateway {

    override fun create(article: Article, feed: Feed) =
        articleDataMapper.create(ArticleMapping(article, feed.id!!))

    override fun selectAll(): List<Article> =
        articleDataMapper.selectAll().map { it.buildArticle() }

    override fun selectAllByFeed(feed: Feed): List<Article> =
        articleDataMapper.selectAllByFeed(feed).map { it.buildArticle() }

    override fun selectAllByFeedSlugs(slugs: Set<String>): List<Article> {
        val slugsArray = dataSource.getConnection().createArrayOf("text", slugs.toTypedArray())
        return articleDataMapper.selectAllByFeedSlugs(slugsArray).map { it.buildArticle() }
    }

    override fun removeAllByFeed(feed: Feed) =
        articleDataMapper.removeAllByFeed(feed)
}

interface ArticleDataMapper {
    Select("select * from article")
    fun selectAll(): List<ArticleMapping>

    Select("select * from article where feed_id = #{id}")
    fun selectAllByFeed(feed: Feed): List<ArticleMapping>

    Select("select article.* from article inner join feed on article.feed_id = feed.id where slug = any(#{slugs}::text[])")
    fun selectAllByFeedSlugs(Param("slugs") slugs: java.sql.Array): List<ArticleMapping>

    Insert("insert into article (feed_id, title, link, content, date) values (#{feedId}, #{title}, #{link}, #{content}, #{date})")
    fun create(article: ArticleMapping)

    Delete("delete from article where feed_id = #{id}")
    fun removeAllByFeed(feed: Feed)
}

private class ArticleMapping {
    var id: Long? = null
    var title = ""
    var link = ""
    var content = ""
    var date: Timestamp? = null
    var feedId: Long = 0

    constructor() {
    }

    constructor(article: Article, feedId: Long) {
        id = article.id
        title = article.title
        link = article.link
        content = article.content
        date = Timestamp.valueOf(article.date.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime())

        this.feedId = feedId
    }

    fun buildArticle(): Article {
        return Article(
            id = this.id,
            title = this.title,
            link = this.link,
            content = this.content,
            date = this.date!!.toUtcZonedDateTime()
        )
    }
}
