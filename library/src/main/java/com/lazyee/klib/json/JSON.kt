package com.lazyee.klib.json

/**
 * @Author leeorz
 * @Date 3/19/21-11:35 AM
 * @Description:JSON 接口，后面使用的时候应该直接使用JSON接口实现的对象，这样日后如果需要替换的实现的时候可以快速切换
 */
interface JSON<T> {
    val instance:T
    fun <R> toJson(entity:R?):String?
    fun <R> fromJson(json:String?,clazz: Class<R>):R?
}
