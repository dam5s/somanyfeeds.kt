package com.somanyfeeds.kotlinextensions

// Invoke given function with caller as argument, then returns caller
public inline fun <T : Any, R> T.tap(f: (T) -> R): T {
    f(this)
    return this
}
