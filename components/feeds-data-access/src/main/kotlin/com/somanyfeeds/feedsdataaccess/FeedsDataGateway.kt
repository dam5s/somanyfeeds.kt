package com.somanyfeeds.feedsdataaccess

import com.somanyfeeds.databaseaccess.withMapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.session.SqlSessionFactory
import javax.inject.Inject

interface FeedsDataGateway {
    fun selectAllFeeds(): List<Feed>
}

class PostgresFeedsDataGateway
    @Inject constructor(val sqlSessionFactory: SqlSessionFactory) : FeedsDataGateway {

    override fun selectAllFeeds(): List<Feed> =
        sqlSessionFactory.withMapper(javaClass<FeedDataMapper>()) {
            selectFeeds().map { it.buildFeed() }
        }
}

interface FeedDataMapper {
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
