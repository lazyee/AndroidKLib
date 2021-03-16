package com.lazyee.klib.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import com.lazyee.klib.BuildConfig

/**
 * @Author leeorz
 * @Date 2020/11/5-10:59 AM
 * @Description:app工具类
 */

object AppUtils {

    /**
     * 创建跳转的Intent
     * @param context Context
     * @param clazz Class<out Activity>?
     * @param action String?
     * @param data Uri?
     * @param setIntentExtraBlock Function1<Intent, Unit>?
     * @param setBundleBlock Function1<Bundle, Bundle>?
     * @param flag Int?
     * @return Intent?
     */
    fun createIntent(
            context: Context,
            clazz: Class<out Activity>? = null,
            action: String? = null,
            data: Uri? = null,
            setIntentExtraBlock: ((Intent) -> Unit)? = null,
            setBundleBlock: ((Bundle) -> Unit)? = null,
            flag: Int? = null
    ): Intent? {
        var intent: Intent? = null

        if (clazz != null) {
            intent = Intent(context, clazz)
        } else {
            action?.run { intent = Intent(action) }
        }

        intent ?: return intent

        setIntentExtraBlock?.invoke(intent!!)
        setBundleBlock?.run {
            val bundle = Bundle()
            invoke(bundle)
            intent!!.putExtras(bundle)
        }
        data?.run { intent!!.data = data }
        flag?.run { intent!!.flags = flag }


        return intent
    }

    /**
     * 横屏
     *
     * @param context
     * @return
     */
    fun isLandscape(context: Context): Boolean {
        val mConfiguration = context.resources.configuration //获取设置的配置信息
        //获取屏幕方向
        return mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * 竖屏
     *
     * @param context
     * @return
     */
    fun isPortrait(context: Context): Boolean {
        val mConfiguration = context.resources.configuration //获取设置的配置信息
        //获取屏幕方向
        return mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * 判断是否是主线程
     * @return Boolean
     */
    fun isOnMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }

    /**
     * 判断是否是非主线程
     * @return Boolean
     */
    fun isOnBackgroundThread(): Boolean {
        return !isOnMainThread()
    }

    /**
     * 获取uuid
     */
    private var uuid: String? = null
    fun getUUID(): String {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(debugDeviceID)) {
            return debugDeviceID
        }
        if (uuid == null) {
            try {
                //一共13位  如果位数不够可以继续添加其他信息
                uuid = "35" +
                        Build.BOARD.length % 10 +
                        Build.BRAND.length % 10 +
                        Build.CPU_ABI.length % 10 +
                        Build.DEVICE.length % 10 +
                        Build.DISPLAY.length % 10 +
                        Build.HOST.length % 10 +
                        Build.ID.length % 10 +
                        Build.MANUFACTURER.length % 10 +
                        Build.MODEL.length % 10 +
                        Build.PRODUCT.length % 10 +
                        Build.TAGS.length % 10 +
                        Build.TYPE.length % 10 +
                        Build.USER.length % 10
            } catch (e: Exception) {
                uuid = ""
            }
        }
        return uuid!!
    }

    private var debugDeviceID = ""

    /**
     * 设置测试用的uuid
     */
    fun setDebugDeviceID(deviceID: String) {
        if (BuildConfig.DEBUG) {
            debugDeviceID = deviceID
        }
    }

    /**
     * 从AndroidManifest中获取meta data
     * @param context Context
     * @param key String
     * @return String?
     */
    fun getMetaDataFromManifest(context: Context, key: String): String? {
        try {
            val pm = context.packageManager
            val appInfo = pm.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val channel = appInfo.metaData.getString(key)
            if (!TextUtils.isEmpty(channel)) {
                return channel
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }
}