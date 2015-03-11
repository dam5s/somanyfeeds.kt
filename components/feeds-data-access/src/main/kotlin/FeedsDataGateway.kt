package com.somanyeeds.feedsdataaccess

import org.apache.ibatis.annotations.Select

trait FeedsDataGateway {
    fun selectFeeds(): List<Feed>
}

class PostgresFeedsDataGateway(val feedDataMapper: FeedDataMapper) : FeedsDataGateway {
    override fun selectFeeds(): List<Feed> =
        feedDataMapper.selectFeeds().map { it.buildFeed() }
}

trait FeedDataMapper {
    Select("select * from feed")
    fun selectFeeds(): List<FeedMapping>;
}

private class FeedMapping() {
    var id: Long = 0
    var name: String = ""
    var slug: String = ""
    var url: String = ""
    var type: FeedType = FeedType.RSS

    fun buildFeed(): Feed {
        return Feed(
            id = this.id,
            name = this.name,
            slug = this.slug,
            url = this.url,
            type = this.type
        )
    }
}
