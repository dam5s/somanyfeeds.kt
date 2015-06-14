package com.somanyfeeds.feedsprocessing.atom

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText
import java.time.ZonedDateTime
import java.util.Date

class Atom {
    JacksonXmlElementWrapper(useWrapping = false)
    JsonProperty("entry")
    var entries: List<Entry> = listOf()
}

class Entry {
    var title: Title = Title()
    var link: Link = Link()
    var content: Content = Content()
    var published: Date = Date()
}

class Title {
    JacksonXmlText
    var text: String = ""
}

class Link {
    JacksonXmlProperty(isAttribute = true)
    var href: String = ""
}

class Content {
    JacksonXmlText
    var text: String = ""
}
