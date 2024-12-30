package com.lazyee.klib.extension

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2023/6/12 14:46
 */

fun Activity.hideKeyboard(view:View){
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showKeyboard(view:View){
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}


/**
 * 进入全屏模式
 * @isHideActionBar 是否隐藏actionBar 默认隐藏
 */
fun Activity.enterFullScreen(isHideActionBar:Boolean = true){
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    if(!isHideActionBar)return
    if(actionBar != null){
        actionBar?.hide()
        return
    }
    if(this is AppCompatActivity && supportActionBar != null){
        supportActionBar?.hide()
        return
    }
}

/**
 * 退出全屏模式
 * @isShowActionBar 是否显示actionBar 默认显示
 */
fun Activity.exitFullScreen(isShowActionBar:Boolean = true){
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    if(!isShowActionBar)return
    if(actionBar != null){
        actionBar?.show()
        return
    }

    if(this is AppCompatActivity && supportActionBar != null){
        supportActionBar?.show()
        return
    }
}

/**
 * 切换到竖屏
 */
fun Activity.changeOrientationPortrait() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

/**
 * 切换到横屏
 */
fun Activity.changeOrientationLandscape() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}