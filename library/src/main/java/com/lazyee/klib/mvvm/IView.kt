package com.lazyee.klib.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * @Author leeorz
 * @Date 3/25/21-3:12 PM
 * @Description:MVVM中的View层
 */
interface IView {
    fun onLoadingStateChanged(state: LoadingState)

    fun initViewModel(owner: LifecycleOwner,viewModel: BaseViewModel){
        viewModel.getRepositoryList().forEach {
            owner.lifecycle.addObserver(it)
        }

        viewModel.loadingStateLiveData.observe(owner,object :Observer<LoadingState>{
            override fun onChanged(state: LoadingState?) {
                state?:return
                onLoadingStateChanged(state)
            }
        })
    }
}