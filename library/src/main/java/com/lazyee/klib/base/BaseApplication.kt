package com.lazyee.klib.base

import android.app.Application
import com.lazyee.klib.app.AppManager
import com.lazyee.klib.util.AppUtils

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description: 主要是做来监听activity的生命周期
 * Date: 2022/3/21 9:26 上午
 */
open class BaseApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        AppManager.register(this)
    }

    fun isMainProcess():Boolean = packageName == AppUtils.getCurrentProcessName(applicationContext)
}