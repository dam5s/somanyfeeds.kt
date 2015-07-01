package com.somanyfeeds.aggregator

import com.somanyfeeds.feedsdataaccess.Feed
import javax.inject.Inject

class FeedPresenter @Inject constructor() {
    fun present(feed: Feed, slugs: Set<String>): FeedViewModel {
        val name = feed.name
        val isSelected = slugs.contains(feed.slug)

        var pathSlugs: MutableSet<String> = slugs.toMutableSet()
        if (isSelected) {
            pathSlugs.remove(feed.slug)
        } else {
            pathSlugs.add(feed.slug)
        }

        val path = "/${pathSlugs.sort().join(",")}"

        return FeedViewModel(name, path, isSelected)
    }
}

data class FeedViewModel(
    val name: String,
    val path: String,
    val isSelected: Boolean
)
