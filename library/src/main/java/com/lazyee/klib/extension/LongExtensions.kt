package com.lazyee.klib.extension

import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author leeorz
 * @Date 11/24/20-9:37 AM
 * @Description:长整型拓展方法
 */

/**
 * 将长整型转换成为格式话的时间日期字符串
 */
fun Long?.toDateString(format:String = "yyyy-MM-dd hh:mm:ss",locale: Locale = Locale.CHINA): String {
    if (null == this)return ""
    val date = Date(this)
    return SimpleDateFormat(format,locale).format(date)
}

/**
 * 将长整型转换成Date
 */
fun Long?.toDate(): Date? {
    if (null == this)return null
    return Date(this)
}