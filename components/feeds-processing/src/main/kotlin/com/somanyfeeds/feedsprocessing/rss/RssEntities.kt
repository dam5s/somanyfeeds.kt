package com.somanyfeeds.feedsprocessing.rss

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import java.util.Date

class Rss {
    var channel: Channel = Channel()
}

class Channel {
    JacksonXmlElementWrapper(useWrapping = false)
    JsonProperty("item")
    var items: List<Item> = listOf()
}

class Item {
    var title: String = ""
    var link: String = ""
    var pubDate: Date = Date()
    var description: String = ""
}
