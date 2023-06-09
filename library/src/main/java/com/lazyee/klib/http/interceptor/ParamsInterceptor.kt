package com.lazyee.klib.http.interceptor

import com.lazyee.klib.http.HttpContentType
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import org.json.JSONObject

/**
 * @Author leeorz
 * @Date 3/9/21-5:28 PM
 * @Description:设置公共参数拦截器
 */
class HttpParamsInterceptor(private val paramsProvider: HttpParamsProvider) :
    Interceptor {
    private val TAG = "[HttpParamsInterceptor]"
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val builder = originalUrl.newBuilder()

        val headers = paramsProvider.provideHeader()
        if(!headers.isNullOrEmpty()){
            originalRequest = addHeaderToRequest(originalRequest,headers)
        }

        val params = paramsProvider.provideParams()
        if(params.isNullOrEmpty()){
            return chain.proceed(originalRequest)
        }

        val newRequest:Request?
        if (originalRequest.method == "POST") {
            val requestBody = setPOSTRequestParams(originalRequest,params)
            if(requestBody != null){
                newRequest = originalRequest.newBuilder().post(requestBody).build()
            }else{
                return chain.proceed(originalRequest)
            }
        } else {
            setGETRequestParams(builder, params)
            newRequest = originalRequest.newBuilder().url(builder.build()).build()
        }

        return chain.proceed(newRequest)
    }



    private fun addHeaderToRequest(request: Request,params: HashMap<String, String>):Request{
        val builder = request.newBuilder()
        params.forEach { builder.addHeader(it.key,it.value) }
        return builder.build()
    }

    private fun setPOSTRequestParams(request: Request, params: HashMap<String, String>):RequestBody?{
        if(params.isEmpty()) return request.body

        val contentType = request.body?.contentType().toString()
        if(contentType.contains(HttpContentType.APPLICATION_X_WWW_FORM_URLENCODED)){
            val bodyBuilder = FormBody.Builder()
            val oldFormBody = request.body as FormBody
            (0 until oldFormBody.size).forEach {
                bodyBuilder.add(oldFormBody.name(it),oldFormBody.value(it))
            }
            params.forEach { bodyBuilder.add(it.key,it.value) }
            return bodyBuilder.build()
        }

        if(contentType.contains(HttpContentType.APPLICATION_JSON)){
            val buffer = Buffer()
            request.body?.writeTo(buffer)
            val json = JSONObject(buffer.readUtf8())
            params.forEach { json.put(it.key,it.value) }
            return json.toString().toRequestBody(contentType.toMediaType())
        }
        return request.body
    }

    private fun setGETRequestParams(builder: HttpUrl.Builder, params: HashMap<String, String>){
        if(params.isEmpty())return
        params.forEach { builder.addQueryParameter(it.key, it.value) }
    }
}

/**
 * 公共参数的提供者
 */
interface HttpParamsProvider {
    fun provideParams(): HashMap<String, String>?
    fun provideHeader():HashMap<String, String>?
}