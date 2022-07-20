package com.lazyee.klib.extension

import android.os.Looper
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:添加共有方法
 * Date: 2022/7/20 10:09
 */

fun Any.isMainThread(): Boolean {
    return Looper.getMainLooper() == Looper.myLooper()
}