package com.lazyee.klib.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lazyee.klib.app.ActivityStack
import com.lazyee.klib.http.HttpManager

/**
 * @Author leeorz
 * @Date 2020/11/2-3:29 PM
 * @Description: activity base 类,手动实现了Dagger-Android 中DaggerActivity
 */
open class BaseActivity: AppCompatActivity(){


    val TAG :String by lazy { this::class.java.simpleName }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityStack.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStack.remove(this)
        HttpManager.cancel(this)
    }

    open fun getScreenName():String{
        return javaClass.simpleName
    }


}