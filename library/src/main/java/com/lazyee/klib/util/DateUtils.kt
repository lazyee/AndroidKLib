package com.lazyee.klib.util

import android.text.TextUtils
import com.lazyee.klib.extension.toDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:日期工具类
 * Date: 2023/6/2 14:08
 */
object DateUtils {

    const val yyyyMMdd = "yyyy-MM-dd"
    const val yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"
    /**
     * 格式化时间戳
     */
    fun format(timeMillis:Long, format:String): String {
        return format(Date(timeMillis),format)
    }

    /**
     * 格式化时间戳
     */
    fun format(date:Date,format: String): String {
        val simpleFormatter = SimpleDateFormat(format, Locale.getDefault())
        return simpleFormatter.format(date)
    }

    /**
     * 将字符串转换为Date
     */
    fun stringToDate(string:String,format:String = yyyyMMddHHmmss,locale:Locale = Locale.CHINA): Date? {
        if(TextUtils.isEmpty(string))return null
        try {
            val dateFormat = SimpleDateFormat(format, locale)
            return dateFormat.parse(string)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return null
    }

    /**
     * 将字符串转换为时间戳
     */
    fun stringToTimeMillis(string:String, format:String = yyyyMMddHHmmss,locale:Locale = Locale.CHINA): Long? {
        if(TextUtils.isEmpty(string))return null
        return stringToDate(string,format,locale)?.time
    }
}