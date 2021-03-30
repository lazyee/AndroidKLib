@file:Suppress("BlockingMethodInNonBlockingContext")

package com.lazyee.klib.extension

import android.text.TextUtils
import android.util.Base64
import com.lazyee.klib.util.FileUtils
import com.lazyee.klib.util.LogUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.net.URL
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
 * 根据URL返回String内容
 * @receiver String
 * @param method String default GET
 * @param connectTimeout Int seconds default 30s
 * @param readTimeout Int seconds default 30s
 * @param block Function1<[@kotlin.ParameterName] String?, Unit>
 */
fun String.contentOfUrl(
    method: String = "GET",
    connectTimeout: Int = 30,
    readTimeout: Int = 30, block: (content: String?) -> Unit
) {

    if (!startsWith("https://", true)
            && !startsWith("http://", true)){
        block(null)
        return
    }
    GlobalScope.launch {
        val url = URL(this@contentOfUrl)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = connectTimeout * 1000
        conn.readTimeout = readTimeout * 1000
        conn.requestMethod = method
        if (conn.responseCode != 200) {
            conn.disconnect()
            runBlocking { block(null) }
        }

        try {
            block(FileUtils.contentOfInputStream(conn.inputStream))
        } catch (e: Exception) {
            e.printStackTrace()
            conn.disconnect()
            block(null)
        }
    }
}

/**
 * 从文件中获取正文
 * @receiver String
 * @return String?
 */
fun String.contentOfPath(): String? {
    return FileUtils.contentOfPath(this)
}

/**
 * String 写入文件
 * @receiver String
 * @param path String 文件路径
 * @return Boolean 写入是否成功，true则为成功
 */
fun String.contentWriteToFile(path: String): Boolean {
    return FileUtils.contentWriteToFile(this, path)
}

/**
 * 文件是否存在
 * @receiver String
 * @return Boolean
 */
fun String.fileIsExists(): Boolean {
    return FileUtils.fileIsExists(this)
}

/**
 * 删除文件
 * @receiver String
 */
fun String.deleteFile(): Boolean {
    return FileUtils.delete(this)
}

/**
 * 字符串生成MD5
 * @receiver String
 * @return String
 */
fun String.md5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.hex()
}

/**
 * 将字符串进行Base64编码
 * @receiver String
 * @return String
 */
fun String.encodeBase64():String{
    return Base64.encodeToString(toByteArray(), Base64.NO_WRAP)
}

/**
 * 将字符串进行Base64编码，需要编码的数据量大的时候使用这个方法
 * @receiver String
 * @param block Function1<[@kotlin.ParameterName] String, Unit>
 */
fun String.encodeBase64(block: (string: String) -> Unit){
    GlobalScope.launch {
        measureTimeMillis { block(encodeBase64()) }.also {
            LogUtils.i("StringExtensions", "encode to base64 spend time:$it")
        }

    }
}

/**
 * 将Base64编码的字符串进行解码
 * @receiver String
 * @return String
 */
fun String.decodeBase64():String{
    return Base64.decode(this, Base64.DEFAULT).toString(Charsets.UTF_8)
}

/**
 * 将Base64编码的字符串进行解码,需要解码的数据量大的时候使用这个方法
 * @receiver String
 * @return String
 */
fun String.decodeBase64(block: (string: String) -> Unit){
    GlobalScope.launch {
        measureTimeMillis { block(decodeBase64()) }.also {
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
 * 比较版本名,如果比传入的版本名比大，那么返回true
 * @receiver String
 * @param compareVersionName String [1.1.1.1]
 * @return Boolean
 */
fun String.compareVersionName(compareVersionName: String): Boolean {
    val currentVersionNameArr = this.split("\\.".toRegex()).toTypedArray()
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
            return currentVersion > compareVersion
        }
    }

    return false
}


/**
 * 安全的把字符串转换的Long ,非数字的时候转换为0
 * @receiver String?
 * @return Long
 */
fun String?.safeToLong(): Long {
    try {
        return if (TextUtils.isEmpty(this)) 0 else this!!.toLong()
    } catch (e: Exception) { }
    return 0
}

/**
 * 安全的把字符串转换的Int ,非数字的时候转换为0
 * @receiver String?
 * @return Int
 */
fun String?.safeToInt():Int{
    try {
        return if (TextUtils.isEmpty(this)) 0 else this!!.toInt()
    } catch (e: Exception) { }
    return 0
}

/**
 * 安全的把字符串转换的Double ,非数字的时候转换为0.0
 * @receiver String?
 * @return Double
 */
fun String?.safeToDouble():Double{
    try {
        return if (TextUtils.isEmpty(this)) 0.0 else this!!.toDouble()
    } catch (e: Exception) { }
    return 0.0
}

/**
 * 安全的把字符串转换的Float ,非数字的时候转换为0f
 * @receiver String?
 * @return Float
 */
fun String?.safeToFloat():Float{
    try {
        return if (TextUtils.isEmpty(this)) 0f else this!!.toFloat()
    } catch (e: Exception) { }
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
fun String?.toDate(format:String = "yyyy-MM-dd hh:mm:ss",locale:Locale = Locale.CHINA): Date? {
    if(TextUtils.isEmpty(this))return null
    val dateFormat = SimpleDateFormat(format, locale)
    return dateFormat.parse(this!!)
}

/**
 * 将字符串转换为时间戳
 */
fun String?.toTimeMillis(format:String = "yyyy-MM-dd hh:mm:ss",locale:Locale = Locale.CHINA): Long? {
    if(TextUtils.isEmpty(this))return null
    return toDate(format,locale)?.time
}