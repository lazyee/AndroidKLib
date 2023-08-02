@file:Suppress("BlockingMethodInNonBlockingContext")

package com.lazyee.klib.extension

import android.text.TextUtils
import android.util.Base64
import com.lazyee.klib.util.DateUtils
import com.lazyee.klib.util.LogUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.system.measureTimeMillis

/**
 * @Author leeorz
 * @Date 2020/11/2-10:31 PM
 * @Description:String 的拓展方法
 */


/**
 * 字符串生成MD5
 * @receiver String
 * @return String
 */
fun String.md5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.joinToString("") { "%02X".format(it) }
}

/**
 * 将字符串进行Base64编码
 * @receiver String
 * @receiver flag int
 * @return String
 */
fun String.encodeBase64(flags:Int = Base64.DEFAULT):String{
    return Base64.encodeToString(toByteArray(), flags)
}



/**
 * 将字符串进行Base64编码，需要编码的数据量大的时候使用这个方法
 * @receiver flag int
 * @receiver String
 * @param block Function1<[@kotlin.ParameterName] String, Unit>
 */
fun String.encodeBase64(flags:Int = Base64.DEFAULT,block: (string: String) -> Unit){
    GlobalScope.launch {
        measureTimeMillis { block(encodeBase64(flags)) }.also {
            LogUtils.i("StringExtensions", "encode to base64 spend time:$it")
        }

    }
}

/**
 * 将Base64编码的字符串进行解码
 * @receiver String
 * @receiver flag int
 * @return String
 */
fun String.decodeBase64(flags:Int = Base64.DEFAULT):String{
    return Base64.decode(this, flags).toString(Charsets.UTF_8)
}

/**
 * 将Base64编码的字符串进行解码,需要解码的数据量大的时候使用这个方法
 * @receiver flag int
 * @receiver String
 * @return String
 */
fun String.decodeBase64(flags:Int = Base64.DEFAULT,block: (string: String) -> Unit){
    GlobalScope.launch {
        measureTimeMillis { block(decodeBase64(flags)) }.also {
            LogUtils.i("StringExtensions", "decode base64 string spend time:$it")
        }
    }
}

/**
 * 进行URL编码
 * @receiver String
 * @return String
 */
fun String.encodeURL():String{
    return URLEncoder.encode(this, Charsets.UTF_8.displayName())
}

/**
 * 进行URL解码
 * @receiver String
 * @return String
 */
fun String.decodeURL():String{
    return URLDecoder.decode(this, Charsets.UTF_8.displayName())
}




/**
 * 安全的把字符串转换的Long ,非数字的时候转换为0
 * @receiver String?
 * @return Long
 */
fun String?.safeToLong(): Long {
    this?:return 0
    try {
        return if (TextUtils.isEmpty(this)) 0 else this.toLong()
    } catch (e: Exception) { }
    return 0
}

/**
 * 安全的把字符串转换的Int ,非数字的时候转换为0
 * @receiver String?
 * @return Int
 */
fun String?.safeToInt():Int{
    this?:return 0
    try {
        return if (TextUtils.isEmpty(this)) 0 else this.toInt()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}

/**
 * 安全的把字符串转换的Double ,非数字的时候转换为0.0
 * @receiver String?
 * @return Double
 */
fun String?.safeToDouble():Double{
    this?:return 0.0
    try {
        return if (TextUtils.isEmpty(this)) 0.0 else this.toDouble()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0.0
}

/**
 * 安全的把字符串转换的Float ,非数字的时候转换为0f
 * @receiver String?
 * @return Float
 */
fun String?.safeToFloat():Float{
    this?:return 0f
    try {
        return if (TextUtils.isEmpty(this)) 0f else this.toFloat()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0f
}

/**
 * 隐藏11位手机号码159****0000
 * @receiver String?
 * @return String?
 */
fun String?.hidePhone(): String? {
    return this?.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
}

/**
 * 大陆号码或香港号码均可
 */
fun String?.isPhoneLegal(): Boolean {
    return isChinaPhoneLegal() || isHKPhoneLegal()
}

/**
 * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
 * 此方法中前三位格式有：
 * 13+任意数
 * 15+除4的任意数
 * 18+除1和4的任意数
 * 17+除9的任意数
 * 147
 */
fun String?.isChinaPhoneLegal(): Boolean {
//    val regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$"
    val regExp = "^(1[3-9])\\d{9}$"
    val p: Pattern = Pattern.compile(regExp)
    val m: Matcher = p.matcher(this!!)
    return m.matches()
}

/**
 * 香港手机号码8位数，5|6|8|9开头+7位任意数
 */

fun String?.isHKPhoneLegal(): Boolean {
    val regExp = "^(5|6|8|9)\\d{7}$"
    val p: Pattern = Pattern.compile(regExp)
    val m: Matcher = p.matcher(this!!)
    return m.matches()
}

/**
 * 将字符串转换为Date
 */
fun String?.toDate(format:String = DateUtils.yyyyMMddHHmmss,locale:Locale = Locale.CHINA): Date? {
    if(TextUtils.isEmpty(this))return null
    return DateUtils.stringToDate(this!!,format,locale)
}

/**
 * 将字符串转换为时间戳
 */
fun String?.toTimeMillis(format:String = DateUtils.yyyyMMddHHmmss,locale:Locale = Locale.CHINA): Long? {
    if(TextUtils.isEmpty(this))return null
    return DateUtils.stringToTimeMillis(this!!,format,locale)
}

/**
 * 分割词组
 * 你好AAABCD生成如下数组
 * [你好AAABCD, 你好AAABC, 好AAABCD, 你好AAAB, 好AAABC, AAABCD, 你好AAA, 好AAAB, AAABC, AABCD, 你好AA, 好AAA, AAAB, AABC, ABCD, 你好A, 好AA, AAA, AAB, ABC, BCD, 你好, 好A, AA, AB, BC, CD, 你, 好, A, B, C, D]
 */
fun String.split2Combo():List<String>{
    val comboList = mutableListOf<String>()
    repeat(length){
        for (index in (length) downTo  it){
            if(index == it)continue
            val combo = substring(it,index)
            if(comboList.contains(combo))continue
            val lastCombo =  comboList.lastOrNull { it.length >= combo.length }
            comboList.add( comboList.indexOf(lastCombo) + 1,combo)
        }
    }
    return comboList
}

//fun main() {
//    "你好AAABCD".split2Combo()
//}