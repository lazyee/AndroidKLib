package com.lazyee.klib.mvvm

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.lazyee.klib.http.ApiManager

/**
 * @Author leeorz
 * @Date 3/25/21-3:44 PM
 * @Description:数据仓库的基类
 */
open class MVVMBaseRepository:LifecycleObserver {
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
        ApiManager.cancel(this)
    }
}