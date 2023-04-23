package com.lazyee.klib.extension

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.lazyee.klib.constant.AppConstants
import com.lazyee.klib.listener.OnKeyboardVisibleListener
import com.lazyee.klib.util.FileUtils

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
 * activity softInputMode 为 adjustNothing的时候无法监听
 */
fun Context.addOnKeyBoardVisibleListener(listener: OnKeyboardVisibleListener?): ViewTreeObserver.OnGlobalLayoutListener? {
    if (this !is Activity) return null

    fun getVisibleHeight(view:View):Int{
        val rect = Rect()
        view.getWindowVisibleDisplayFrame(rect)
        return rect.height()
    }
    val keyboardGlobalLayoutListener = createKeyboardGlobalLayoutListener(window.decorView,::getVisibleHeight,listener)
    window.decorView.viewTreeObserver.addOnGlobalLayoutListener(keyboardGlobalLayoutListener)
    return keyboardGlobalLayoutListener
}

/**
 * 设置键盘显示隐藏监听
 * activity softInputMode 为 adjustNothing的时候使用
 */
fun Context.addAdjustNothingModeOnKeyBoardVisibleListener(listener: OnKeyboardVisibleListener?): ViewTreeObserver.OnGlobalLayoutListener? {
    if (this !is AppCompatActivity) return null

    fun getVisibleHeight(view:View):Int{
        return view.measuredHeight
    }
    val popupView = LinearLayout(this)
    popupView.layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    val keyboardGlobalLayoutListener = createKeyboardGlobalLayoutListener(popupView, ::getVisibleHeight,listener)
    popupView.viewTreeObserver.addOnGlobalLayoutListener(keyboardGlobalLayoutListener)

    val popupWindow = PopupWindow(this)
    popupWindow.contentView = popupView

    popupWindow.softInputMode =
        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
    popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED

    popupWindow.width = 0
    popupWindow.height = ViewGroup.LayoutParams.MATCH_PARENT
    popupWindow.setBackgroundDrawable(ColorDrawable(0))

    window.decorView.run {
        post { popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, 0, 0) }
    }

    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            popupWindow.dismiss()
        }
    })

    return keyboardGlobalLayoutListener
}

private fun createKeyboardGlobalLayoutListener(view:View,getVisibleHeight:(view:View)->Int, listener: OnKeyboardVisibleListener?): ViewTreeObserver.OnGlobalLayoutListener {
    var decorViewVisibleHeight = 0
    return object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val visibleHeight = getVisibleHeight(view)

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
}

/**
 * 移除键盘显示监听
 */
fun Context.removeKeyBoardVisibleListener(listener: ViewTreeObserver.OnGlobalLayoutListener){
    if(this !is Activity)return
    window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
}


/**
 * 显示通知
 * 如果显示不出来，要去应用的通知管理中设置是否显示通知
 */
fun Context.showNotification(notificationId:Int,
                             buildNotification: (builder:Notification.Builder)->Unit,
                             channelId:String = "defaultChannelId",
                             channelName:String = "defaultChannelName"){
    val notificationBuilder: Notification.Builder
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(notificationChannel)
        notificationBuilder = Notification.Builder(this, channelId)
        notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE)
        notificationBuilder.setAutoCancel(true)
    } else {
        notificationBuilder = Notification.Builder(this)
    }

    buildNotification(notificationBuilder)
    notificationManager.notify(notificationId, notificationBuilder.build())
}

/**
 * 移除通知
 */
fun Context.cancelNotification(notificationId:Int){
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(notificationId)
}

/**
 * 获取内部存储空间大小
 */
fun Context.getInternalFsTotalSize(): Long {
    return FileUtils.getFsTotalSize(cacheDir.absolutePath)
}

/**
 * 获取内容存储空间可用大小
 */
fun Context.getInternalFsAvailableSize(): Long {
    return FileUtils.getFsAvailableSize(cacheDir.absolutePath)
}

/**
 * 获取外部存储空间大小
 */
fun Context.getExternalFsTotalSize(): Long {
    return FileUtils.getFsTotalSize(externalCacheDir?.absolutePath?:"")
}

/**
 * 获取外部存储空间可用大小
 */
fun Context.getExternalFsAvailableSize(): Long {
    return FileUtils.getFsAvailableSize(externalCacheDir?.absolutePath?:"")
}

/**
 * 添加网络状态回调
 * @param lifecycle
 * @param callback
 */
fun Context.registerNetworkStateCallback(lifecycle:Lifecycle, callback:(isAvailable:Boolean)->Unit){
    val broadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            callback.invoke(isNetworkAvailable())
        }
    }
    val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    registerReceiver(broadcastReceiver,intentFilter)

    lifecycle.addObserver(object :LifecycleObserver{
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy(){
            unregisterReceiver(broadcastReceiver)
        }
    })
}

/**
 * 判断网络是否连接
 * @param * @return
 */
fun Context.isNetworkAvailable(): Boolean {
    val mConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val mNetworkInfo = mConnectivityManager.activeNetworkInfo
    return mNetworkInfo?.isAvailable?:false
}

