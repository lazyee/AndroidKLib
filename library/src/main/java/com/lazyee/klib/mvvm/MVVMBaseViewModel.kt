package com.lazyee.klib.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazyee.klib.http.ApiManager
import com.lazyee.klib.http.IApiResult
import com.lazyee.klib.typed.TCallback
import com.lazyee.klib.typed.VoidCallback
import com.lazyee.klib.util.LogUtils
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

/**
 * @Author leeorz
 * @Date 3/25/21-3:05 PM
 * @Description:viewmodel
 */
open class MVVMBaseViewModel :ViewModel() {
    val loadingStateLiveData = MutableLiveData<LoadingState>()
    val pageLoadingStateLiveData = MutableLiveData<LoadingState>()
    val toastLongMsgLiveData = MutableLiveData<String?>()
    val toastShortMsgLiveData = MutableLiveData<String?>()
    val toastLongResIdLiveData = MutableLiveData<Int>()
    val toastShortResIdLiveData = MutableLiveData<Int>()
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
        loadingStateLiveData.postValue(LoadingState.LOADING)
    }

    fun onLoadSuccess(){
        loadingStateLiveData.postValue(LoadingState.SUCCESS)
    }

    fun onLoadFailure(){
        loadingStateLiveData.postValue(LoadingState.FAILURE)
    }

    fun onPageLoading(){
        pageLoadingStateLiveData.postValue(LoadingState.LOADING)
    }

    fun onPageLoadSuccess(){
        pageLoadingStateLiveData.postValue(LoadingState.SUCCESS)
    }

    fun onPageLoadFailure(){
        pageLoadingStateLiveData.postValue(LoadingState.FAILURE)
    }

    fun toastLong(msg:String?){
        toastLongMsgLiveData.postValue(msg)
    }

    fun toastShort(msg:String?){
        toastShortMsgLiveData.postValue(msg)
    }

    fun toastLong(resId:Int){
        toastLongResIdLiveData.postValue(resId)
    }

    fun toastShort(resId:Int){
        toastShortResIdLiveData.postValue(resId)
    }

    override fun onCleared() {
        super.onCleared()
        LogUtils.e("viewMode onCleared")
        mRepositoryList.forEach { it.onCleared() }
    }

    /**
     * 协程环境下请求网络接口
     */
    fun <T> request(block:suspend ()-> T,
                   onSuccess:TCallback<T>? = null,
                   onFailure:TCallback<T>? = null,
                   onRequestFailure:TCallback<Throwable>? = null,
                   onFinal:VoidCallback? = null){
        viewModelScope.launch {
            ApiManager.request(block,onSuccess,onFailure,onRequestFailure,onFinal)
        }
    }

}