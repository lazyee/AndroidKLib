package com.lazyee.klib.http

/**
 * @Author leeorz
 * @Date 3/9/21-4:20 PM
 * @Description:请求回调
 */
abstract class HttpCallback<T> {
    abstract fun onSuccess(result:T?)
    abstract fun onFailure(result:T?)
    abstract fun onRequestFailure(throwable:Throwable)
}