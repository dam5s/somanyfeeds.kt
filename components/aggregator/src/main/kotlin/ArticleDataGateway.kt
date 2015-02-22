package com.somanyfeeds.aggregator

trait ArticleDataGateway {
    fun selectArticles(): List<Article>;
}

class PostgresArticleDataGateway : ArticleDataGateway {
    override fun selectArticles(): List<Article> {
        throw UnsupportedOperationException()
    }
}
