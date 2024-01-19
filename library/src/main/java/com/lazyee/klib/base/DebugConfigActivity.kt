package com.lazyee.klib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ContentFrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewbinding.ViewBinding
import com.lazyee.klib.BuildConfig
import com.lazyee.klib.common.SP
import com.lazyee.klib.databinding.LayoutDebugConfigBinding
import com.lazyee.klib.debug.DebugView
import com.lazyee.klib.debug.IDebugConfig

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description: 根据debug状态是否显示选择环境组件,使用的时候需要在特定的Activity继承DebugConfigActivity
 * Date: 2022/5/16 11:54 上午
 */
private const val kSelectedDebugConfigName = "selected_debug_config_name"
abstract class  DebugConfigActivity<VB: ViewBinding> : ViewBindingActivity<VB>(),DebugView.Callback {


    private lateinit var mSelectedDebugConfig:IDebugConfig
    private val sp by lazy { SP(this,"debug-config", MODE_PRIVATE) }
    private val debugView by lazy { DebugView(this@DebugConfigActivity) }
    private lateinit var mContentView:View

    override fun initView() {
        super.initView()
        if(BuildConfig.DEBUG){
            if(mViewBinding.root.parent is ContentFrameLayout){
                val contentFrameLayout = (mViewBinding.root.parent as ContentFrameLayout)
                contentFrameLayout.addView(debugView)
                setSelectedDebugConfig(getDebugOptionList())
                debugView.setDebugConfigList(getDebugOptionList(),mSelectedDebugConfig)
                debugView.setDebugViewCallback(this@DebugConfigActivity)
                if(isDefaultDisplayDebugConfigSelector()){
                    debugView.show()
                }
            }
            return
        }
    }

    private fun setSelectedDebugConfig(debugConfigList:MutableList<IDebugConfig>) {
        mSelectedDebugConfig =
            debugConfigList.find { it.getConfigName() == sp.string(kSelectedDebugConfigName) }
                ?: debugConfigList.first()
    }

    abstract fun getDebugOptionList():MutableList<IDebugConfig>

    abstract fun onSelectedDebugConfig(debugConfig: IDebugConfig)

    open fun isDefaultDisplayDebugConfigSelector(): Boolean {
        return false
    }

    override fun onDebugViewHide(selectedDebugConfig: IDebugConfig) {
        mSelectedDebugConfig = selectedDebugConfig
        sp.put(kSelectedDebugConfigName,selectedDebugConfig.getConfigName())
        onSelectedDebugConfig(selectedDebugConfig)
    }

}