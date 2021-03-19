package com.lazyee.klib.json

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * @Author leeorz
 * @Date 3/19/21-11:37 AM
 * @Description:Moshi 数据解析 支持kotlin
 */
object KMoshiJSON :JSON<Moshi>{
    private val kMoshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

    override val instance: Moshi = kMoshi

    override fun <T> toJson(entity: T?): String? {
        entity?:return null
        return instance.adapter<T>((entity as Any).javaClass).toJson(entity)
    }

    override fun <T> fromJson(json: String?, clazz: Class<T>): T? {
        json?:return null
        return instance.adapter(clazz).fromJson(json)
    }
}