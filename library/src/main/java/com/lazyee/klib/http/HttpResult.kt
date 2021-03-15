package com.lazyee.klib.http

/**
 * @Author leeorz
 * @Date 3/8/21-5:33 PM
 * @Description:
 */
abstract class HttpResult<T> {
    abstract fun getCode():String
    abstract fun getData():T
    abstract fun getMsg():String
}