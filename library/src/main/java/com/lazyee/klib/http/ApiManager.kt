package com.lazyee.klib.http

import android.annotation.SuppressLint
import com.lazyee.klib.http.interceptor.AddCookiesInterceptor
import com.lazyee.klib.http.interceptor.ParamsProvider
import com.lazyee.klib.http.interceptor.HttpParamsInterceptor
import com.lazyee.klib.http.interceptor.ApiResultInterceptor
import com.lazyee.klib.http.interceptor.ReceivedCookiesInterceptor
import com.lazyee.klib.util.LogUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.util.ErrorDialogFragmentFactory.Support
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*


/**
 * @Author leeorz
 * @Date 3/8/21-4:08 PM
 * @Description:网络请求
 */
private const val TAG = "[ApiManager]"
private val defaultHostnameVerifier = HostnameVerifier { _, _ -> true }
private val defaultX509TrustManager = object : X509TrustManager {
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
private val defaultSSLSocketFactory = SSLContext.getInstance("SSL").let {
    it.init(null, arrayOf(defaultX509TrustManager), SecureRandom())
    it.socketFactory
}

class ApiManager private constructor(
    private val baseUrl: String,
    private val isSupportCookie: Boolean = false,
    private var paramsProvider: ParamsProvider? = null,
    private val interceptors: MutableList<Interceptor>,
    private val sslSocketFactory: SSLSocketFactory? = null,
    private val x509TrustManager: X509TrustManager? = null,
    private val hostnameVerifier: HostnameVerifier? = null
) {

    private lateinit var retrofit: Retrofit
    init {
        createRetrofit()
    }


    /**
     * 请求
     * @param tag Any
     * @param observable Observable<T>
     * @param callback HttpCallback<T>?
     */
    fun <T> request(tag: Any, observable: Observable<T>, callback: ApiCallback<T>? = null) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(createHttpObserver(tag,callback))
    }

    fun <T> createHttpObserver(tag: Any,callback:ApiCallback<T>? = null): Observer<T> {
        lateinit var task: RxJavaHttpTask
        val observer = object : Observer<T> {
            override fun onSubscribe(disposable: Disposable) {
                task = RxJavaHttpTask(disposable)
                addTask(tag, task)
            }

            override fun onNext(data: T) {
                removeTask(tag, task)
                handleHttpResult(data, callback)
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                removeTask(tag, task)
                if(callback is ApiCallback3<T>?){
                    callback?.onRequestFailure(e)
                }
            }

            override fun onComplete() {
                removeTask(tag, task)
            }
        }
        return observer
    }

    fun <T> create(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

    fun <T> request(tag: Any, call: Call<T>, callback: ApiCallback<T>? = null) {
        val task = RetrofitCallHttpTask(call)
        addTask(tag, task)
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                removeTask(tag, task)
                handleHttpResult(response.body(), callback)
            }

            override fun onFailure(call: Call<T>?, t: Throwable) {
                t.printStackTrace()
                removeTask(tag, task)
                if(callback is ApiCallback3<T>?){
                    callback?.onRequestFailure(t)
                }
            }
        })
    }

    /**
     * 协程请求
     */
    suspend fun <T> request(tag: Any,call:Call<T>): T? {
        val task = RetrofitCallHttpTask(call)
        try {
            val response = call.execute()
            return response.body()
        }catch (e:Exception){
            e.printStackTrace()
        }
        removeTask(tag,task)
        return null
    }

    fun <T> handleHttpResult(result: T?, callback: ApiCallback<T>?) {
        if(result == null){
            callback?.onSuccess(null)
            return
        }
        if (result !is IApiResult<*>) {
            callback?.onSuccess(result)
            return
        }

        if (isApiResultIntercept(result)){
            if(callback is ApiCallback2<T>?){
                callback?.onFailure(result)
            }
            return
        }

        if (ApiCode.isSuccessful(result.getCode())) {
            callback?.onSuccess(result)
            return
        }
        if(callback is ApiCallback2<T>?){
            callback?.onFailure(result)
        }

    }

    private fun createRetrofit() {
        val clientBuilder = OkHttpClient().newBuilder()
            .addInterceptor(getHttpLoggingInterceptor())

        if (sslSocketFactory != null && x509TrustManager != null) {
            clientBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager)
        }
        if (hostnameVerifier != null) {
            clientBuilder.hostnameVerifier(hostnameVerifier)
        }

        if (paramsProvider != null) {
            clientBuilder.addInterceptor(HttpParamsInterceptor(paramsProvider!!))
        }

        if(isSupportCookie){
            clientBuilder.addInterceptor(AddCookiesInterceptor())
            clientBuilder.addInterceptor(ReceivedCookiesInterceptor())
        }

        interceptors.forEach { clientBuilder.addInterceptor(it) }

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val moshiConverterFactory = MoshiConverterFactory.create(moshi)
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(clientBuilder.build())
            .build()
    }


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
     * api请求结果是否被拦截
     * @param result ApiResult<*>
     * @return Boolean
     */
    private fun isApiResultIntercept(result: IApiResult<*>): Boolean {
        for (interceptor in interceptors) {
            if (interceptor is ApiResultInterceptor &&  interceptor.intercept(result)) return true
        }
        return false
    }

    /**
     * 添加api请求结果拦截
     * @param interceptor ApiResultInterceptor
     */
    fun addInterceptor(interceptor: Interceptor) {
        interceptors.add(interceptor)
    }

    /**
     * 清空所有的网络请求拦截
     */
    fun clearInterceptor() {
        interceptors.clear()
    }

    companion object {
        private val tasks: HashMap<Any, MutableList<HttpTask>> = HashMap()

        /**
         * 记录请求
         * @param tag Any
         */
        private fun addTask(tag: Any, task: HttpTask) {
            if (tasks.containsKey(tag)) {
                tasks[tag]!!.add(task)
                return
            }
            tasks[tag] = mutableListOf()
            tasks[tag]!!.add(task)
        }

        /**
         * 取消请求
         * @param tag Any
         */
        fun cancel(tag: Any) {
            tasks[tag] ?: return
            val iterator = tasks[tag]!!.iterator()
            while (iterator.hasNext()) {
                val task = iterator.next()
                if (!task.isInvalid()) {
                    task.cancelTask()
                }
                iterator.remove()
            }

            if (tasks[tag]!!.isEmpty()) {
                tasks.remove(tag)
            }
        }

        private fun removeTask(tag: Any, task: HttpTask) {
            tasks[tag]?.remove(task)
        }
    }

    class Builder {
        private var baseUrl: String = ""
        private var isSupportCookie = false
        private val interceptors = mutableListOf<Interceptor>()
        private var paramsProvider: ParamsProvider? = null
        private var hostnameVerifier: HostnameVerifier? = null
        private var x509TrustManager: X509TrustManager? = null
        private var sslSocketFactory: SSLSocketFactory? = null


        fun setParamsProvider(provider: ParamsProvider): Builder {
            this.paramsProvider = provider
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            interceptors.add(interceptor)
            return this
        }

        fun baseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun setHostnameVerifier(hostnameVerifier: HostnameVerifier): Builder {
            this.hostnameVerifier = hostnameVerifier
            return this
        }

        fun setX509TrustManager(trustManager: X509TrustManager): Builder {
            this.x509TrustManager = trustManager
            return this
        }

        fun setSSLSocketFactory(sslSocketFactory: SSLSocketFactory): Builder {
            this.sslSocketFactory = sslSocketFactory
            return this
        }

        fun setSupportCookie(isSupport: Boolean): Builder {
            this.isSupportCookie = isSupport
            return this
        }


        fun build(): ApiManager {
            return ApiManager(
                baseUrl,
                isSupportCookie,
                paramsProvider,
                interceptors,
                hostnameVerifier = hostnameVerifier ?: defaultHostnameVerifier,
                sslSocketFactory = sslSocketFactory ?: defaultSSLSocketFactory,
                x509TrustManager = x509TrustManager ?: x509TrustManager
            )
        }
    }
}



