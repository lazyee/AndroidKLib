package com.lazyee.klib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.lazyee.klib.util.ViewBindingUtils

/**
 * ClassName: ViewBindingDialogFragment
 * Description:ViewBinding DialogFragment
 * Date: 2025/4/19 22:45
 * @author Leeorz
 */
open class ViewBindingDialogFragment<VB : ViewBinding> : DialogFragment() {
    lateinit var mViewBinding:VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
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