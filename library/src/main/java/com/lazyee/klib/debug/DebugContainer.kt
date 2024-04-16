package com.lazyee.klib.debug

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lazyee.klib.common.SP
import com.lazyee.klib.databinding.ItemDebugConfigBinding
import com.lazyee.klib.databinding.WidgetDebugViewBinding
import com.lazyee.klib.extension.gone
import com.lazyee.klib.extension.setSize
import com.lazyee.klib.extension.visible

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:debug View
 * Date: 2022/5/16 11:28 上午
 */

class DebugContainer : FrameLayout {

    private val sp by lazy { SP(context,"__config__", AppCompatActivity.MODE_PRIVATE) }
    private val mViewBinding by lazy {
        WidgetDebugViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    }
    private val mBottomSheetBehavior by lazy { BottomSheetBehavior.from(mViewBinding.llDebugContent) }
    private var mSelectedDebugConfig: DebugConfig? = null

    private val mAdapter: DebugConfigAdapter = DebugConfigAdapter()
    private var mDebugViewCallback: Callback? = null
    private var mItemViewResId:Int = -1

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mViewBinding.run {
            tvClose.setOnClickListener { hide() }
            coordinatorLayout.bringToFront()
        }

        hide()
        mBottomSheetBehavior.addBottomSheetCallback(mBottomSheetCallback)
    }

    private val mBottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            mDebugViewCallback?:return
            mSelectedDebugConfig?:return
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                sp.put(mDebugViewCallback!!.provideDebugConfigKey(),mSelectedDebugConfig!!.getKey())
                mDebugViewCallback!!.onSelectedDebugConfig(mSelectedDebugConfig!!)
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {

        }
    }

    fun setItemViewResId(resId:Int): DebugContainer {
        mItemViewResId = resId
        return this
    }

    fun setDebugViewCallback(callback: Callback): DebugContainer {
        mDebugViewCallback = callback
        mSelectedDebugConfig = callback.provideDebugConfig().find { it.getKey() == sp.string(mDebugViewCallback!!.provideDebugConfigKey())}
        mSelectedDebugConfig?.run { mDebugViewCallback!!.onSelectedDebugConfig(this) }
        mViewBinding.recyclerView.adapter = mAdapter
        return this
    }

    fun setTitle(title:String):DebugContainer{
        mViewBinding.tvTitle.text = title
        return this
    }

    fun show() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun hide() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private inner class DebugConfigAdapter : RecyclerView.Adapter<DebugConfigViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebugConfigViewHolder {
            val binding = ItemDebugConfigBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val itemView =  LayoutInflater.from(parent.context).inflate(mItemViewResId,null)
//            itemView.setSize(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
            binding.flContent.addView(itemView)
            return DebugConfigViewHolder(binding)
        }

        override fun onBindViewHolder(holder: DebugConfigViewHolder, position: Int) {
            holder.bindData(mDebugViewCallback!!.provideDebugConfig()[position])
        }

        override fun getItemCount(): Int {
            return mDebugViewCallback!!.provideDebugConfig().size
        }

    }

    private inner class DebugConfigViewHolder(val mViewBinding: ItemDebugConfigBinding) :
        RecyclerView.ViewHolder(mViewBinding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bindData(config: DebugConfig) {

            mViewBinding.run {
                cardView.setOnClickListener {
                    mSelectedDebugConfig = config
                    mAdapter.notifyDataSetChanged()
                    hide()
                }
                mDebugViewCallback?.onBindItemView(root,config)
                if (config == mSelectedDebugConfig) ivSelected.visible() else ivSelected.gone()
            }
        }
    }

    interface Callback {
        fun provideDebugConfig():List<DebugConfig>
        fun provideDebugConfigKey():String
        fun onSelectedDebugConfig(selectedDebugConfig: DebugConfig)
        fun onBindItemView(itemView: View,item:DebugConfig)
    }
}

