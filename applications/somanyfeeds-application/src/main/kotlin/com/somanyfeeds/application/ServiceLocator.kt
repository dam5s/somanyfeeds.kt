package com.somanyfeeds.application

import com.somanyfeeds.aggregator.ArticlesController
import com.somanyfeeds.aggregator.DefaultArticlesController
import com.somanyfeeds.aggregator.FeedPresenter
import com.somanyfeeds.articlesdataaccess.ArticleDataMapper
import com.somanyfeeds.articlesdataaccess.PostgresArticlesDataGateway
import com.somanyfeeds.feedsdataaccess.FeedDataMapper
import com.somanyfeeds.feedsdataaccess.FeedType
import com.somanyfeeds.feedsdataaccess.PostgresFeedsDataGateway
import com.somanyfeeds.feedsprocessing.DefaultArticlesUpdater
import com.somanyfeeds.feedsprocessing.DefaultFeedUpdatesScheduler
import com.somanyfeeds.feedsprocessing.FeedUpdatesScheduler
import com.somanyfeeds.feedsprocessing.FeedsUpdater
import com.somanyfeeds.feedsprocessing.atom.AtomFeedProcessor
import com.somanyfeeds.feedsprocessing.rss.RssFeedProcessor
import com.somanyfeeds.httpgateway.HttpGateway
import com.somanyfeeds.httpgateway.OkHttpGateway
import com.squareup.okhttp.OkHttpClient
import org.apache.ibatis.mapping.Environment
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.apache.ibatis.transaction.TransactionFactory
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import javax.sql.DataSource

object ServiceLocator {
    private val dataSource: DataSource = PostgresDataSourceFactory().create()
    private val sqlSessionFactory = buildSqlSessionFactory(dataSource)
    private val feedsDataGateway = PostgresFeedsDataGateway(sqlSessionFactory)
    private val articlesDataGateway = PostgresArticlesDataGateway(sqlSessionFactory)
    private val feedPresenter = FeedPresenter()
    private val articlesController = DefaultArticlesController(articlesDataGateway, feedsDataGateway, feedPresenter)

    private val scheduledExecutorService: ScheduledExecutorService = ScheduledThreadPoolExecutor(2)

    private val httpGateway: HttpGateway = OkHttpGateway(OkHttpClient())

    private val feedsUpdater = FeedsUpdater(
        feedsDataGateway = feedsDataGateway,
        feedProcessors = mapOf(
            FeedType.RSS to RssFeedProcessor(httpGateway),
            FeedType.ATOM to AtomFeedProcessor(httpGateway)
        ),
        articlesUpdater = DefaultArticlesUpdater(articlesDataGateway, 20)
    )

    private val feedUpdatesScheduler = DefaultFeedUpdatesScheduler(
        scheduledExecutorService = scheduledExecutorService,
        feedsUpdater = feedsUpdater
    )

    fun articlesController(): ArticlesController = articlesController
    fun feedUpdatesScheduler(): FeedUpdatesScheduler = feedUpdatesScheduler

    private fun buildSqlSessionFactory(dataSource: DataSource): SqlSessionFactory {
        val transactionFactory: TransactionFactory = JdbcTransactionFactory()
        val environment = Environment("development", transactionFactory, dataSource)
        val configuration = Configuration(environment)

        configuration.addMapper(javaClass<FeedDataMapper>())
        configuration.addMapper(javaClass<ArticleDataMapper>())

        return SqlSessionFactoryBuilder().build(configuration)
    }
}
