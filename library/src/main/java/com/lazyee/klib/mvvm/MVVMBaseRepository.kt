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
open class MVVMBaseRepository {

    open fun onCleared(){
        ApiManager.cancel(this)
    }
}