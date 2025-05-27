package com.lazyee.klib.base

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lazyee.klib.annotation.observeLiveEvent
import com.lazyee.klib.extension.addAdjustNothingModeOnKeyBoardVisibleListener
import com.lazyee.klib.extension.addOnKeyBoardVisibleListener
import com.lazyee.klib.extension.removeKeyBoardVisibleListener
import com.lazyee.klib.extension.toastLong
import com.lazyee.klib.extension.toastShort
import com.lazyee.klib.http.ApiManager
import com.lazyee.klib.listener.OnKeyboardVisibleListener
import com.lazyee.klib.mvvm.LoadingState
import com.lazyee.klib.mvvm.MVVMBaseView

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


    fun <T : ViewModel> getViewModel(modelClass: Class<T> ): T {
        return ViewModelProvider(this).get(modelClass)
    }

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
        observeLiveEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        ApiManager.cancel(this)
        removeOnKeyboardVisibleListener()
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

    override fun onShowLongToast(resId: Int) {
        toastLong(resId)
    }

    override fun onShowShortToast(resId: Int) {
        toastShort(resId)
    }

    /**
     * 添加键盘显示监听
     */
    fun addOnKeyboardVisibleListener(listener: OnKeyboardVisibleListener?){
        mOnKeyboardVisibleListener = listener
        mOnKeyboardVisibleChangeGlobalLayoutListener?.run { removeKeyBoardVisibleListener(this) }
        if(window.attributes.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING){
            mOnKeyboardVisibleChangeGlobalLayoutListener = addAdjustNothingModeOnKeyBoardVisibleListener(mOnKeyboardVisibleListener)
        }else{
            mOnKeyboardVisibleChangeGlobalLayoutListener = addOnKeyBoardVisibleListener(mOnKeyboardVisibleListener)
        }
    }

    /**
     * 移除键盘显示监听
     */
    fun removeOnKeyboardVisibleListener(){
        mOnKeyboardVisibleListener = null
        mOnKeyboardVisibleChangeGlobalLayoutListener?.run {
            removeKeyBoardVisibleListener(this)
        }
        mOnKeyboardVisibleChangeGlobalLayoutListener = null
    }

}

