package com.lazyee.klib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.lazyee.klib.BuildConfig
import com.lazyee.klib.common.SP
import com.lazyee.klib.databinding.LayoutDebugConfigBinding
import com.lazyee.klib.debug.DebugView
import com.lazyee.klib.debug.IDebugConfig

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description: 根据debug状态是否显示选择环境组件
 * Date: 2022/5/16 11:54 上午
 */
private const val kSelectedDebugConfigName = "selected_debug_config_name"
abstract class  DebugConfigActivity : BaseActivity(),DebugView.Callback {


    private lateinit var mSelectedDebugConfig:IDebugConfig
    private val sp by lazy { SP(this,"debug-config", MODE_PRIVATE) }
    private val mViewBinding by lazy { LayoutDebugConfigBinding.inflate(layoutInflater) }
    private val debugView by lazy { DebugView(this@DebugConfigActivity) }
    private lateinit var mContentView:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        mViewBinding.run {
            if((getLayoutId() != -1 && getContentView() != null) && (getLayoutId() == -1 && getContentView() == null)){
                throw Exception("The methods getLayoutId() and getContentView() must implement only one of them")
            }

            if(getLayoutId() != -1){
                mContentView = LayoutInflater.from(this@DebugConfigActivity).inflate(getLayoutId(),null)
                frameLayout.addView(mContentView)
            }

            if(getContentView() != null){
                mContentView = getContentView()!!
                frameLayout.addView(mContentView)
            }

            if(BuildConfig.DEBUG){
                frameLayout.addView(debugView)
                setSelectedDebugConfig(getDebugOptionList())
                debugView.setDebugConfigList(getDebugOptionList(),mSelectedDebugConfig)
                debugView.setDebugViewCallback(this@DebugConfigActivity)
                if(isDefaultDisplayDebugConfigSelector()){
                    debugView.show()
                }
                return
            }
            initView()
        }
    }

    private fun setSelectedDebugConfig(debugConfigList:MutableList<IDebugConfig>) {
        mSelectedDebugConfig =
            debugConfigList.find { it.getConfigName() == sp.string(kSelectedDebugConfigName) }
                ?: debugConfigList.first()
    }

    abstract fun initView()

    open fun getLayoutId():Int{
        return - 1
    }

    open fun getContentView(): View?{
        return null
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
        initView()
    }

}