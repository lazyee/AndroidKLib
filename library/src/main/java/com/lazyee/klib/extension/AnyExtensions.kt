package com.lazyee.klib.extension

import android.content.res.Resources
import android.os.Looper
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:添加共有方法
 * Date: 2022/7/20 10:09
 */

fun Any.isMainThread(): Boolean = Looper.getMainLooper() == Looper.myLooper()

/**
 * The absolute width of the available display size in pixels.
 */
val Any.screenWidth: Int
    get() = Resources.getSystem().displayMetrics.widthPixels

/**
 * The absolute height of the available display size in pixels.
 */
val Any.screenHeight: Int
    get() = Resources.getSystem().displayMetrics.heightPixels
