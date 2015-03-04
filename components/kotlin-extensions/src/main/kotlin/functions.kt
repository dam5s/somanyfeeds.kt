package com.somanyfeeds.kotlinextensions

import java.io.InputStream

// Invoke given function with caller as argument, then returns caller
public inline fun <T : Any, R> T.tap(f: (T) -> R): T {
    f(this)
    return this
}

public fun Any.getResourceAsStream(path: String): InputStream? {
    return javaClass<Any>().getResourceAsStream(path)
}
