package com.lazyee.klib.json

import com.lazyee.klib.json.moshi.MoshiArrayListJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * @Author leeorz
 * @Date 3/19/21-11:37 AM
 * @Description:Moshi 数据解析
 */
object MoshiJSON : JSON<Moshi> {
    private val moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(MoshiArrayListJsonAdapter.FACTORY)
            .build()
    }
    override val instance: Moshi = moshi
    override fun <T> toJson(entity: T?): String? {
        entity ?: return null
        return instance.adapter<T>((entity as Any).javaClass).toJson(entity)
    }

    override fun <T> fromJson(json: String?, clazz: Class<T>): T? {
        json ?: return null
        return instance.adapter(clazz).fromJson(json)
    }
}