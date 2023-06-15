package com.lazyee.klib.http

/**
 * @Author leeorz
 * @Date 3/9/21-4:20 PM
 * @Description:请求回调
 */

interface ApiCallback<T> {
    fun onSuccess(result:T?)

}
interface ApiCallback2<T>:ApiCallback<T> {

    /**
     * 只有业务失败(HttpResult)的时候才会回调
     */
    fun onFailure(result:T?)
}

interface ApiCallback3<T>:ApiCallback2<T> {
    fun onRequestFailure(throwable:Throwable)
}
