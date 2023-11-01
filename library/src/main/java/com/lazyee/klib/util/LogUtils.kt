package com.lazyee.klib.util

import android.text.TextUtils
import android.util.Log
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

    private var mExecutorService: ExecutorService = Executors.newSingleThreadExecutor()
    private var isDebug = true
    private var isRecord = false
    private var logFileDirPath :String = ""
    private var logFile:File? = null

    fun init(isDebug: Boolean){
        init(isDebug,false,"")
    }

    /**
     * logFileDirPath:xxxx/yyyy/zzzzz
     * logFile:{logFileDirPath}/logcat-yyyy-MM-dd.log
     */
    fun init(isDebug:Boolean,isRecord:Boolean = false,logFileDirPath:String = ""){
        this.isDebug = isDebug
        this.isRecord = isRecord
        this.logFileDirPath = logFileDirPath
        mExecutorService.run { deleteTimeOutLogFile() }
    }

    fun d(tag: String?, any: Any?) {
        log(Level.D, getTag(tag), getMsg(any))
    }

    fun e(tag: String?, any: Any?) {
        log(Level.E,getTag(tag), getMsg(any))
    }

    fun i(tag: String?, any: Any?) {
        log(Level.I,getTag(tag), getMsg(any))
    }

    fun w(tag: String?,any: Any?){
        log(Level.W,getTag(tag), getMsg(any))
    }

    fun v(tag: String?,any: Any?){
        log(Level.V,getTag(tag), getMsg(any))
    }

    private fun getTag(tag: String?):String{
        if (TextUtils.isEmpty(tag))return "[TAG]"
        return tag!!
    }

    private fun getMsg(any:Any?):String{
        if (any == null)return "[null]"
        if (any is String) return any
        return any.toString()
    }


    @Synchronized
    private fun getTargetLogFile(date:String): File {
        val targetLogFilePath = "${logFileDirPath}${File.separator}logcat-$date.log"
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

    private fun log(level:Level,tag: String,msg: String){
        if (isDebug) {
            when(level){
                Level.W-> Log.w(tag,msg)
                Level.I-> Log.i(tag,msg)
                Level.E-> Log.e(tag,msg)
                Level.V-> Log.v(tag,msg)
                Level.D-> Log.d(tag,msg)
            }
        }

        if(isRecord){
            mExecutorService.run { log2File(level,tag,msg) }
        }
    }

    private fun log2File(level:Level,tag:String,msg:String){
        if(!isRecord)return
        if(logFileDirPath.isEmpty())return
        val currentTimeMillis = System.currentTimeMillis()
        val yyyyMMddHHmmss = DateUtils.format(currentTimeMillis,DateUtils.yyyyMMddHHmmss)
        val yyyyMMdd = yyyyMMddHHmmss.split(" ")[0]
        val log =  "$yyyyMMddHHmmss -> [${level.name}] $tag : ${msg}\n"
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

    enum class Level(name:String){
        W("Warring"),
        I("Info"),
        E("Error"),
        V("Verbose"),
        D("Debug")
    }
}
