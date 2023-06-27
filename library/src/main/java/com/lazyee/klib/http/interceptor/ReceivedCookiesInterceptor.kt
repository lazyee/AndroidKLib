package com.lazyee.klib.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2023/6/27 22:15
 */
class ReceivedCookiesInterceptor : Interceptor {
    companion object{
        var cookie = ""
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            for( header in originalResponse.headers("Set-Cookie")){
                if(header.contains("JSESSIONID")){
                    cookie = header.substring(header.indexOf("JSESSIONID"), header.indexOf(";"))
                    break
                }
            }
        }
        return originalResponse
    }
}