package com.somanyfeeds.feedsprocessing;

import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.ScheduledFuture

trait FeedUpdatesScheduler {
    fun start()
    fun stop()
}

class DefaultFeedUpdatesScheduler(
    val scheduledExecutorService: ScheduledExecutorService,
    val feedsUpdater: Runnable
) : FeedUpdatesScheduler {

    var future: ScheduledFuture<out Any?>? = null

    override fun start() {
        future = scheduledExecutorService.scheduleAtFixedRate(feedsUpdater, 0, 5, TimeUnit.SECONDS)
    }

    override fun stop() {
        future?.cancel(true)
    }
}
