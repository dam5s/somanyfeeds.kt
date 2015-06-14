package com.somanyfeeds.feedsdataaccess

data class Feed(
    val id: Long? = null,
    val name: String,
    val slug: String,
    val url: String,
    val type: FeedType
)

enum class FeedType {
    RSS
    ATOM
}
