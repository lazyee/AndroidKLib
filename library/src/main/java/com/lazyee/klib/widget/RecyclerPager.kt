package com.lazyee.klib.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

open class RecyclerPager : FrameLayout {
    constructor(context: Context):super(context,null)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr:Int):super(context,attrs,defStyleAttr)

    private lateinit var mRecyclerView: RecyclerView
    private val mPagerSnapHelper = PagerSnapHelper()
    private var mOnPositionChangedListener:OnPositionChangedListener? = null
    private var currentPosition = 0
    private var mAdapter: Adapter<*>? = null


    override fun onFinishInflate() {
        super.onFinishInflate()
        mRecyclerView = createRecyclerView()
        mRecyclerView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mRecyclerView.layoutManager = linearLayoutManager
        mPagerSnapHelper.attachToRecyclerView(mRecyclerView)
        addView(mRecyclerView)

        mRecyclerView.addOnScrollListener(mOnScrollListener)
    }

    open fun createRecyclerView(): RecyclerView {
        return RecyclerView(context)
    }

    private val mOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    val viewIdle = mPagerSnapHelper.findSnapView(mRecyclerView.layoutManager)
                    viewIdle?.run {
                        val position = mRecyclerView.layoutManager?.getPosition(viewIdle) ?: 0
                        callbackPositionChanged(position)
                    }
                }
            }
        }
    }

    private fun callbackPositionChanged(position: Int){
        if (position != currentPosition) {
            currentPosition = position
            mOnPositionChangedListener?.onPositionChanged(currentPosition)
        }
    }

    fun scrollToPosition(position: Int,isSmooth:Boolean = false){
        if(isSmooth){
            mRecyclerView.smoothScrollToPosition(position)
            return
        }
        mRecyclerView.scrollToPosition(position)
        mAdapter?.run {
            if(position >= itemCount -1)return@run
            if(position < 0)return@run
            callbackPositionChanged(position)
        }
    }

    fun setAdapter(adapter: Adapter<*>){
        mAdapter = adapter
        mRecyclerView.adapter = mAdapter
    }

    fun setOnPositionChangedListener(listener: OnPositionChangedListener){
        mOnPositionChangedListener = listener
    }

    interface OnPositionChangedListener{
        fun onPositionChanged(position:Int)
    }
}