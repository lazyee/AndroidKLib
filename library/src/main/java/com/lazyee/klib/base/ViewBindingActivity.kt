package com.lazyee.klib.base

import android.graphics.Rect
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.viewbinding.ViewBinding
import com.lazyee.klib.util.ViewBindingUtils

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:ViewBinding Activity
 * Date: 2022/3/21 4:05 下午
 */
open  class ViewBindingActivity<VB: ViewBinding>:BaseActivity() {
    lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ViewBindingUtils.getViewBinding(javaClass, layoutInflater)
        setContentView(mViewBinding.root)
        initView()
    }

    open fun initView() {

    }

}