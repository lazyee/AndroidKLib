package com.lazyee.klib.app

import android.content.Context
import android.os.Process
import com.lazyee.klib.log.Log2File
import java.io.File
import kotlin.system.exitProcess

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:全局异常捕捉
 * Date: 2023/10/31 10:00
 */
object GlobalExceptionCatcher {
    private lateinit var mCrashLogDirPath:String
    private lateinit var mApplicationContext:Context
    private var mUncaughtExceptionCallback:UncaughtExceptionCallback? = null

    private val crashLog2File by lazy { Log2File(mApplicationContext,true,mCrashLogDirPath,"crash") }

    fun init(applicationContext: Context) {
        init(applicationContext,null)
    }

    fun init(applicationContext: Context,callback: UncaughtExceptionCallback?){
        mUncaughtExceptionCallback = callback
        mApplicationContext = applicationContext
        mCrashLogDirPath = applicationContext.filesDir.absolutePath + File.separator + "crash"
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler)
    }


    fun exitProcess(){
        Process.killProcess(Process.myPid())
        exitProcess(1)
    }

    private val mUncaughtExceptionHandler:Thread.UncaughtExceptionHandler = Thread.UncaughtExceptionHandler { thread, throwable ->
        crashLog2File.log(throwable.stackTraceToString())
        mUncaughtExceptionCallback?.onUncaughtException(throwable)
    }

    fun clearCrashLog(): Boolean {
        if(!this::mCrashLogDirPath.isInitialized )return false
        val crashLogDir = File(mCrashLogDirPath)
        if(!crashLogDir.exists())return false
        crashLogDir.listFiles()?.forEach {
            it.delete()
        }
        return true
    }

    fun getLastCrashLog():File?{
        if(!this::mCrashLogDirPath.isInitialized )return null
        val crashLogDir = File(mCrashLogDirPath)
        if(!crashLogDir.exists())return null
        return crashLogDir.listFiles()?.maxByOrNull { it.lastModified() }
    }

    fun getCrashLogFileList(): List<File>? {
        if(!this::mCrashLogDirPath.isInitialized )return null
        val crashLogDir = File(mCrashLogDirPath)
        if(!crashLogDir.exists())return emptyList()
        return crashLogDir.listFiles()?.toList()
    }

    interface UncaughtExceptionCallback{
        fun onUncaughtException(throwable:Throwable)
    }
}