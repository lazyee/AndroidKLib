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

    open fun getRepositoryList():List<MVVMBaseRepository>{
        return listOf()
    }

    fun onLoading(){
        loadingStateLiveData.value = LoadingState.LOADING
    }

    fun onSuccess(){
        loadingStateLiveData.value = LoadingState.SUCCESS
    }

    fun onFailure(){
        loadingStateLiveData.value = LoadingState.FAILURE
    }
}