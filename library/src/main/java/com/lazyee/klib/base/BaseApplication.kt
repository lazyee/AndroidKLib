package com.lazyee.klib.base

import android.app.Application
import com.lazyee.klib.app.ActivityManager

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description: 主要是做来监听activity的生命周期
 * Date: 2022/3/21 9:26 上午
 */
class BaseApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        application = this
        ActivityManager.registerActivityLifecycleCallbacks(this)
    }

    companion object{
        private var application:Application? = null
        fun getApplication(): Application? {
            return application
        }
    }
}