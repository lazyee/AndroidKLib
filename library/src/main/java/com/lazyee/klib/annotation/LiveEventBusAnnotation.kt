package com.lazyee.klib.annotation

import androidx.lifecycle.LifecycleOwner
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlin.reflect.KClass

/**
 * ClassName: LiveEventBusAnnotation
 * Description: 自定义EventBus注解
 * Date: 2025/4/24 11:18
 * @author Leeorz
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LiveEvent(val event: String)

//将方法和LiveEventBus绑定，无需在方法中手动写观察逻辑，可以像传统的EventBus一样调用
fun LifecycleOwner.observeLiveEvent(){
    javaClass.declaredMethods.forEach { method ->
        method.annotations
            .filterIsInstance<LiveEvent>()
            .forEach { annotation ->
                if (method.parameterCount != 1){
                    throw IllegalArgumentException("用@LiveBus注解修饰的方法[${method.name}]的参数必须只能有一个")
                }
                LiveEventBus.get<Any>(annotation.event).observe(this){ data->
                    method.invoke(this,data)
                }
            }
    }
}


//@Target(AnnotationTarget.FUNCTION)
//@Retention(AnnotationRetention.RUNTIME)
//annotation class LiveEvent2(val clazz: KClass<*>)
