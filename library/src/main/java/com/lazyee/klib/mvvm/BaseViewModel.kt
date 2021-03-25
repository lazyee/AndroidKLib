package com.lazyee.klib.mvvm

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @Author leeorz
 * @Date 3/25/21-3:05 PM
 * @Description:viewmodel
 */
open class BaseViewModel :ViewModel() {
    val loadingStateLiveData = MutableLiveData<LoadingState>()

    open fun getRepositoryList():List<BaseRepository>{
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