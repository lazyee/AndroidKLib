package com.lazyee.klib.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.lazyee.klib.common.SP
import java.util.Locale

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:地区管理器，主要用来修改语言
 * Date: 2023/12/18 17:30
 */
val kCurrentLanguage = "current_language"

object LanguageUtils {
//    private val mContext: Context
//    constructor(context: Context){
//        this.mContext = context
//    }
//
//    fun changeLocale(locale: Locale,callback: TCallback<Locale>? = null){
//        Locale.setDefault(locale)
//
//        val config = Configuration(mContext.resources.configuration)
//        config.setLocale(locale)
//        config.setLayoutDirection(locale)
//
//        mContext.createConfigurationContext(config)
//
//        /**
//         * 一般来说需要跳转到第一个activity
//         */
//        callback?.invoke(locale)
//    }
    private fun getSP(context: Context): SP {
        return SP(context,"LanguageConfig")
    }

    fun getConfigLocale(context: Context?): Locale {
        context?:return Locale.getDefault()
        val language = getSP(context).string(kCurrentLanguage)
        if(language.isNullOrEmpty()) return Locale.getDefault()
        return Locale(language)
    }

    fun saveConfigLocale(context: Context,locale: Locale){
        getSP(context).put(kCurrentLanguage,locale.language)
    }


    /**
     *  在Application和Activity中调用这个方法即可完成语言切换的持久化
     *     override fun attachBaseContext(base: Context?) {
     *         val context = LanguageUtils.attachBaseContext(base,LanguageUtils.getConfigLocale(base))
     *         super.attachBaseContext(context)
     *     }
     */
    fun attachBaseContext(context: Context?, locale: Locale): Context? {
        context?:return context
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }

    /**
     * 是否是简体中文
     */
    fun isSimplifiedChinese(context: Context): Boolean {
        val resources = context.resources
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
    fun isTraditionalChinese(context: Context): Boolean {
        val resources = context.resources
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
    fun isEnglish(context: Context): Boolean {
        val resources = context.resources
        val configuration = resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales.get(0) == Locale.ENGLISH
        }else{
            configuration.locale == Locale.ENGLISH
        }
    }
}