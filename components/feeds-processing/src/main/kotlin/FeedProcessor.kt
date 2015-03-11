package com.somanyeeds.feedsprocessing

import com.somanyeeds.feedsdataaccess.Feed

public trait FeedProcessor {
    fun process(feed: Feed)
}
