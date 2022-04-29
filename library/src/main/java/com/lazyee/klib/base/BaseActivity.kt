package com.lazyee.klib.base

import android.graphics.Rect
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import com.lazyee.klib.constant.AppConstants
import com.lazyee.klib.http.HttpUtil
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

    private fun createKeyboardVisibleChangeGlobalLayoutListener(): ViewTreeObserver.OnGlobalLayoutListener {
        return object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val rect = Rect()
                window.decorView.getWindowVisibleDisplayFrame(rect)
                val visibleHeight = rect.height()

                if (mDecorViewVisibleHeight == 0) {
                    mDecorViewVisibleHeight = visibleHeight
                    return
                }

                if (mDecorViewVisibleHeight == visibleHeight) {
                    return
                }

                val keyboardHeight = mDecorViewVisibleHeight - visibleHeight
                if (keyboardHeight > 200) {
                    AppConstants.SOFT_KEYBOARD_HEIGHT = keyboardHeight
                    mOnKeyboardVisibleListener?.onSoftKeyboardShow(keyboardHeight)
                    mDecorViewVisibleHeight = visibleHeight
                    return
                }

                if (visibleHeight - mDecorViewVisibleHeight > 200) {
                    mOnKeyboardVisibleListener?.onSoftKeyboardHide()
                    mDecorViewVisibleHeight = visibleHeight
                }
            }
        }
    }

    /**
     * 设置键盘显示监听
     */
    fun setOnKeyboardVisibleListener(listener: OnKeyboardVisibleListener?){
        mOnKeyboardVisibleListener = listener
        window.decorView.viewTreeObserver.run {
            mOnKeyboardVisibleChangeGlobalLayoutListener?.run { removeOnGlobalLayoutListener(this) }
            mOnKeyboardVisibleListener?.run {
                createKeyboardVisibleChangeGlobalLayoutListener().run {
                    mOnKeyboardVisibleChangeGlobalLayoutListener = this
                    addOnGlobalLayoutListener(this)
                }
            }
        }
    }
}

interface OnKeyboardVisibleListener{
    fun onSoftKeyboardShow(keyboardHeight:Int)
    fun onSoftKeyboardHide()
}