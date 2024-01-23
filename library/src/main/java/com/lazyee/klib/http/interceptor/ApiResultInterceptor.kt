package com.lazyee.klib.http.interceptor

import com.lazyee.klib.http.IApiResult

/**
 * @Author leeorz
 * @Date 3/16/21-12:59 PM
 * @Description:请求数据拦截
 */
interface ApiResultInterceptor {
    fun intercept(result: IApiResult<*>):Boolean
}