package com.lazyee.klib.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.lazyee.klib.extension.toastLong
import com.lazyee.klib.extension.toastShort
import com.lazyee.klib.http.ApiManager
import com.lazyee.klib.mvvm.LoadingState
import com.lazyee.klib.mvvm.MVVMBaseView
import com.lazyee.klib.typed.VoidCallback

/**
 * @Author leeorz
 * @Date 2020/11/2-3:31 PM
 * @Description: fragment base 类
 */
open class BaseFragment:Fragment(),MVVMBaseView {
    val TAG :String by lazy { this::class.java.simpleName }

    var isViewCreated:Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        initViewModel(this)
    }

    /**
     * 调用activity的runOnUiThread方法
     * @param action Runnable
     */
    fun runOnUiThread(action: Runnable) {
        activity?.runOnUiThread(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        ApiManager.cancel(this)
    }

    override fun onLoadingStateChanged(state: LoadingState) {

    }

    override fun onPageLoadingStateChanged(state: LoadingState) {

    }

    override fun onShowLongToast(msg: String?) {
        if(TextUtils.isEmpty(msg))return
        activity?.toastLong(msg!!)
    }

    override fun onShowShortToast(msg: String?) {
        if(TextUtils.isEmpty(msg))return
        activity?.toastShort(msg!!)
    }

    override fun onShowLongToast(resId: Int) {
        activity?.toastLong(resId)
    }

    override fun onShowShortToast(resId: Int) {
        activity?.toastShort(resId)
    }
}