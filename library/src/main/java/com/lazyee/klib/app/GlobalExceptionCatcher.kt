package com.lazyee.klib.app

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import com.lazyee.klib.extension.screenHeight
import com.lazyee.klib.extension.screenWidth
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
    private lateinit var mExceptionLogDir:File
    private var errorLogFile:File? = null
    private lateinit var mApplicationContext:Context
    private var mUncaughtExceptionCallback:UncaughtExceptionCallback? = null

    fun init(applicationContext: Context) {
        init(applicationContext,null)
    }

    fun init(applicationContext: Context,callback: UncaughtExceptionCallback?){
        mUncaughtExceptionCallback = callback
        mApplicationContext = applicationContext
        mExceptionLogDir = File(applicationContext.filesDir.absolutePath + File.separator + "error")
        if(!mExceptionLogDir.exists()){
            mExceptionLogDir.mkdirs()
        }
        deleteTimeOutExceptionLogFile()
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler)
    }

    /**
     * 删除超过十天的日志文件
     */
    private fun deleteTimeOutExceptionLogFile(){
        if(!mExceptionLogDir.exists())return
        val currentTimeMillis = System.currentTimeMillis()
        mExceptionLogDir.listFiles()?.forEach {file->
            if(currentTimeMillis - file.lastModified() > 10 * 24 * 60 * 60 * 1000){
                file.delete()
            }
        }
    }

    fun exitProcess(){
        Process.killProcess(Process.myPid())
        exitProcess(1)
    }

    private val mUncaughtExceptionHandler:Thread.UncaughtExceptionHandler = Thread.UncaughtExceptionHandler { thread, throwable ->
        val currentTimeMillis = System.currentTimeMillis()
        val yyyyMMddHHmmss = DateUtils.format(currentTimeMillis, DateUtils.yyyyMMddHHmmss)
        val yyyyMMdd = yyyyMMddHHmmss.split(" ")[0]
        val errorLogFile = getTargetErrorLogFile(yyyyMMdd)
        val packageManager = mApplicationContext.packageManager
        val packageInfo  = packageManager.getPackageInfo(mApplicationContext.packageName, PackageManager.GET_ACTIVITIES)
        errorLogFile.appendText("==============================================================================================\n")
        errorLogFile.appendText(" CRASH DATE         : $yyyyMMddHHmmss\n")
        errorLogFile.appendText(" BRAND              : ${Build.BRAND}\n")
        errorLogFile.appendText(" DEVICE             : ${Build.DEVICE}\n")
        errorLogFile.appendText(" OS VERSION         : ${Build.VERSION.RELEASE}\n")
        errorLogFile.appendText(" APP VERSION CODE   : ${packageInfo?.versionCode?:"null"}\n")
        errorLogFile.appendText(" APP VERSION NAME   : ${packageInfo?.versionName?:"null"}\n")
        errorLogFile.appendText(" SCREEN PIXEL       : ${mApplicationContext.screenWidth}x${mApplicationContext.screenHeight}\n")
        errorLogFile.appendText("----------------------------------------------------------------------------------------------\n")
        errorLogFile.appendText(throwable.stackTraceToString())

        mUncaughtExceptionCallback?.onUncaughtException(throwable)
    }

    private fun getTargetErrorLogFile(date:String): File {
        val targetLogFilePath = "${mExceptionLogDir.absolutePath}${File.separator}error-$date.log"
        if(errorLogFile != null && errorLogFile!!.absolutePath == targetLogFilePath){
            return errorLogFile!!
        }

        errorLogFile = File(targetLogFilePath)
        if(!errorLogFile!!.exists()){
            errorLogFile!!.createNewFile()
        }
        return errorLogFile!!
    }

    fun clearErrorLog(): Boolean {
        if(!this::mExceptionLogDir.isInitialized )return false
        mExceptionLogDir.listFiles()?.forEach {
            it.delete()
        }
        return true
    }

    fun getLastErrorLog():File?{
        if(!this::mExceptionLogDir.isInitialized )return null
        return mExceptionLogDir.listFiles()?.maxByOrNull { it.lastModified() }
    }

    fun getErrorLogFileList(): Array<File>? {
        if(!this::mExceptionLogDir.isInitialized )return null
        return mExceptionLogDir.listFiles()
    }

    interface UncaughtExceptionCallback{
        fun onUncaughtException(throwable:Throwable)
    }
}