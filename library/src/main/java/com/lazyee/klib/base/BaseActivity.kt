package com.lazyee.klib.base

import android.app.Activity
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

