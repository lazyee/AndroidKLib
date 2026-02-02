package com.lazyee.klib.event

import kotlinx.coroutines.flow.MutableSharedFlow

object AppEventBus {
    private val _events = MutableSharedFlow<Any>(
        extraBufferCapacity = 1
    )

    val events = _events

    fun post(event: Any) {
        _events.tryEmit(event)
    }
}