package com.lazyee.klib.http

/**
 * @Author leeorz
 * @Date 3/8/21-5:33 PM
 * @Description:
 */
interface  HttpResult<T> {
    fun getCode():String
    fun getData():T
    fun getMsg():String
}