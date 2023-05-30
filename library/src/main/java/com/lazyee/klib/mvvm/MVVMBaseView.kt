package com.lazyee.klib.mvvm

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.Exception

/**
 * @Author leeorz
 * @Date 3/25/21-3:12 PM
 * @Description:MVVM中的View层
 */
interface MVVMBaseView {

    fun onLoadingStateChanged(state: LoadingState)
    fun onPageLoadingStateChanged(state: LoadingState)
    fun onShowLongToast(msg:String)
    fun onShowShortToast(msg:String)

    fun initViewModel(owner: LifecycleOwner) {
        javaClass.declaredFields.forEach { field ->
            if (field.annotations.find { it is ViewModel } != null) {
                try {
                    field.isAccessible = true
                    val target: Any? = field.get(this)
                    if (target != null && target is MVVMBaseViewModel) {
                        initViewModel(owner, target)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        javaClass.declaredMethods.forEach { method ->
            if (method.annotations.find { it is ViewModel } != null) {
                try {
                    method.isAccessible = true
                    val target: Any? = method.invoke(this)
                    if (target != null && target is MVVMBaseViewModel) {
                        initViewModel(owner, target)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun initViewModel(owner: LifecycleOwner, viewModel: MVVMBaseViewModel) {
        viewModel.getRepositoryList().forEach {
            owner.lifecycle.addObserver(it)
        }

        viewModel.loadingStateLiveData.observe(owner, object : Observer<LoadingState> {
            override fun onChanged(state: LoadingState?) {
                state ?: return
                onLoadingStateChanged(state)
            }
        })

        viewModel.pageLoadingStateLiveData.observe(owner, object : Observer<LoadingState> {
            override fun onChanged(state: LoadingState?) {
                state ?: return
                onPageLoadingStateChanged(state)
            }
        })

        viewModel.toastLongMsgLiveData.observe(owner, Observer<String> {
            it?: return@Observer
            onShowLongToast(it)
        })

        viewModel.toastShortMsgLiveData.observe(owner,Observer<String>{
            it?: return@Observer
            onShowShortToast(it)
        })
    }
}