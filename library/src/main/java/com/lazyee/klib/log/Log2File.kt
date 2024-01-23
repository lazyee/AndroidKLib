package com.lazyee.klib.log

import android.text.TextUtils
import android.util.Log
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
 */
class Log2File(private val isRecord:Boolean, private val logFileDirPath: String,private val logName:String = "log") {

    private val mExecutorService: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    private var logFile:File? = null

    init{
        mExecutorService.run { deleteTimeOutLogFile() }
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
        val log =  "$yyyyMMddHHmmss -> [${level.name}] $tag : ${msg}\n"
        getTargetLogFile(yyyyMMdd).appendText(log)
    }

    private fun log2File(log:String){
        if(logFileDirPath.isEmpty())return
        val currentTimeMillis = System.currentTimeMillis()
        val yyyyMMddHHmmss = DateUtils.format(currentTimeMillis, DateUtils.yyyyMMddHHmmss)
        val yyyyMMdd = yyyyMMddHHmmss.split(" ")[0]
        getTargetLogFile(yyyyMMdd).appendText(log)
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
}
