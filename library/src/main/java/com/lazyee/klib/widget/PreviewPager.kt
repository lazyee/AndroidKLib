package com.lazyee.klib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

open class PreviewPager :RecyclerPager  {

    constructor(context: Context):super(context,null)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr:Int):super(context,attrs,defStyleAttr)

    override fun createRecyclerView(): RecyclerView {
        return PreviewRecyclerView(context)
    }

    private inner class PreviewRecyclerView(context: Context) :RecyclerView(context){
        private var isLock = false
        override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
            when(e?.actionMasked){
                MotionEvent.ACTION_POINTER_DOWN -> isLock = true
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> isLock = false
            }
            if(isLock){
                return false
            }
            return super.onInterceptTouchEvent(e)
        }

        override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
            when(ev?.actionMasked){
                MotionEvent.ACTION_POINTER_DOWN -> isLock = true
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> isLock = false
            }
            return super.dispatchTouchEvent(ev)
        }
    }
}