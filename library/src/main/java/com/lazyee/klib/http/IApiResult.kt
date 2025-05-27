package com.lazyee.klib.http

/**
 * @Author leeorz
 * @Date 3/8/21-5:33 PM
 * @Description:请求结果
 */
interface IApiResult<T> {
    val code:String
    val data:T?
    val msg:String?
}