package com.lazyee.klib.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lazyee.klib.http.HttpManager

/**
 * @Author leeorz
 * @Date 2020/11/2-3:31 PM
 * @Description: fragment base 类
 */
open class BaseFragment:Fragment() {
    val TAG :String by lazy { this::class.java.simpleName }

    var isViewCreated:Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
    }

    /**
     * 调用activity的runOnUiThread方法
     * @param action Runnable
     */
    fun runOnUiThread(action: Runnable) {
        activity?.runOnUiThread(action)
    }

    open fun getScreenName():String{
        return javaClass.simpleName
    }

    override fun onDestroy() {
        super.onDestroy()
        HttpManager.cancel(this)
    }
}