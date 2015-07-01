package com.somanyfeeds.articlesdataaccess

import com.somanyfeeds.databaseaccess.withMapper
import com.somanyfeeds.feedsdataaccess.Feed
import com.somanyfeeds.kotlinextensions.toUtcZonedDateTime
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.session.SqlSessionFactory
import java.sql.Timestamp
import java.time.ZoneId
import javax.inject.Inject

public interface ArticlesDataGateway {
    fun create(article: Article, feed: Feed)

    fun selectAllByFeedSlugs(slugs: Set<String>): List<Article>

    fun removeAllByFeed(feed: Feed)
}

class PostgresArticlesDataGateway
    @Inject constructor(val sqlSessionFactory: SqlSessionFactory) : ArticlesDataGateway {

    override fun create(article: Article, feed: Feed) =
        sqlSessionFactory.withMapper(javaClass<ArticleDataMapper>()) {
            create(ArticleMapping(article, feed.id!!))
        }

    override fun selectAllByFeedSlugs(slugs: Set<String>): List<Article> =
        sqlSessionFactory.withMapper(javaClass<ArticleDataMapper>()) { session ->
            val slugsArray = session.getConnection().createArrayOf("text", slugs.toTypedArray())
            selectAllByFeedSlugs(slugsArray).map { it.buildArticle() }
        }

    override fun removeAllByFeed(feed: Feed) =
        sqlSessionFactory.withMapper(javaClass<ArticleDataMapper>()) {
            removeAllByFeed(feed)
        }
}

interface ArticleDataMapper {
    Insert("insert into article (feed_id, title, link, content, date) values (#{feedId}, #{title}, #{link}, #{content}, #{date})")
    fun create(article: ArticleMapping)

    Select("select article.* from article inner join feed on article.feed_id = feed.id where slug = any(#{slugs}::text[])")
    fun selectAllByFeedSlugs(Param("slugs") slugs: java.sql.Array): List<ArticleMapping>

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
