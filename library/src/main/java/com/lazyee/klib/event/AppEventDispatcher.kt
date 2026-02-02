package com.lazyee.klib.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

object AppEventDispatcher {

    fun register(target: Any, scope: CoroutineScope) {
        val methods = target::class.java.declaredMethods
            .filter { it.isAnnotationPresent(AppEvent::class.java) }

        if (methods.isEmpty()) return

        scope.launch {
            AppEventBus.events.collect { event ->
                methods.forEach { method ->
                    val paramType = method.parameterTypes.firstOrNull()
                    if (paramType?.isAssignableFrom(event::class.java) == true) {
                        method.isAccessible = true
                        method.invoke(target, event)
                    }
                }
            }
        }
    }
}