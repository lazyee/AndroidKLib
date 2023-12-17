package com.lazyee.klib.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.lazyee.klib.util.ViewBindingUtils

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:ViewBinding Dialog
 * Date: 2023/5/23 16:23
 */
open class ViewBindingDialog<VB:ViewBinding> :Dialog{

    constructor(context: Context):this(context,0)
    constructor(context: Context,themeResId:Int):super(context,themeResId)
    constructor(context: Context,cancelable:Boolean,cancelListener: DialogInterface.OnCancelListener):super(context,cancelable,cancelListener)

    var mViewBinding: VB? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ViewBindingUtils.getViewBinding(javaClass, layoutInflater)
        mViewBinding?.run { setContentView(root) }
        initView()
    }

    open fun initView(){

    }
}