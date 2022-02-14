package com.lazyee.klib.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lazyee.klib.app.ActivityManager
import com.lazyee.klib.http.HttpManager
import com.lazyee.klib.mvvm.LoadingState
import com.lazyee.klib.mvvm.MVVMBaseView

/**
 * @Author leeorz
 * @Date 2020/11/2-3:29 PM
 * @Description: activity base 类
 */
open class BaseActivity: AppCompatActivity(), MVVMBaseView {
    val TAG :String by lazy { this::class.java.simpleName }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.add(this)
        initViewModel(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.remove(this)
        HttpManager.cancel(this)
    }

    override fun onLoadingStateChanged(state: LoadingState) {

    }

    override fun onPageLoadingStateChanged(state: LoadingState) {

    }
}