package com.lazyee.klib.extension

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.lazyee.klib.constant.AppConstants
import com.lazyee.klib.listener.OnKeyboardVisibleListener

/**
 * 打开一个Activity
 * @receiver Activity
 * @param clazz Class<out Activity>?
 * @param flag Int?
 * @param requestCode Int?
 */
fun Context.goto(
    clazz: Class<out Activity>? = null,
    action: String? = null,
    bundle: Bundle? = null,
    flag: Int? = null,
    requestCode: Int? = null
) {

    var intent: Intent? = null
    if (clazz != null) {
        intent = Intent(this, clazz)
    } else if (!TextUtils.isEmpty(action)) {
        intent = Intent(action)
    }

    intent ?: return

    if (flag != null) intent.flags = flag
    if (bundle != null) intent.putExtras(bundle)

    if (this is Activity) {
        if (requestCode == null) {
            startActivity(intent)
        } else {
            startActivityForResult(intent, requestCode)
        }

    } else if (this is Fragment) {
        if (requestCode == null) {
            startActivity(intent)
        } else {
            startActivityForResult(intent, requestCode)
        }
    } else if (this is android.app.Fragment) {
        if (requestCode == null) {
            startActivity(intent)
        } else {
            startActivityForResult(intent, requestCode)
        }
    } else {
        throw Exception("不支持的Context类型")
    }

}


/**
 * @Author leeorz
 * @Date 2020/11/2-6:23 PM
 * @Description:Context类的拓展方法
 */

fun Context.inflate(layoutId: Int): View {
    return LayoutInflater.from(this).inflate(layoutId, null)
}

/**
 * The absolute width of the available display size in pixels.
 */
val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

/**
 * The absolute height of the available display size in pixels.
 */
val Context.screenHeight: Int
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
fun Context.copy(content: String?): Boolean {
    if (TextUtils.isEmpty(content)) return false
    val manager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("label", content)
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
fun Context.ofColor(colorResId: Int): Int {
    return ContextCompat.getColor(this, colorResId)
}

/**
 * 获取Drawable
 * @receiver Context
 * @param resId Int
 * @return Drawable?
 */
fun Context.ofDrawable(resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
}

fun Context.toastShort(strResId: Int) {
    Toast.makeText(this, strResId, Toast.LENGTH_LONG).show()
}

fun Context.toastLong(strResId: Int) {
    Toast.makeText(this, strResId, Toast.LENGTH_SHORT).show()
}

fun Context.toastShort(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toastLong(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

/**
 * 设置键盘显示隐藏监听
 */
fun Context.addOnKeyBoardVisibleListener(listener: OnKeyboardVisibleListener?): ViewTreeObserver.OnGlobalLayoutListener? {
    if (this !is Activity) return null
    var decorViewVisibleHeight = 0
    val onGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val rect = Rect()
            window.decorView.getWindowVisibleDisplayFrame(rect)
            val visibleHeight = rect.height()

            if (decorViewVisibleHeight == 0) {
                decorViewVisibleHeight = visibleHeight
                return
            }

            if (decorViewVisibleHeight == visibleHeight) {
                return
            }

            val keyboardHeight = decorViewVisibleHeight - visibleHeight
            if (keyboardHeight > 200) {
                AppConstants.SOFT_KEYBOARD_HEIGHT = keyboardHeight
                listener?.onSoftKeyboardShow(keyboardHeight)
                decorViewVisibleHeight = visibleHeight
                return
            }

            if (visibleHeight - decorViewVisibleHeight > 200) {
                listener?.onSoftKeyboardHide()
                decorViewVisibleHeight = visibleHeight
            }
        }
    }
    window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    return onGlobalLayoutListener
}

/**
 * 移除键盘显示监听
 */
fun Context.removeKeyBoardVisibleListener(listener: ViewTreeObserver.OnGlobalLayoutListener){
    if(this !is Activity)return
    window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
}