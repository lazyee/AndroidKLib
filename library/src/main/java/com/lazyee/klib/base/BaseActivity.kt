package com.lazyee.klib.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lazyee.klib.app.ActivityManager
import com.lazyee.klib.http.HttpManager

/**
 * @Author leeorz
 * @Date 2020/11/2-3:29 PM
 * @Description: activity base 类
 */
open class BaseActivity: AppCompatActivity(){
    val TAG :String by lazy { this::class.java.simpleName }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.remove(this)
        HttpManager.cancel(this)
    }

    open fun getScreenName():String{
        return javaClass.simpleName
    }


}