package com.lazyee.klib.http.interceptor

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2023/6/27 22:19
 */
class AddCookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        //添加Cookie
        if(!TextUtils.isEmpty(ReceivedCookiesInterceptor.cookie)){
            builder.addHeader("Cookie", ReceivedCookiesInterceptor.cookie)
        }
        return chain.proceed(builder.build());
    }
}