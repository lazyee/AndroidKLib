package com.lazyee.klib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.lazyee.klib.R
import com.lazyee.klib.extension.gone
import com.lazyee.klib.extension.visible

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:页面切换loading,network error，exception,content界面
 * Date: 2023/6/10 09:54
 */
open class PageStateSwitcher:FrameLayout {

    companion object{
        var provider: LayoutProvider? = null
        fun config(provider: LayoutProvider?){
            this.provider = provider
        }
    }

    private var contentViewLayoutId:Int? = null
    private var loadViewLayoutId:Int? = null
    private var networkErrorViewLayoutId:Int? = null
    private var exceptionViewLayoutId:Int? = null
    private var emptyViewLayoutId:Int? = null

    private var contentView: View? = null
    private var networkErrorView: View? = null
    private var loadingView: View? = null
    private var exceptionView: View? = null
    private var emptyView:View? = null

    constructor(context: Context):super(context,null)
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context,attrs: AttributeSet?,defStyleAttr:Int):super(context,attrs,defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        //通过xml配置
        contentView = findViewById(R.id.contentView)
        networkErrorView = findViewById(R.id.networkErrorView)
        loadingView = findViewById(R.id.loadingView)
        emptyView = findViewById(R.id.emptyView)
        exceptionView = findViewById(R.id.exceptionView)

        //通过代码配置
        if(contentView == null){
            contentView = inflateLayoutByLayoutId(contentViewLayoutId)
        }

        if(loadingView == null){
            loadingView = inflateLayoutByLayoutId(loadViewLayoutId)
        }

        if(networkErrorView == null){
            networkErrorView = inflateLayoutByLayoutId(networkErrorViewLayoutId)
        }

        if(exceptionView == null){
            exceptionView = inflateLayoutByLayoutId(exceptionViewLayoutId)
        }

        if(emptyView == null){
            emptyView = inflateLayoutByLayoutId(emptyViewLayoutId)
        }

        //通过全局配置初始化view
        if(loadingView == null){
            loadingView = inflateLayoutByLayoutId(provider?.getLoadingViewLayoutId())
        }

        if(networkErrorView == null){
            networkErrorView = inflateLayoutByLayoutId(provider?.getNetworkErrorViewLayoutId())
        }

        if(exceptionView == null){
            exceptionView = inflateLayoutByLayoutId(provider?.getExceptionViewLayoutId())
        }

        if(emptyView == null){
            emptyView = inflateLayoutByLayoutId(provider?.getEmptyViewLayoutId())
        }

        showContentView()
    }

    fun setContentViewLayoutId(layoutId: Int?){
        this.contentViewLayoutId = layoutId
    }

    fun setNetworkErrorViewLayoutId(layoutId: Int?){
        this.networkErrorViewLayoutId = layoutId
    }

    fun setExceptionViewLayoutId(layoutId: Int?){
        this.exceptionViewLayoutId = layoutId
    }

    fun setEmptyViewLayoutId(layoutId: Int?){
        this.emptyViewLayoutId = layoutId
    }

    fun setLoadingViewLayoutId(layoutId: Int?){
        this.loadViewLayoutId = layoutId
    }



    private fun inflateLayoutByLayoutId(resId:Int?):View?{
        resId?:return null
        if(resId == 0)return null
        val view = LayoutInflater.from(context).inflate(resId,this,false)
        addView(view)
        return view
    }

    fun getContentView():View? = contentView
    fun getNetworkErrorView():View? = networkErrorView
    fun getLoadingView():View? = loadingView
    fun getExceptionView():View? = exceptionView
    fun getEmptyView():View? = emptyView

    fun showExceptionView(){
        contentView?.gone()
        networkErrorView?.gone()
        loadingView?.gone()
        emptyView?.gone()
        exceptionView?.visible()
    }

    fun showContentView(){
        contentView?.visible()
        networkErrorView?.gone()
        loadingView?.gone()
        emptyView?.gone()
        exceptionView?.gone()
    }

    fun showNetworkErrorView(){
        contentView?.gone()
        networkErrorView?.visible()
        loadingView?.gone()
        emptyView?.gone()
        exceptionView?.gone()
    }

    fun showLoadingView(){
        contentView?.gone()
        networkErrorView?.gone()
        loadingView?.visible()
        emptyView?.gone()
        exceptionView?.gone()
    }

    fun showEmptyView(){
        contentView?.gone()
        networkErrorView?.gone()
        loadingView?.gone()
        emptyView?.visible()
        exceptionView?.gone()
    }

    interface LayoutProvider{
        fun getLoadingViewLayoutId():Int?
        fun getExceptionViewLayoutId():Int?
        fun getNetworkErrorViewLayoutId():Int?
        fun getEmptyViewLayoutId():Int?
    }
}

