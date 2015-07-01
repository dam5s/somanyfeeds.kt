package com.somanyfeeds.application;


import com.somanyfeeds.aggregator.ArticlesController;
import com.somanyfeeds.feedsprocessing.FeedUpdatesScheduler;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {SoManyFeedsModule.class})
public interface SoManyFeedsComponent {

    static SoManyFeedsComponent build() {
        return DaggerSoManyFeedsComponent
                .builder()
                .soManyFeedsModule(new SoManyFeedsModule())
                .build();
    }

    ArticlesController articlesController();

    FeedUpdatesScheduler feedUpdatesScheduler();
}
