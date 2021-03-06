package com.lazyee.klib.http.interceptor

import com.lazyee.klib.http.HttpContentType
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import org.json.JSONObject

/**
 * @Author leeorz
 * @Date 3/9/21-5:28 PM
 * @Description:设置公共参数拦截器
 */
class HttpParamsInterceptor(private val paramsProviderMap: HashMap<String, HttpParamsProvider>) :
    Interceptor {
    private val TAG = "[HttpParamsInterceptor]"
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val builder = originalUrl.newBuilder()

        val headers = getHeadersByUrl(originalUrl)
        if(headers != null && headers.isNotEmpty()){
            originalRequest = addHeaderToRequest(originalRequest,headers)
        }

        val params = getParamsByUrl(originalUrl)
        if(params == null || params.isEmpty()){
            return chain.proceed(originalRequest)
        }

        val newRequest:Request?
        val url = builder.build()
        if (originalRequest.method == "POST") {
            val requestBody = setPOSTRequestParams(originalRequest,params)
            if(requestBody != null){
                newRequest = originalRequest.newBuilder().post(requestBody).build()
            }else{
                return chain.proceed(originalRequest)
            }
        } else {
            setGETRequestParams(builder, params)
            newRequest = originalRequest.newBuilder().url(url).build()
        }

        return chain.proceed(newRequest)
    }

    private fun getParamsByUrl(url: HttpUrl):HashMap<String, String>?{
        val iterator = paramsProviderMap.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (url.toString().contains(item.key)) {
                return item.value.provideParams()
            }
        }
        return null
    }

    private fun getHeadersByUrl(url:HttpUrl):HashMap<String,String>?{
        val iterator = paramsProviderMap.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (url.toString().contains(item.key)) {
                return item.value.provideHeader()
            }
        }
        return null
    }

    private fun addHeaderToRequest(request: Request,params: HashMap<String, String>):Request{
        val builder = request.newBuilder()
        params.forEach { builder.addHeader(it.key,it.value) }
        return builder.build()
    }

    private fun setPOSTRequestParams(request: Request, params: HashMap<String, String>):RequestBody?{
        when(request.body?.contentType().toString()){
            HttpContentType.APPLICATION_X_WWW_FORM_URLENCODED -> {
                val bodyBuilder = FormBody.Builder()
                val oldFormBody = request.body as FormBody
                (0 until oldFormBody.size).forEach {
                    bodyBuilder.add(oldFormBody.name(it),oldFormBody.value(it))
                }
                params.forEach { bodyBuilder.add(it.key,it.value) }
                return bodyBuilder.build()
            }
            HttpContentType.APPLICATION_JSON ->{
                val buffer = Buffer()
                request.body?.writeTo(buffer)
                val json = JSONObject(buffer.readUtf8())
                params.forEach { json.put(it.key,it.value) }
                return json.toString().toRequestBody()
            }
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