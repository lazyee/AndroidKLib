package com.lazyee.klib.extension

import android.content.Context
import com.lazyee.klib.base.BaseActivity
import com.lazyee.klib.base.BaseFragment
import com.lazyee.klib.util.LogUtils

/**
 * @Author leeorz
 * @Date 2020/11/3-12:47 PM
 * @Description:日志输出拓展方法，BaseActivity,BaseFragment,Context实例可以直接调用日志输出方法
 * 实际调用的是LogUtils的中的方法
 */

/**
 * 日志输出 级别 e
 * @receiver Context
 * @param any Any?
 */
fun BaseActivity.e(any: Any?){
    LogUtils.e(TAG,any)
}
/**
 * 日志输出 级别 i
 * @receiver Context
 * @param any Any?
 */
fun BaseActivity.i(any: Any?){
    LogUtils.i(TAG,any)
}

/**
 * 日志输出 级别 d
 * @receiver BaseActivity
 * @param any Any?
 */
fun BaseActivity.d(any: Any?){
    LogUtils.d(TAG,any)
}

/**
 * 日志输出 级别 v
 * @receiver BaseActivity
 * @param any Any?
 */
fun BaseActivity.v(any: Any?){
    LogUtils.v(TAG,any)
}

/**
 * 日志输出 级别 w
 * @receiver BaseActivity
 * @param any Any?
 */
fun BaseActivity.w(any: Any?){
    LogUtils.w(TAG,any)
}

/**
 * 日志输出 级别 e
 * @receiver Context
 * @param any Any?
 */
fun BaseFragment.e(any: Any?){
    LogUtils.e(TAG,any)
}
/**
 * 日志输出 级别 i
 * @receiver Context
 * @param any Any?
 */
fun BaseFragment.i(any: Any?){
    LogUtils.i(TAG,any)
}

/**
 * 日志输出 级别 d
 * @receiver BaseActivity
 * @param any Any?
 */
fun BaseFragment.d(any: Any?){
    LogUtils.d(TAG,any)
}

/**
 * 日志输出 级别 v
 * @receiver BaseFragment
 * @param any Any?
 */
fun BaseFragment.v(any: Any?){
    LogUtils.v(TAG,any)
}

/**
 * 日志输出 级别 w
 * @receiver BaseFragment
 * @param any Any?
 */
fun BaseFragment.w(any: Any?){
    LogUtils.w(TAG,any)
}


/**
 * 日志输出 级别 e
 * @receiver Context
 * @param tag String?
 * @param any Any?
 */
fun Context.e(tag:String?, any:Any?){
    LogUtils.e(tag,any)
}

/**
 * 日志输出 级别 i
 * @receiver Context
 * @param tag String?
 * @param any Any?
 */
fun Context.i(tag:String?, any:Any?){
    LogUtils.i(tag,any)
}

/**
 * 日志输出 级别 d
 * @receiver Context
 * @param tag String?
 * @param any Any?
 */
fun Context.d(tag:String?, any:Any?){
    LogUtils.d(tag,any)
}

/**
 * 日志输出 级别 v
 * @receiver Context
 * @param tag String?
 * @param any Any?
 */
fun Context.v(tag:String?, any:Any?){
    LogUtils.v(tag,any)
}

/**
 * 日志输出 级别 w
 * @receiver Context
 * @param tag String?
 * @param any Any?
 */
fun Context.w(tag:String?, any:Any?){
    LogUtils.w(tag,any)
}