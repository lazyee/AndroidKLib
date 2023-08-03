package com.lazyee.klib.base

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.lazyee.klib.extension.addOnKeyBoardVisibleListener
import com.lazyee.klib.extension.removeKeyBoardVisibleListener
import com.lazyee.klib.extension.toastLong
import com.lazyee.klib.extension.toastShort
import com.lazyee.klib.http.ApiManager
import com.lazyee.klib.listener.OnKeyboardVisibleListener
import com.lazyee.klib.mvvm.LoadingState
import com.lazyee.klib.mvvm.MVVMBaseView
import com.lazyee.klib.util.AppUtils

/**
 * @Author leeorz
 * @Date 2020/11/2-3:29 PM
 * @Description: activity base 类
 */
open class  BaseActivity: AppCompatActivity(), MVVMBaseView {
    companion object{
        var isFollowSystemFontScale = true
    }

    val TAG :String by lazy { this::class.java.simpleName }
    private var mDecorViewVisibleHeight = 0

    val activity: Activity
        get() = this

    /**
     * 键盘显示监听
     */
    private var mOnKeyboardVisibleListener:OnKeyboardVisibleListener? = null
    /**
     * 键盘显示隐藏引发的测量监听
     */
    private var mOnKeyboardVisibleChangeGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? =
        null

    override fun attachBaseContext(newBase: Context?) {
        if(isFollowSystemFontScale){
            super.attachBaseContext(newBase)
        }else{
            super.attachBaseContext(getConfigurationContext(newBase))
        }
    }

    private fun getConfigurationContext(context: Context?): Context? {
        context?:return null
        val configuration: Configuration = context.resources.configuration
        configuration.fontScale = 1f
        return context.createConfigurationContext(configuration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ApiManager.cancel(this)
    }

    override fun onLoadingStateChanged(state: LoadingState) {

    }

    override fun onPageLoadingStateChanged(state: LoadingState) {

    }

    override fun onShowLongToast(msg: String?) {
        if(TextUtils.isEmpty(msg))return
        toastLong(msg!!)
    }

    override fun onShowShortToast(msg: String?) {
        if(TextUtils.isEmpty(msg))return
        toastShort(msg!!)
    }

    /**
     * 设置键盘显示监听
     */
    fun setOnKeyboardVisibleListener(listener: OnKeyboardVisibleListener?){
        mOnKeyboardVisibleListener = listener
        mOnKeyboardVisibleChangeGlobalLayoutListener?.run { removeKeyBoardVisibleListener(this) }
        mOnKeyboardVisibleChangeGlobalLayoutListener = addOnKeyBoardVisibleListener(mOnKeyboardVisibleListener)
    }
}

