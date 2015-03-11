package com.somanyfeeds.aggregator;

trait FeedUpdatesScheduler {
    fun start()
    fun stop()
}

class DefaultFeedUpdatesScheduler : FeedUpdatesScheduler {
    override fun stop() {
        throw UnsupportedOperationException()
    }

    override fun start() {
        throw UnsupportedOperationException()
    }
}
