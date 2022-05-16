package com.lazyee.klib.debug

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lazyee.klib.common.SP
import com.lazyee.klib.databinding.ItemDebugConfigBinding
import com.lazyee.klib.databinding.WidgetDebugViewBinding
import com.lazyee.klib.extension.dpToPx
import com.lazyee.klib.extension.gone
import com.lazyee.klib.extension.visible

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:debug View
 * Date: 2022/5/16 11:28 上午
 */

class DebugView : LinearLayout {

    private val mViewBinding by lazy {
        WidgetDebugViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }
    private val mBottomSheetBehavior by lazy { BottomSheetBehavior.from(mViewBinding.llDebugContent) }
    private val mDatas = mutableListOf<IDebugConfig>()
    private lateinit var mSelectedDebugConfig: IDebugConfig

    private val mAdapter: DebugConfigAdapter = DebugConfigAdapter()
    private var mDebugViewCallback: Callback? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mViewBinding.run {
            recyclerView.adapter = mAdapter
            tvClose.setOnClickListener { hide() }
        }

        mBottomSheetBehavior.addBottomSheetCallback(mBottomSheetCallback)
    }

    private val mBottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                mDebugViewCallback?.onDebugViewHide(mSelectedDebugConfig)
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {

        }
    }

    fun setDebugViewCallback(callback: Callback) {
        mDebugViewCallback = callback
    }

    fun show() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun hide() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDebugConfigList(data: MutableList<IDebugConfig>, selectedDebugConfig: IDebugConfig) {
        mSelectedDebugConfig = selectedDebugConfig
        mDatas.clear()
        mDatas.addAll(data)
        mAdapter.notifyDataSetChanged()
    }

    private inner class DebugConfigAdapter : RecyclerView.Adapter<DebugConfigViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebugConfigViewHolder {
            return DebugConfigViewHolder(
                ItemDebugConfigBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: DebugConfigViewHolder, position: Int) {
            holder.bindData(mDatas[position])
        }

        override fun getItemCount(): Int {
            return mDatas.size
        }

    }

    private inner class DebugConfigViewHolder(val mViewBinding: ItemDebugConfigBinding) :
        RecyclerView.ViewHolder(mViewBinding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bindData(config: IDebugConfig) {

            mViewBinding.run {
                cardView.setOnClickListener {
                    mSelectedDebugConfig = config
                    mAdapter.notifyDataSetChanged()
                    hide()
                }
                tvBaseUrl.text = "BaseUrl:${config.getBaseUrl()}"
                tvConfigName.text = config.getConfigName()
                if (config == mSelectedDebugConfig) ivSelected.visible() else ivSelected.gone()
            }
        }
    }

    interface Callback {
        fun onDebugViewHide(selectedDebugConfig: IDebugConfig)
    }
}

