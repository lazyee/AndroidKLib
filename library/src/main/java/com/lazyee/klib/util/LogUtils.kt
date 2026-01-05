package com.lazyee.klib.util

import android.text.TextUtils
import android.util.Log
import com.lazyee.klib.log.Log2File
import com.lazyee.klib.log.LogLevel
import io.reactivex.internal.schedulers.ExecutorScheduler
import java.io.File
import java.util.Calendar
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

/**
 * @Author leeorz
 * @Date 2020/11/2-6:54 PM
 * @Description:日志工具类
 */
object LogUtils {

    private var isDebug = true

    fun init(isDebug: Boolean){
        this.isDebug = isDebug
    }

    fun d(tag: String?, any: Any?) {
        log(LogLevel.D, getTag(tag), getMsg(any))
    }

    fun d(any: Any?) {
        log(LogLevel.D, getTag(null), getMsg(any))
    }

    fun e(tag: String?, any: Any?) {
        log(LogLevel.E,getTag(tag), getMsg(any))
    }

    fun e(any: Any?) {
        log(LogLevel.E, getTag(null), getMsg(any))
    }

    fun i(tag: String?, any: Any?) {
        log(LogLevel.I,getTag(tag), getMsg(any))
    }

    fun i(any: Any?) {
        log(LogLevel.I, getTag(null), getMsg(any))
    }

    fun w(tag: String?,any: Any?){
        log(LogLevel.W,getTag(tag), getMsg(any))
    }

    fun w(any: Any?) {
        log(LogLevel.W, getTag(null), getMsg(any))
    }

    fun v(tag: String?,any: Any?){
        log(LogLevel.V,getTag(tag), getMsg(any))
    }
    fun v(any: Any?) {
        log(LogLevel.V, getTag(null), getMsg(any))
    }

    internal fun getTag(tag: String?):String{
        if (TextUtils.isEmpty(tag))return "[TAG]"
        return tag!!
    }

    internal fun getMsg(any:Any?):String{
        if (any == null)return "[null]"
        if (any is String) return any
        return any.toString()
    }

    internal fun log(level:LogLevel,tag: String,msg: String){
        if (!isDebug) return
        when(level){
            LogLevel.W-> Log.w(tag,msg)
            LogLevel.I-> Log.i(tag,msg)
            LogLevel.E-> Log.e(tag,msg)
            LogLevel.V-> Log.v(tag,msg)
            LogLevel.D-> Log.d(tag,msg)
        }
    }
}
