package com.lazyee.klib.extension

import android.content.Context
import com.lazyee.klib.app.ContextManager

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2022/5/16 4:51 下午
 */
fun Number.dpToPx(): Number {
    return ContextManager.last?.dp2px(this.toFloat())?:this
}

fun Number.dpToPx(context: Context): Number {
    return this.toFloat().dpToPx()
}