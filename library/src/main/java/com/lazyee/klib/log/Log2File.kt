package com.lazyee.klib.log

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.lazyee.klib.extension.screenHeight
import com.lazyee.klib.extension.screenWidth
import com.lazyee.klib.util.DateUtils
import com.lazyee.klib.util.LogUtils
import java.io.File
import java.lang.StringBuilder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @Author leeorz
 * @Date 2020/11/2-6:54 PM
 * @Description:日志工具类
 *      logFileDirPath:xxxx/yyyy/zzzzz
 *      logFile:{logFileDirPath}/logcat-yyyy-MM-dd.log
 *      context:必须使用applicationContext
 */
class Log2File(private val context: Context, private val isRecord:Boolean, private val logFileDirPath: String, private val logName:String = "log") {

    private val mExecutorService: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    private var logFile:File ? = null

    init{
        mExecutorService.run { deleteTimeOutLogFile() }
    }

    /**
     * 文件内容是否为空
     */
    fun isBlank():Boolean{
        if(logFile == null) return true
        if(!logFile!!.exists()) return true
        if(logFile!!.length() == 0L) return true
        return false
    }
    
    fun log(log:String){
        log2File(log)
    }

    fun log(sb:StringBuilder){
        log(sb.toString())
    }

    fun log(sb:StringBuffer){
        log(sb.toString())
    }

    fun d(tag: String?, any: Any?) {
        log(LogLevel.D, LogUtils.getTag(tag), LogUtils.getMsg(any))
    }

    fun e(tag: String?, any: Any?) {
        log(LogLevel.E,LogUtils.getTag(tag), LogUtils.getMsg(any))
    }

    fun i(tag: String?, any: Any?) {
        log(LogLevel.I,LogUtils.getTag(tag), LogUtils.getMsg(any))
    }

    fun w(tag: String?,any: Any?){
        log(LogLevel.W,LogUtils.getTag(tag), LogUtils.getMsg(any))
    }

    fun v(tag: String?,any: Any?){
        log(LogLevel.V,LogUtils.getTag(tag), LogUtils.getMsg(any))
    }



    @Synchronized
    private fun getTargetLogFile(date:String): File {
        val targetLogFilePath = "${logFileDirPath}${File.separator}${logName}-$date.log"
        if(logFile != null && logFile!!.absolutePath == targetLogFilePath){
            return logFile!!
        }

        val logDir = File(logFileDirPath)
        if(!logDir.exists()){
            logDir.mkdirs()
        }
        logFile = File(targetLogFilePath)
        if(!logFile!!.exists()){
            logFile!!.createNewFile()
        }
        return logFile!!
    }

    private fun log(level: LogLevel, tag: String, msg: String){
        LogUtils.log(level,tag,msg)
        if(!isRecord) return
        mExecutorService.run { log2File(level,tag,msg) }
    }

    private fun log2File(level: LogLevel, tag:String, msg:String){
        if(logFileDirPath.isEmpty())return
        val currentTimeMillis = System.currentTimeMillis()
        val yyyyMMddHHmmss = DateUtils.format(currentTimeMillis, DateUtils.yyyyMMddHHmmss)
        val yyyyMMdd = yyyyMMddHHmmss.split(" ")[0]
        val sb = StringBuilder()
        sb.append("┌─\n")
        sb.append(" DATE:$yyyyMMddHHmmss (${level.name}) TAG:$tag\n")
        sb.append(" $msg\n")
        sb.append("└─\n")
        val file = getTargetLogFile(yyyyMMdd)
        writeDeviceInfo2File(file)
        file.appendText(sb.toString())
    }

    private fun log2File(log:String){
        if(logFileDirPath.isEmpty())return
        val currentTimeMillis = System.currentTimeMillis()
        val yyyyMMddHHmmss = DateUtils.format(currentTimeMillis, DateUtils.yyyyMMddHHmmss)
        val yyyyMMdd = yyyyMMddHHmmss.split(" ")[0]
        val sb = StringBuilder()
        sb.append("┌─\n")
        sb.append(" DATE:$yyyyMMddHHmmss\n")
        sb.append(" $log\n")
        sb.append("└─\n")
        val file = getTargetLogFile(yyyyMMdd)
        writeDeviceInfo2File(file)
        file.appendText(sb.toString())
    }

    private fun writeDeviceInfo2File(file:File){
        if(file.length() == 0L){
            file.appendText(getDeviceInfo())
        }
    }


    /**
     * 删除超过十天的日志文件
     */
    private fun deleteTimeOutLogFile(){
        val logDir = File(logFileDirPath)
        if(!logDir.exists())return
        val currentTimeMillis = System.currentTimeMillis()
        logDir.listFiles()?.forEach {file->
            if(currentTimeMillis - file.lastModified() > 10 * 24 * 60 * 60 * 1000){
                file.delete()
            }
        }
    }

    private fun getDeviceInfo():String{
        val packageManager = context.packageManager
        val packageInfo = packageManager.getPackageInfo(context.packageName, PackageManager.GET_ACTIVITIES)
        val lineWidth = 50 // 包含两边边框 ║

        fun line(content: String): String {
            val padded = content.padEnd(lineWidth - 2, ' ')
            return "│$padded│\n"
        }

        val sb = StringBuilder()
        sb.append("┌" + "─".repeat(lineWidth - 2) + "┐\n")
        sb.append(line(" DATE                  : ${DateUtils.format(System.currentTimeMillis(), DateUtils.yyyyMMdd)}"))
        sb.append(line(" BRAND                 : ${Build.BRAND}"))
        sb.append(line(" MODEL                 : ${Build.MODEL}"))
        sb.append(line(" DEVICE                : ${Build.DEVICE}"))
        sb.append(line(" OS VERSION            : ${Build.VERSION.RELEASE}"))
        sb.append(line(" SDK VERSION           : ${Build.VERSION.SDK_INT}"))
        sb.append(line(" APP VERSION CODE      : ${packageInfo?.versionCode ?: "null"}"))
        sb.append(line(" APP VERSION NAME      : ${packageInfo?.versionName ?: "null"}"))
        sb.append(line(" SCREEN PIXEL          : ${screenWidth}x${screenHeight}"))
        sb.append("└" + "─".repeat(lineWidth - 2) + "┘\n")
        return sb.toString()
    }
}
