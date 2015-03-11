package com.somanyfeeds.articlesdataaccess

import java.sql.Timestamp
import com.somanyfeeds.kotlinextensions.toUtcZonedDateTime
import org.apache.ibatis.annotations.Select

public trait ArticlesDataGateway {
    fun selectArticles(): List<Article>;
}

class PostgresArticlesDataGateway(val articleDataMapper: ArticleDataMapper) : ArticlesDataGateway {
    override fun selectArticles(): List<Article> =
        articleDataMapper.selectArticles().map { it.buildArticle() }
}

trait ArticleDataMapper {
    Select("select * from article")
    fun selectArticles(): List<ArticleMapping>
}

private class ArticleMapping {
    var id: Long = 0
    var title = ""
    var link = ""
    var content = ""
    var date: Timestamp? = null

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
