package com.somanyeeds.aggregator

data class Feed(
    val id: Long? = 0,
    val name: String,
    val slug: String,
    val url: String,
    val type: FeedType
)

enum class FeedType {
    RSS
    ATOM
}
