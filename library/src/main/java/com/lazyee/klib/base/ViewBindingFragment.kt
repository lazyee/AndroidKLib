package com.lazyee.klib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lazyee.klib.util.ViewBindingUtils

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:ViewBinding Fragment
 * Date: 2022/3/21 4:05 下午
 */
open class ViewBindingFragment<VB:ViewBinding> :BaseFragment(){

    lateinit var mViewBinding:VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mViewBinding = ViewBindingUtils.getViewBinding(javaClass,inflater,container,false)
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    open fun initView(){

    }

    open fun initData(){

    }
}