package com.lazyee.klib.listener

import android.view.ViewTreeObserver

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:键盘显示监听
 * Date: 2022/5/13 2:58 下午
 */
interface OnKeyboardVisibleListener {
    fun onSoftKeyboardShow(keyboardHeight:Int)
    fun onSoftKeyboardHide()
}