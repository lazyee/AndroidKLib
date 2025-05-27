package com.lazyee.klib.base

import android.graphics.Rect
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.lazyee.klib.util.ViewBindingUtils
import kotlin.reflect.jvm.internal.impl.load.java.structure.JavaClass

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

    /**
     * 这里不主动发起调用initData，由开发者自己选择调用时机，这里起到的作用仅仅是固定初始化数据的方法名
     */
    open fun initData(){

    }

}