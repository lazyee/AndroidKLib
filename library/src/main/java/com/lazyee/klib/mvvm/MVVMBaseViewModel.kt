package com.lazyee.klib.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @Author leeorz
 * @Date 3/25/21-3:05 PM
 * @Description:viewmodel
 */
open class MVVMBaseViewModel :ViewModel() {
    val loadingStateLiveData = MutableLiveData<LoadingState>()
    val pageLoadingStateLiveData = MutableLiveData<LoadingState>()
    private val mRepositoryList = mutableListOf<MVVMBaseRepository>()

    fun getRepositoryList():List<MVVMBaseRepository>{
        if(mRepositoryList.isEmpty()){
            javaClass.declaredFields.forEach { field ->
                if (field.annotations.find { it is ViewModel } != null) {
                    try {
                        field.isAccessible = true
                        val target: Any? = field.get(this)
                        if (target != null && target is MVVMBaseRepository) {
                            mRepositoryList.add(target)
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
                        if (target != null && target is MVVMBaseRepository) {
                            mRepositoryList.add(target)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        return mRepositoryList
    }

    fun onLoading(){
        loadingStateLiveData.value = LoadingState.LOADING
    }

    fun onLoadSuccess(){
        loadingStateLiveData.value = LoadingState.SUCCESS
    }

    fun onLoadFailure(){
        loadingStateLiveData.value = LoadingState.FAILURE
    }

    fun onPageLoading(){
        pageLoadingStateLiveData.value = LoadingState.LOADING
    }

    fun onPageLoadSuccess(){
        pageLoadingStateLiveData.value = LoadingState.SUCCESS
    }

    fun onPageLoadFailure(){
        pageLoadingStateLiveData.value = LoadingState.FAILURE
    }
}