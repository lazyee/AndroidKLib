package com.lazyee.klib.extension

import android.content.Context
import com.lazyee.klib.app.ActivityManager
import com.lazyee.klib.base.BaseApplication

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2022/5/16 4:51 下午
 */
fun Float.dpToPx(): Int {
    return ActivityManager.last?.dp2px(this)?:this.toInt()
}

fun Int.dpToPx(context: Context): Int {
    return this.toFloat().dpToPx()
}