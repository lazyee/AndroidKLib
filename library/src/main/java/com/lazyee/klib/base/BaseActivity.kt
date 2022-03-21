package com.lazyee.klib.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.lazyee.klib.app.ActivityManager
import com.lazyee.klib.http.HttpUtil
import com.lazyee.klib.mvvm.LoadingState
import com.lazyee.klib.mvvm.MVVMBaseView
import kotlin.reflect.KClass

/**
 * @Author leeorz
 * @Date 2020/11/2-3:29 PM
 * @Description: activity base 类
 */
open class  BaseActivity: AppCompatActivity(), MVVMBaseView {
    val TAG :String by lazy { this::class.java.simpleName }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        HttpUtil.cancel(this)
    }

    override fun onLoadingStateChanged(state: LoadingState) {

    }

    override fun onPageLoadingStateChanged(state: LoadingState) {

    }

}