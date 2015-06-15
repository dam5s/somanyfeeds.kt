package com.somanyfeeds.articlesdataaccess

import java.time.ZonedDateTime

data class Article(
    val id: Long? = null,
    val title: String,
    val link: String,
    val content: String,
    val date: ZonedDateTime
)
