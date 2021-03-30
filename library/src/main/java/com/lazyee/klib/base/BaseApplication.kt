package com.lazyee.klib.base

import android.app.Application

/**
 * @Author leeorz
 * @Date 2020/11/3-11:49 AM
 * @Description:BaseApplication
 */
open class BaseApplication:Application(){

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance :BaseApplication? = null
        fun getInstance(): BaseApplication? = instance
    }

}