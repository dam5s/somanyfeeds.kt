package com.somanyfeeds.application;

import com.somanyfeeds.aggregator.*;
import com.somanyfeeds.articlesdataaccess.*;
import com.somanyfeeds.feedsdataaccess.*;
import com.somanyfeeds.feedsprocessing.*;
import com.somanyfeeds.feedsprocessing.atom.AtomFeedProcessor;
import com.somanyfeeds.feedsprocessing.rss.RssFeedProcessor;
import com.somanyfeeds.httpgateway.*;
import com.squareup.okhttp.OkHttpClient;
import dagger.*;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.*;

@Module
public class SoManyFeedsModule {

    @Provides @Singleton
    DataSource provideDataSource() {
        return new PostgresDataSourceFactory().create();
    }

    @Provides @Singleton
    SqlSessionFactory provideSqlSessionFactory(DataSource dataSource) {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);

        configuration.addMapper(FeedDataMapper.class);
        configuration.addMapper(ArticleDataMapper.class);

        return new SqlSessionFactoryBuilder().build(configuration);
    }

    @Provides
    ArticlesDataGateway provideArticlesDataGateway(PostgresArticlesDataGateway gateway) {
        return gateway;
    }

    @Provides
    FeedsDataGateway provideFeedsDataGateway(PostgresFeedsDataGateway gateway) {
        return gateway;
    }

    @Provides
    ArticlesController provideArticlesController(DefaultArticlesController controller) {
        return controller;
    }

    @Provides @Singleton
    HttpGateway provideHttpGateway() {
        return new OkHttpGateway(new OkHttpClient());
    }

    @Provides @Singleton
    Runnable provideFeedsUpdater(
        FeedsDataGateway feedsDataGateway,
        ArticlesUpdater articlesUpdater,
        RssFeedProcessor rss,
        AtomFeedProcessor atom
    ) {
        Map<FeedType, FeedProcessor> processors = new HashMap<>();
        processors.put(FeedType.RSS, rss);
        processors.put(FeedType.ATOM, atom);

        return new FeedsUpdater(feedsDataGateway, articlesUpdater, processors);
    }

    @Provides
    int provideArticlesLimit() {
        return 20;
    }

    @Provides
    ArticlesUpdater provideArticlesUpdater(DefaultArticlesUpdater updater) {
        return updater;
    }

    @Provides @Singleton
    ScheduledExecutorService provideScheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(2);
    }

    @Provides
    FeedUpdatesScheduler provideFeedUpdatesScheduler(DefaultFeedUpdatesScheduler scheduler) {
        return scheduler;
    }
}
