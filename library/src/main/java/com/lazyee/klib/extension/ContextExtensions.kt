package com.lazyee.klib.extension
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.core.content.ContextCompat

/**
 * @Author leeorz
 * @Date 2020/11/2-6:23 PM
 * @Description:Context类的拓展方法
 */

/**
 * The absolute width of the available display size in pixels.
 */
val Context.screenWidth :Int
    get() = resources.displayMetrics.widthPixels

/**
 * The absolute height of the available display size in pixels.
 */
val Context.screenHeight :Int
    get() = resources.displayMetrics.heightPixels

/**
 * dp转px
 * @receiver Context
 * @param dp Float
 * @return Int
 */
fun Context.dp2px(dp: Float): Int {
    val density: Float = resources.displayMetrics.density
    return (dp * density + 0.5f).toInt()
}

/**
 * px转dp
 * @receiver Context
 * @param px Float
 * @return Int
 */
fun Context.px2dp(px: Float): Int {
    val density = resources.displayMetrics.density
    return (px / density + 0.5f).toInt()
}

/**
 * px转sp
 * @receiver Context
 * @param px Float
 * @return Int
 */
fun Context.px2sp(px: Float): Int {
    val scaledDensity = resources.displayMetrics.scaledDensity
    return (px / scaledDensity + 0.5f).toInt()
}

/**
 * sp转px
 * @receiver Context
 * @param sp Float
 * @return Int
 */
fun Context.sp2px(sp: Float): Int {
    val scaledDensity = resources.displayMetrics.scaledDensity
    return (sp * scaledDensity + 0.5f).toInt()
}

/**
 * 复制文本
 */
fun Context.copy(content:String?):Boolean{
    if(TextUtils.isEmpty(content))return false
    val manager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("label",content)
    manager.setPrimaryClip(clipData)
    return true
}

/**
 * 粘贴
 */
fun Context.paste(): CharSequence? {
    val manager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData? = manager.primaryClip
    return if (clip != null && clip.itemCount > 0) {
        return clip.getItemAt(0).coerceToText(this)
    } else null
}

/**
 * 是否横屏
 * @return true 横屏
 */
fun Context.isLandscape(): Boolean {
    val mConfiguration = resources.configuration //获取设置的配置信息
    return mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

/**
 * 是否竖屏
 * @return true 竖屏
 */
fun Context.isPortrait(): Boolean {
    val mConfiguration = resources.configuration //获取设置的配置信息
    return mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT
}

/**
 * 获取颜色
 * @receiver Context
 * @param colorResId Int
 * @return Int
 */
fun Context.ofColor(colorResId:Int): Int {
    return ContextCompat.getColor(this,colorResId)
}

/**
 * 获取Drawable
 * @receiver Context
 * @param resId Int
 * @return Drawable?
 */
fun Context.ofDrawable(resId:Int): Drawable? {
    return ContextCompat.getDrawable(this,resId)
}
