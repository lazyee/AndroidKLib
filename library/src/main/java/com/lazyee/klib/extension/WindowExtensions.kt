package com.lazyee.klib.extension

import android.view.Gravity
import android.view.Window

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