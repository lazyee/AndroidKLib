package com.lazyee.klib.util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.lazyee.klib.extension.safeToLong
import java.io.File

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2022/5/5 3:24 下午
 */
object AppUtils {

    /**
     * 比较版本名,如果当前版本名比待比较的版本名大，那么返回false
     * 这里的比较只能比较由数字组成的版本名，如果有类似1.1.1.beta之类的版本名就不要用这个了
     * @receiver String
     * @param currentVersionName String 1.1.1.1
     * @param compareVersionName String 1.1.1.2
     * @return Boolean
     */
    fun hasNewVersion(currentVersionName:String,compareVersionName: String):Boolean{
        try {
            val currentVersionNameArr = currentVersionName.split("\\.".toRegex()).toTypedArray()
            val compareVersionNameArr = compareVersionName.split("\\.".toRegex()).toTypedArray()
            val loopCount = compareVersionNameArr.size.coerceAtLeast(currentVersionNameArr.size)
            val currentVersionArr = arrayOfNulls<String>(loopCount)
            for (i in currentVersionNameArr.indices) {
                currentVersionArr[i] = currentVersionNameArr[i]
            }
            val compareVersionArr = arrayOfNulls<String>(loopCount)
            for (i in compareVersionNameArr.indices) {
                compareVersionArr[i] = compareVersionNameArr[i]
            }
            for (i in 0 until loopCount) {
                val currentVersion = currentVersionArr[i].safeToLong()
                val compareVersion = compareVersionArr[i].safeToLong()
                if (currentVersion != compareVersion) {
                    return currentVersion < compareVersion
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            return false
        }

        return false
    }


    /**
     * 获取当前进程名
     */
    fun getCurrentProcessName(applicationContext:Context): String? {
        val pid = android.os.Process.myPid()
        var processName:String? = ""
        val manager = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        manager.runningAppProcesses.forEach {process->
            if(process.pid == pid){
                processName = process.processName
                return processName
            }
        }
        return processName
    }

    /**
     * 分享文件
     *
     * 1.在AndroidManifest.xml中添加provider
     * <provider
     *    android:name="androidx.core.content.FileProvider"
     *    android:authorities="xxx.yyy.zzzz.provider.FileProvider"
     *    android:exported="false"
     *    android:grantUriPermissions="true">
     *    <meta-data
     *        android:name="android.support.FILE_PROVIDER_PATHS"
     *        android:resource="@xml/file_paths" />
     * </provider>
     *
     * 2.在res中创建xml文件夹，在xml文件夹中创建file_paths.xml
     * <?xml version="1.0" encoding="utf-8"?>
     * <paths xmlns:android="http://schemas.android.com/apk/res/android">
     *     <external-path name="external_files" path="." />
     *     <root-path name="root_path" path=""/>
     * </paths>
     */
    fun shareFile(context:Context,authority:String, file: File){
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "*/*"
        val fileUri = FileProvider.getUriForFile(context,authority,file)
        shareIntent.putExtra(Intent.EXTRA_STREAM,fileUri)
        context.startActivity(Intent.createChooser(shareIntent,"分享文件到"))
    }

    /**
     * 安装apk
     */
    fun installApk(launcher: ActivityResultLauncher<Intent>,activity: Activity,authority:String, apkFile: File){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val packageUri = Uri.parse("package:" + activity.packageName)
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri)
            launcher.launch(intent)
            return
        }
        realInstallApk(activity,authority,apkFile)
    }

    fun realInstallApk(activity: Activity,authority: String,apkFile: File){
        val intent = Intent(Intent.ACTION_VIEW)
        val apkUri:Uri = FileProvider.getUriForFile(activity, authority, apkFile)
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }
}