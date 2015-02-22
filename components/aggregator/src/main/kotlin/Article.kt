package com.somanyfeeds.aggregator

import java.time.ZonedDateTime

data class Article(
    val title: String,
    val link: String,
    val content: String,
    val date: ZonedDateTime
)

