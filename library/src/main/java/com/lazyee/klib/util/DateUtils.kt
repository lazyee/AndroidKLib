package com.lazyee.klib.util

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
}