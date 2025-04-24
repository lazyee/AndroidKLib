package com.lazyee.klib.annotation

import androidx.lifecycle.LifecycleOwner
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * ClassName: LiveEventBusAnnotation
 * Description: 自定义EventBus注解
 * Date: 2025/4/24 11:18
 * @author Leeorz
 */
@Target(AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class LiveEvent(val event: String)

//将方法和LiveEventBus绑定，无需在方法中手动写观察逻辑，可以像传统的EventBus一样调用
fun LifecycleOwner.observeLiveEvent(){
    javaClass.declaredMethods.forEach { method ->
        method.annotations
            .filterIsInstance<LiveEvent>()
            .forEach { annotation ->
                LiveEventBus.get<Any>(annotation.event).observe(this){ data->
                    method.invoke(this,data)
                }
            }
    }
}