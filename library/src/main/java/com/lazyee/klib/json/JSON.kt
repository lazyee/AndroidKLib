package com.lazyee.klib.json

/**
 * @Author leeorz
 * @Date 3/19/21-11:35 AM
 * @Description:
 */
interface JSON {
    fun <T> toJson(entity:T?):String?
    fun <T> fromJson(json:String?,clazz: Class<T>):T?
}