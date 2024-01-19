package com.lazyee.klib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.lazyee.klib.typed.TCallback

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:便捷监听touch事件
 * Date: 2024/1/19 15:10
 */
class TouchCallbackLayout:FrameLayout{

    private var mTouchDownCallback:TCallback<MotionEvent>? = null
    private var mTouchUpCallback:TCallback<MotionEvent>? = null
    private var mTouchMoveCallback:TCallback<MotionEvent>? = null
    private var mTouchCancelCallback:TCallback<MotionEvent>? = null
    constructor(context: Context):this(context,null)
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr:Int):super(context,attrs,defStyleAttr)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.run {
            when(action){
                MotionEvent.ACTION_DOWN->onTouchDown(this)
                MotionEvent.ACTION_MOVE->onTouchMove(this)
                MotionEvent.ACTION_UP->onTouchUp(this)
                MotionEvent.ACTION_CANCEL->onTouchCancel(this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun addTouchDownCallback(callback:TCallback<MotionEvent>){
        mTouchDownCallback = callback
    }

    fun addTouchUpCallback(callback:TCallback<MotionEvent>){
        mTouchUpCallback = callback
    }

    fun addTouchMoveCallback(callback:TCallback<MotionEvent>){
        mTouchMoveCallback = callback
    }

    fun addTouchCancelCallback(callback:TCallback<MotionEvent>){
        mTouchCancelCallback = callback
    }

    private fun onTouchDown(ev: MotionEvent){
        mTouchDownCallback?.invoke(ev)
    }

    private fun onTouchMove(ev: MotionEvent){
        mTouchMoveCallback?.invoke(ev)
    }

    private fun onTouchCancel(ev: MotionEvent){
        mTouchCancelCallback?.invoke(ev)
    }

    private fun onTouchUp(ev: MotionEvent){
        mTouchUpCallback?.invoke(ev)
    }
}