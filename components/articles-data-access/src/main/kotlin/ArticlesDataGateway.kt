package com.somanyfeeds.articlesdataaccess

import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.kotlinextensions.toUtcZonedDateTime
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Select
import java.sql.Timestamp
import java.time.ZoneId

public trait ArticlesDataGateway {
    fun selectAll(): List<Article>

    fun create(article: Article, feed: Feed)
}

class PostgresArticlesDataGateway(val articleDataMapper: ArticleDataMapper) : ArticlesDataGateway {
    override fun create(article: Article, feed: Feed) {
        articleDataMapper.create(ArticleMapping(article, feed.id!!))
    }

    override fun selectAll(): List<Article> =
        articleDataMapper.selectAll().map { it.buildArticle() }
}

trait ArticleDataMapper {
    Select("select * from article")
    fun selectAll(): List<ArticleMapping>

    Insert("insert into article (feed_id, title, link, content, date) values (#{feedId}, #{title}, #{link}, #{content}, #{date})")
    fun create(article: ArticleMapping)
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
