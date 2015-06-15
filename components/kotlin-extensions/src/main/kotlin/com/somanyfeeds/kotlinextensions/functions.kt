package com.somanyfeeds.kotlinextensions

import java.io.InputStream
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Scanner

// Invoke given function with caller as argument, then returns caller
public inline fun <T : Any, R> T.tap(f: T.() -> R): T {
    this.f()
    return this
}

public fun getResourceAsStream(path: String): InputStream? {
    val classLoader = Thread.currentThread().getContextClassLoader()
    return classLoader.getResourceAsStream(path)
}

fun Timestamp.toUtcZonedDateTime(): ZonedDateTime {
    return this.toLocalDateTime().atZone(ZoneId.of("UTC"))
}

public fun InputStream.asString(): String {
    val s = Scanner(this).useDelimiter("\\A");
    return if (s.hasNext()) s.next() else "";
}
