package com.lazyee.klib.extension

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.TypedValue
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
import com.lazyee.klib.typed.TCallback
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
 * 跳转到系统消息弹窗设置页面
 */
fun Context.gotoSystemNotificationSetting(){
    val intent = Intent()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
        intent.putExtra("app_package", packageName)
        intent.putExtra("app_uid", applicationInfo.uid)
    } else {
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:$packageName")
    }
    startActivity(intent)
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
 * 测量文本宽高
 */
fun Context.measureText(measureText:String, textSize:Float,typeface: Typeface? = null): FloatArray {
    val paint = Paint()
    if(typeface != null){
        paint.typeface = typeface
    }
    paint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,textSize,resources.displayMetrics)
    val width = paint.measureText(measureText)
    val height = paint.fontMetrics.bottom - paint.fontMetrics.top
    return floatArrayOf(width,height)
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
    return window.addOnKeyBoardVisibleListener(listener)
}

/**
 * 设置键盘显示隐藏监听
 * window softInputMode 为 adjustNothing的时候无法监听
 */
fun Window.addOnKeyBoardVisibleListener(listener:OnKeyboardVisibleListener?):ViewTreeObserver.OnGlobalLayoutListener?{
    fun getVisibleHeight(view:View):Int{
        val rect = Rect()
        view.getWindowVisibleDisplayFrame(rect)
        return rect.height()
    }
    val keyboardGlobalLayoutListener = createKeyboardGlobalLayoutListener(decorView,::getVisibleHeight,listener)
    decorView.viewTreeObserver.addOnGlobalLayoutListener(keyboardGlobalLayoutListener)
    return keyboardGlobalLayoutListener
}

/**
 * 设置键盘显示隐藏监听
 * activity softInputMode 为 adjustNothing的时候使用
 */
fun Context.addAdjustNothingModeOnKeyBoardVisibleListener(listener: OnKeyboardVisibleListener?): ViewTreeObserver.OnGlobalLayoutListener? {
    if (this !is Activity) return null

    fun getVisibleHeight(view:View):Int{
        return view.measuredHeight
    }

    val popupWindow = createListenKeyboardVisiblePopupWindow(this)
    val keyboardGlobalLayoutListener = createKeyboardGlobalLayoutListener(popupWindow.contentView, ::getVisibleHeight,listener)
    popupWindow.contentView.viewTreeObserver.addOnGlobalLayoutListener(keyboardGlobalLayoutListener)

    window.decorView.run {
        post { popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, 0, 0) }
    }

    if (this is AppCompatActivity) {
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                popupWindow.dismiss()
            }
        })
    }

    return keyboardGlobalLayoutListener
}

/**
 * 设置键盘显示隐藏监听
 * window softInputMode 为 adjustNothing的时候使用
 * Dialog在使用此方法的时候高度必须设置为全屏
 */
fun Window.addAdjustNothingModeOnKeyBoardVisibleListener(listener: OnKeyboardVisibleListener?): ViewTreeObserver.OnGlobalLayoutListener? {

    if(attributes.height != WindowManager.LayoutParams.MATCH_PARENT && attributes.height != decorView.context.screenHeight){
        throw Exception("window height must be full the screen!!!")
    }
    fun getVisibleHeight(view: View): Int {
        return view.measuredHeight
    }
    val popupWindow = createListenKeyboardVisiblePopupWindow(decorView.context)
    val keyboardGlobalLayoutListener = createKeyboardGlobalLayoutListener(popupWindow.contentView, ::getVisibleHeight, listener)
    popupWindow.contentView.viewTreeObserver.addOnGlobalLayoutListener(keyboardGlobalLayoutListener)

    decorView.run {
        post { popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, 0, 0) }
    }
    return keyboardGlobalLayoutListener
}

private fun createListenKeyboardVisiblePopupWindow(context:Context): PopupWindow {
    val popupView = LinearLayout(context)
    popupView.layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    popupView.setBackgroundColor(Color.parseColor("#FF0000"))

    val popupWindow = PopupWindow(context)
    popupWindow.contentView = popupView

    popupWindow.softInputMode =
        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
    popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED

    popupWindow.width = 0
//    popupWindow.width = ViewGroup.LayoutParams.MATCH_PARENT
    popupWindow.height = ViewGroup.LayoutParams.MATCH_PARENT
    popupWindow.setBackgroundDrawable(ColorDrawable(0))
    return popupWindow
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
 * 移除键盘显示监听
 */
fun Window.removeKeyBoardVisibleListener(listener: ViewTreeObserver.OnGlobalLayoutListener){
    decorView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
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
 * 添加网络状态变更回调
 * @param lifecycle
 * @param callback
 */
fun Context.registerNetworkStateChangedCallback(lifecycle:Lifecycle, callback:TCallback<Boolean>){
    var isNetworkAvailable = isNetworkAvailable()
    val broadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val currentNetworkAvailable = isNetworkAvailable()
            if(currentNetworkAvailable != isNetworkAvailable){
                isNetworkAvailable = currentNetworkAvailable
                callback.invoke(isNetworkAvailable)
            }
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
 * 添加网络状态变更回调,需要手动移除广播监听
 * @param callback
 */
fun Context.registerNetworkStateChangedCallback(callback:TCallback<Boolean>): BroadcastReceiver {
    var isNetworkAvailable = isNetworkAvailable()
    val broadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val currentNetworkAvailable = isNetworkAvailable()
            if(currentNetworkAvailable != isNetworkAvailable){
                isNetworkAvailable = currentNetworkAvailable
                callback.invoke(isNetworkAvailable)
            }
        }
    }

    val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    registerReceiver(broadcastReceiver,intentFilter)
    return broadcastReceiver
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

