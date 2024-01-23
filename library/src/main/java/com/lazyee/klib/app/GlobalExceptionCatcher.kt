package com.lazyee.klib.app

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import com.lazyee.klib.extension.screenHeight
import com.lazyee.klib.extension.screenWidth
import com.lazyee.klib.log.Log2File
import com.lazyee.klib.util.DateUtils
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

    private val crashLog2File by lazy { Log2File(true,mCrashLogDirPath,"crash") }

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
        val packageManager = mApplicationContext.packageManager
        val packageInfo  = packageManager.getPackageInfo(mApplicationContext.packageName, PackageManager.GET_ACTIVITIES)
        val sb = StringBuilder()
        sb.append("==============================================================================================\n")
        sb.append(" CRASH DATE            : ${DateUtils.format(System.currentTimeMillis(), DateUtils.yyyyMMddHHmmss)}\n")
        sb.append(" BRAND                 : ${Build.BRAND}\n")
        sb.append(" DEVICE                : ${Build.DEVICE}\n")
        sb.append(" OS VERSION            : ${Build.VERSION.RELEASE}\n")
        sb.append(" APP VERSION CODE      : ${packageInfo?.versionCode?:"null"}\n")
        sb.append(" APP VERSION NAME      : ${packageInfo?.versionName?:"null"}\n")
        sb.append(" SCREEN PIXEL          : ${mApplicationContext.screenWidth}x${mApplicationContext.screenHeight}\n")
        sb.append(" ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ CRASH STACK TRACE ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓\n")
        sb.append(throwable.stackTraceToString())
        sb.append("==============================================================================================\n")
        crashLog2File.log(sb)

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

    fun getCrashLogFileList(): Array<File>? {
        if(!this::mCrashLogDirPath.isInitialized )return null
        val crashLogDir = File(mCrashLogDirPath)
        if(!crashLogDir.exists())return emptyArray()
        return crashLogDir.listFiles()
    }

    interface UncaughtExceptionCallback{
        fun onUncaughtException(throwable:Throwable)
    }
}