package com.lazyee.klib.extension

import android.view.Gravity
import android.view.Window
import android.view.WindowManager

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:window的拓展方法
 * Date: 2022/5/17 1:41 下午
 */

fun Window.setSize(width:Int, height:Int){
    val lp = attributes
    lp.width = width
    lp.height = height
    attributes = lp
}

fun Window.setSize(width:Int, height:Int,gravity: Int = Gravity.CENTER){
    val lp = attributes
    lp.width = width
    lp.height = height
    lp.gravity = gravity
    attributes = lp
}

fun Window.hideKeyboard(){
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}

fun Window.showKeyboard(){
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
}

/**
 * 设置屏幕常亮
 */
fun Window.keepScreenOn(){
    addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

/**
 * 移除屏幕常亮
 */
fun Window.keepScreenOff(){
    clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}