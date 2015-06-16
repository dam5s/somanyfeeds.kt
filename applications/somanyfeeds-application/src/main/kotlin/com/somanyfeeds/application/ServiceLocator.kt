package com.somanyfeeds.application

import com.somanyfeeds.aggregator.ArticlesController
import com.somanyfeeds.aggregator.DefaultArticlesController
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
import com.somanyfeeds.kotlinextensions.tap
import com.squareup.okhttp.OkHttpClient
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.mapper.MapperFactoryBean
import org.postgresql.ds.PGSimpleDataSource
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import javax.sql.DataSource

object ServiceLocator {
    val dataSource: DataSource = PGSimpleDataSource().tap {
        setUser("dam5s")
        setServerName("localhost")
        setPortNumber(5432)
        setDatabaseName("somanyfeeds_dev")
    }

    val sqlSessionFactoryBean = SqlSessionFactoryBean().tap {
        setDataSource(dataSource)
    }

    val articleDataMapper = buildDataMapper(javaClass<ArticleDataMapper>())
    val articlesDataGateway = PostgresArticlesDataGateway(articleDataMapper = articleDataMapper);
    val articlesController = DefaultArticlesController(articlesDataGateway = articlesDataGateway)

    val scheduledExecutorService: ScheduledExecutorService = ScheduledThreadPoolExecutor(2)

    val feedDataMapper = buildDataMapper(javaClass<FeedDataMapper>())
    val feedsDataGateway = PostgresFeedsDataGateway(feedDataMapper = feedDataMapper)

    val httpGateway: HttpGateway = OkHttpGateway(OkHttpClient())

    val feedsUpdater = FeedsUpdater(
        feedsDataGateway = feedsDataGateway,
        feedProcessors = mapOf(
            FeedType.RSS to RssFeedProcessor(httpGateway),
            FeedType.ATOM to AtomFeedProcessor(httpGateway)
        ),
        articlesUpdater = DefaultArticlesUpdater(articlesDataGateway, 20)
    )
    val feedUpdatesScheduler = DefaultFeedUpdatesScheduler(
        scheduledExecutorService = scheduledExecutorService,
        feedsUpdater = feedsUpdater
    )

    fun articlesController(): ArticlesController = articlesController
    fun feedUpdatesScheduler(): FeedUpdatesScheduler = feedUpdatesScheduler

    private fun buildDataMapper<T>(klass: Class<T>): T {
        val sqlSessionFactory = sqlSessionFactoryBean.getObject().tap {
            getConfiguration().addMapper(klass)
        }

        return MapperFactoryBean<T>().let { dataMapperFactory ->
            dataMapperFactory.setMapperInterface(klass)
            dataMapperFactory.setSqlSessionFactory(sqlSessionFactory)
            dataMapperFactory.getObject()
        }
    }
}
