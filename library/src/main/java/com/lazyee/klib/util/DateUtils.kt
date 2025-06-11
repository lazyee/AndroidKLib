package com.lazyee.klib.util

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.Calendar
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

    /**
     * 聊天时间提示信息
     * 1、当天的消息，以每5分钟为一个跨度的显示时间；
     * 2、消息超过1天、小于1周，显示星期+收发消息的时间；
     * 3、消息大于1周，显示手机收发时间的日期。
     */
    fun getChatTimeTip(msgTimeMillis: Long, prevMsgTimeMillis: Long): String {

        val msgCalendar = Calendar.getInstance().apply { timeInMillis = msgTimeMillis }
        val currentCalendar = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }

        // 同一天内，判断是否超过5分钟
        if ((msgTimeMillis - prevMsgTimeMillis) > 5 * 60 * 1000L) {
            val pattern:String
            if(isSameDay(msgCalendar,currentCalendar)){
                pattern = "HH:mm"
            }else if(isSameWeek(msgCalendar,currentCalendar)){
                pattern = "EEEE HH:mm" //EEEE: 星期几
            }else if(isSameMonth(msgCalendar,currentCalendar)){
                pattern = "M月d日 HH:mm"
            }else{
                pattern = "yyyy年M月d日 HH:mm"
           }
            return SimpleDateFormat(pattern, Locale.getDefault ()).format(Date(msgTimeMillis))
        }

        return ""
    }

    /**
     * 判断两个时间是否是同一天
     */
    fun isSameDay(timeMillis1: Long, timeMillis2: Long): Boolean {
        val c1 = Calendar.getInstance().apply { timeInMillis = timeMillis1 }
        val c2 = Calendar.getInstance().apply { timeInMillis = timeMillis2 }
        return isSameDay(c1, c2)
    }
    /**
     * 判断两个时间是否是同一天
     */
    fun isSameDay(c1: Calendar, c2: Calendar): Boolean {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     * 判断两个时间是否是同一年
     */
    fun isSameYear(timeMillis1: Long, timeMillis2: Long): Boolean {
        val c1 = Calendar.getInstance().apply { timeInMillis = timeMillis1 }
        val c2 = Calendar.getInstance().apply { timeInMillis = timeMillis2 }
        return isSameYear(c1,c2)
    }
    /**
     * 判断两个时间是否是同一年
     */
    fun isSameYear(c1: Calendar, c2: Calendar): Boolean {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
    }

    /**
     * 判断两个时间是否是同一个月
     */
    fun isSameMonth(timeMillis1: Long, timeMillis2: Long): Boolean{
        val c1 = Calendar.getInstance().apply { timeInMillis = timeMillis1 }
        val c2 = Calendar.getInstance().apply { timeInMillis = timeMillis2 }
        return isSameMonth(c1,c2)
    }
    /**
     * 判断两个时间是否是同一个月
     */
    fun isSameMonth(c1: Calendar, c2: Calendar): Boolean {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
    }

    /**
     * 判断两个时间是否是同一个星期
     */
    fun isSameWeek(timeMillis1: Long, timeMillis2: Long): Boolean{
        val c1 = Calendar.getInstance().apply { timeInMillis = timeMillis1 }
        val c2 = Calendar.getInstance().apply { timeInMillis = timeMillis2 }
        return isSameWeek(c1,c2)
    }
    /**
     * 判断两个时间是否是同一个星期
     */
    fun isSameWeek(c1: Calendar, c2: Calendar): Boolean {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR)
    }
}