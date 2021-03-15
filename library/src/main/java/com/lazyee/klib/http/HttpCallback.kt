package com.lazyee.klib.http

/**
 * @Author leeorz
 * @Date 3/9/21-4:20 PM
 * @Description:请求回调
 */
interface HttpCallback<T> {
    fun onSuccess(data:T)
    fun onFailure(data:T? = null,throwable:Throwable? = null)
}