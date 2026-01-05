package com.lazyee.klib.util

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.lazyee.klib.R
import com.lazyee.klib.extension.safeToLong
import com.lazyee.klib.typed.TCallback
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
        context.startActivity(Intent.createChooser(shareIntent,context.getString(R.string.str_share_file_to)))
    }

    /**
     * 安装apk
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
    fun installApk(activity: Activity,authority:String, apkFile: File){
        if(activity !is FragmentActivity) throw Exception("必须要FragmentActivity才能执行此方法")
        interInstallApk(activity = activity,authority = authority,apkFile = apkFile)
    }

    /**
     * 安装apk
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
    fun installApk(fragment: Fragment,authority: String,apkFile: File){
        interInstallApk(fragment = fragment,authority = authority,apkFile = apkFile)
    }

    private fun interInstallApk(activity: FragmentActivity? = null,fragment: Fragment? = null,authority: String,apkFile: File){

        fun realInstallApk(context: Context,authority: String,apkFile: File){
            val intent = Intent(Intent.ACTION_VIEW)
            val apkUri:Uri = FileProvider.getUriForFile(context, authority, apkFile)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        var fragmentManager: FragmentManager? = null
        var context:Context? = null
        if(activity != null){
            context = activity
            fragmentManager = activity.supportFragmentManager
        }else if(fragment != null){
            context = fragment.context
            fragmentManager = fragment.childFragmentManager

        }
        context?:return
        fragmentManager?:return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val packageManager = context.packageManager
            val canRequestPackageInstalls = packageManager.canRequestPackageInstalls()
            if(canRequestPackageInstalls){
                realInstallApk(context,authority,apkFile)
                return
            }

            val packageUri = Uri.parse("package:" + context.packageName)
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri)
            registerSimpleActivityResult(fragmentManager,intent){ result->
                if(result.resultCode != Activity.RESULT_OK)return@registerSimpleActivityResult
                realInstallApk(context,authority,apkFile)
            }
            return
        }
        realInstallApk(context,authority,apkFile)

    }


    /**
     * 注册简单的registerForActivityResult
     */
    fun registerSimpleActivityResult(activity: FragmentActivity, intent: Intent,callback: TCallback<ActivityResult>){
        registerSimpleActivityResult(activity.supportFragmentManager,intent,callback)
    }

    /**
     * 注册简单的registerForActivityResult
     */
    fun registerSimpleActivityResult(fragment:Fragment,intent: Intent,callback: TCallback<ActivityResult>){
        registerSimpleActivityResult(fragment.childFragmentManager,intent,callback)
    }

    private fun registerSimpleActivityResult(fragmentManager: FragmentManager,intent: Intent,callback: TCallback<ActivityResult>){
        val tag = "ActivityResultFragment"
        var targetFragment:Fragment? = fragmentManager.findFragmentByTag(tag)
        val transaction = fragmentManager.beginTransaction()
        targetFragment?.run {
            transaction.remove(this)
        }
        targetFragment = ActivityResultFragment.newInstance(intent,callback)
        transaction.add(targetFragment,tag)
        transaction.commitAllowingStateLoss()
    }
    /**
     * 必须设置为公开的class
     */
    class ActivityResultFragment:Fragment(){
        private var intent:Intent? = null
        private var callback:TCallback<ActivityResult>? = null
        companion object{
            fun newInstance(intent: Intent,callback: TCallback<ActivityResult>): ActivityResultFragment{

                val fragment = ActivityResultFragment()
                fragment.intent = intent
                fragment.callback = callback
                return fragment
            }
        }

        private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            parentFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
            callback?.invoke(result)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            intent?.run {
                activityResultLauncher.launch(intent)
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            super.onCreateView(inflater, container, savedInstanceState)
            return inflater.inflate(R.layout.layout_debug_config,null,false)
        }
    }

    /**
     * 判断微信是否安装
     */
    fun isWechatInstalled(context: Context): Boolean {
        return isTargetAppInstalled(context,"com.tencent.mm")
    }

    /**
     * 判断QQ是否安装
     */
    fun isQQInstalled(context: Context): Boolean {
        return isTargetAppInstalled(context,"com.tencent.mobileqq")
    }

    private fun isTargetAppInstalled(context: Context, packageName: String): Boolean {
        val packageManager = context.packageManager
        val intent = Intent()
        intent.setPackage(packageName)
        val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.isNotEmpty()
    }
}