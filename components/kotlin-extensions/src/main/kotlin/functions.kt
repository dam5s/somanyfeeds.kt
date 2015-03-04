package com.somanyfeeds.kotlinextensions

import java.io.InputStream
import java.time.ZonedDateTime
import java.sql.Timestamp
import java.time.ZoneId

// Invoke given function with caller as argument, then returns caller
public inline fun <T : Any, R> T.tap(f: (T) -> R): T {
    f(this)
    return this
}

public fun Any.getResourceAsStream(path: String): InputStream? {
    return javaClass<Any>().getResourceAsStream(path)
}

fun Timestamp.toUtcZonedDateTime(): ZonedDateTime {
    return this.toLocalDateTime().atZone(ZoneId.of("UTC"))
}
