package com.lazyee.klib.base

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.internal.ViewUtils.removeOnGlobalLayoutListener
import com.lazyee.klib.constant.AppConstants
import com.lazyee.klib.extension.addOnKeyBoardVisibleListener
import com.lazyee.klib.extension.removeKeyBoardVisibleListener
import com.lazyee.klib.http.HttpUtil
import com.lazyee.klib.listener.OnKeyboardVisibleListener
import com.lazyee.klib.mvvm.LoadingState
import com.lazyee.klib.mvvm.MVVMBaseView

/**
 * @Author leeorz
 * @Date 2020/11/2-3:29 PM
 * @Description: activity base 类
 */
open class  BaseActivity: AppCompatActivity(), MVVMBaseView {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        HttpUtil.cancel(this)
    }

    override fun onLoadingStateChanged(state: LoadingState) {

    }

    override fun onPageLoadingStateChanged(state: LoadingState) {

    }

    override fun onShowLongToast(msg: String) {

    }

    override fun onShowShortToast(msg: String) {

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

