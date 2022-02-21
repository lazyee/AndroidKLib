package com.lazyee.klib.http

import io.reactivex.disposables.Disposable
import retrofit2.Call

interface HttpTask {
    fun cancelTask()
    fun isInvalid(): Boolean
}

class RxJavaHttpTask(private val disposable: Disposable) : HttpTask {
    override fun cancelTask() {
        if (!isInvalid()) {
            disposable.dispose()
        }
    }

    override fun isInvalid(): Boolean {
        return disposable.isDisposed
    }
}

class RetrofitCallHttpTask(private val call: Call<*>) : HttpTask {
    override fun cancelTask() {
        if (!isInvalid()) {
            call.cancel()
        }
    }

    override fun isInvalid(): Boolean {
        return call.isCanceled
    }

}