package com.lazyee.klib.manager

import android.app.Activity
import android.content.Context
import android.os.Build
import com.lazyee.klib.typed.VoidCallback
import java.util.Locale

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:地区管理器，主要用来修改语言
 * Date: 2023/12/18 17:30
 */
class LocaleManager {
    private val mContext: Context
    constructor(context: Context){
        this.mContext = context
    }

    fun changeLocale(locale: Locale,callback:VoidCallback? = null){
        val resources = mContext.resources
        val configuration = resources.configuration

        configuration.setLocale(locale)

        val dm = resources.displayMetrics
        resources.updateConfiguration(configuration,dm)

        /**
         * 一般来说需要跳转到第一个activity
         */
        callback?.invoke()
    }

    /**
     * 是否是简体中文
     */
    fun isSimplifiedChinese(): Boolean {
        val resources = mContext.resources
        val configuration = resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales.get(0) == Locale.SIMPLIFIED_CHINESE
        }else{
            configuration.locale == Locale.SIMPLIFIED_CHINESE
        }
    }

    /**
     * 是否是繁体中文
     */
    fun isTraditionalChinese(): Boolean {
        val resources = mContext.resources
        val configuration = resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales.get(0) == Locale.TRADITIONAL_CHINESE
        }else{
            configuration.locale == Locale.TRADITIONAL_CHINESE
        }
    }

    /**
     * 是否英语
     */
    fun isEnglish(): Boolean {
        val resources = mContext.resources
        val configuration = resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales.get(0) == Locale.ENGLISH
        }else{
            configuration.locale == Locale.ENGLISH
        }
    }
}