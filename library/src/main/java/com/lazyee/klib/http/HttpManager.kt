package com.lazyee.klib.http

import android.annotation.SuppressLint
import android.text.TextUtils
import com.lazyee.klib.http.interceptor.HttpParamsProvider
import com.lazyee.klib.http.interceptor.HttpParamsInterceptor
import com.lazyee.klib.http.interceptor.HttpResultInterceptor
import com.lazyee.klib.util.LogUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager


/**
 * @Author leeorz
 * @Date 3/8/21-4:08 PM
 * @Description:网络请求
 */

object HttpManager {
    private const val TAG = "[HttpManager]"
    private lateinit var defaultRetrofit: Retrofit
    private val retrofits: HashMap<String, Retrofit> = HashMap()
    private val paramsAdapterMap : HashMap<String, HttpParamsProvider> = HashMap()
    private val tasks: HashMap<Any, Disposable> = HashMap()

    private val httpResultInterceptors = mutableListOf<HttpResultInterceptor>()

    /**
     * 初始化Retrofit
     */
    fun init(baseUrl: String) {
        defaultRetrofit = switch(baseUrl)
    }

    /**
     * 设置公共参数
     * @param baseUrl String
     * @param provider HttpParamsProvider
     */
    fun setParamsProvider(baseUrl: String, provider: HttpParamsProvider){
        paramsAdapterMap[baseUrl] = provider
    }

    fun <T> create(clazz: Class<T>): T {
        return switch().create(clazz)
    }

    /**
     * 请求
     * @param tag Any
     * @param observable Observable<T>
     * @param callback HttpCallback<T>?
     * @return Observable<T>
     */
    fun <T> request(
        tag: Any,
        observable: Observable<T>,
        callback: HttpCallback<T>? = null
    ): Observable<T> {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(obtainObserver(tag, callback))
        return observable
    }

    private fun <T> obtainObserver(tag: Any, callback: HttpCallback<T>? = null): Observer<T> {
        return object : Observer<T> {
            override fun onSubscribe(disposable: Disposable) {
                addTask(tag, disposable)
            }

            override fun onNext(data: T) {
                removeTask(tag)

                if(data is HttpResult<*>){
                    if(isHttpResultIntercept(data))return
                    if(HttpCode.isSuccessful(data.getCode())){
                        callback?.onSuccess(data)
                    }else{
                        callback?.onFailure(data)
                    }
                    return
                }
                callback?.onSuccess(data)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                removeTask(tag)
                callback?.onFailure(throwable = e)
            }

            override fun onComplete() {
                removeTask(tag)
            }
        }
    }


    private fun createRetrofit(baseUrl: String): Retrofit {
        val clientBuilder = OkHttpClient().newBuilder()
            .addInterceptor(HttpParamsInterceptor(paramsAdapterMap))
            .addInterceptor(getHttpLoggingInterceptor())
//            .addInterceptor(GZipInterceptor())
            .sslSocketFactory(ssLSocketFactory, x509TrustManager)
            .hostnameVerifier(hostnameVerifier)

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val moshiConverterFactory = MoshiConverterFactory.create(moshi)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(clientBuilder.build())
            .build()
    }


    /**
     * 切换retrofit实例
     * @param baseUrl String?
     * @return Retrofit
     */
    fun switch(baseUrl: String? = null): Retrofit {
        if (!TextUtils.isEmpty(baseUrl)) {
            return retrofits[baseUrl]
                ?: return createRetrofit(baseUrl!!)
        }
        return defaultRetrofit
    }

    private var hostnameVerifier: HostnameVerifier = HostnameVerifier { _, _ -> true }

    /**
     * 设置日志拦截
     * @return HttpLoggingInterceptor
     */
    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        //新建log拦截器
        return HttpLoggingInterceptor {
            LogUtils.d(TAG, it)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    /**
     * 默认信任所有
     */
    private val x509TrustManager = object : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }


        @SuppressLint("TrustAllX509TrustManager")
        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    private var ssLSocketFactory: SSLSocketFactory =
        SSLContext.getInstance("SSL").let {
            it.init(null, arrayOf(x509TrustManager), SecureRandom())
            it.socketFactory
        }

    /**
     * 记录请求
     * @param tag Any
     * @param disposable Disposable
     */
    private fun addTask(tag: Any, disposable: Disposable) {
        cancel(tag)
        removeTask(tag)
        tasks[tag] = disposable
    }

    /**
     * 取消请求
     * @param tag Any
     */
    fun cancel(tag: Any) {
        tasks[tag] ?: return
        if (tasks[tag]!!.isDisposed) {
            tasks.remove(tag)
            return
        }
        tasks[tag]!!.dispose()

        cancelAllDisposedTask()
    }

    private fun removeTask(tag: Any) {
        tasks.remove(tag)
    }

    fun cancelAllTask() {
        tasks.forEach { cancel(it.key) }
        tasks.clear()
    }

    /**
     * 清除所有Disposed的请求
     */
    private fun cancelAllDisposedTask(){
        val iterator = tasks.iterator()
        while (iterator.hasNext()){
            val task = iterator.next()
            if(task.value.isDisposed){
                iterator.remove()
            }
        }
    }

    /**
     * 网络请求结果是否被拦截
     * @param httpResult HttpResult<*>
     * @return Boolean
     */
    fun isHttpResultIntercept(httpResult: HttpResult<*>): Boolean {
        for(interceptor in httpResultInterceptors){
            if(interceptor.intercept(httpResult))return true
        }
        return false
    }

    /**
     * 添加网络请求拦截
     * @param interceptor HttpResultInterceptor
     */
    fun addHttpResultInterceptor(interceptor: HttpResultInterceptor){
        httpResultInterceptors.add(interceptor)
    }

    /**
     * 清空所有的网络请求拦截
     */
    fun clearHttpResultInterceptor(){
        httpResultInterceptors.clear()
    }
}

