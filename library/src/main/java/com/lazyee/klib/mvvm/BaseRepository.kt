package com.lazyee.klib.mvvm

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * @Author leeorz
 * @Date 3/25/21-3:44 PM
 * @Description:仓库
 */
open class BaseRepository:LifecycleObserver {
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