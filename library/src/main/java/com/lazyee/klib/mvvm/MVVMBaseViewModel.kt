package com.lazyee.klib.mvvm

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.lazyee.klib.http.ApiManager

/**
 * @Author leeorz
 * @Date 3/25/21-3:05 PM
 * @Description:viewmodel
 */
open class MVVMBaseViewModel :ViewModel(), LifecycleObserver {
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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate(){
//        Log.e("TAG","onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart(){
//        Log.e("TAG","onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop(){
//        Log.e("TAG","onStop")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume(){
//        Log.e("TAG","onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause(){
//        Log.e("TAG","onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy(){
//        Log.e("TAG","onDestroy")
    }
}