package com.lazyee.klib.json

/**
 * @Author leeorz
 * @Date 3/19/21-11:35 AM
 * @Description:JSON 接口
 */
interface JSON<T> {
    val instance:T
    fun <R> toJson(entity:R?):String?
    fun <R> fromJson(json:String?,clazz: Class<R>):R?
}