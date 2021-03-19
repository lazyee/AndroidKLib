package com.lazyee.klib.json

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * @Author leeorz
 * @Date 3/19/21-11:37 AM
 * @Description:
 */
object MoshiJSON :JSON{
    private val moshi by lazy { Moshi.Builder().build() }
    override fun <T> toJson(entity: T?): String? {
        entity?:return null
        return moshi.adapter<T>((entity as Any).javaClass).toJson(entity)
    }

    override fun <T> fromJson(json: String?, clazz: Class<T>): T? {
        json?:return null
        return moshi.adapter(clazz).fromJson(json)
    }
}