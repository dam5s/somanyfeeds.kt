package com.somanyfeeds.aggregator

import com.somanyfeeds.feedsdataaccess.Feed

class FeedPresenter {
    val name: String
    val path: String
    val isSelected: Boolean

    constructor(feed: Feed, slugs: Set<String>) {
        name = feed.name
        isSelected = slugs.contains(feed.slug)

        var pathSlugs: MutableSet<String> = slugs.toMutableSet()
        if (isSelected) {
            pathSlugs.remove(feed.slug)
        } else {
            pathSlugs.add(feed.slug)
        }

        path = "/${pathSlugs.sort().join(",")}"
    }
}
