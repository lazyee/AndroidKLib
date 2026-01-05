package com.lazyee.klib.manager

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * ClassName: TabFragmentManager
 * Description:主要用在tab页面中对fragment进行管理
 *         TabFragmentManager(supportFragmentManager,R.id.flContent).apply {
 *             addTab(llTab1) { Tab1Fragment() }
 *             addTab(llTab2) { Tab2Fragment() }
 *             addTab(llTab3) { Tab3Fragment() }
 *         }
 * Date: 2025/6/4 15:09
 * @author Leeorz
 */
class TabFragmentManager(private val fragmentManager: FragmentManager, private val containerLayoutId:Int) {
    private val fragmentMap = HashMap<String,Fragment>()
    private val tabViews = mutableListOf<View>()

    fun addTab(tabView: View,block:()->Fragment) {
        tabViews.add(tabView)
        tabView.setOnClickListener {
            show(it.id.toString(),block)
            changeTabEnableState(it.id)
        }
    }

    private fun initFragment(tag:String,block:()->Fragment) {
        if(fragmentMap.containsKey(tag)) {
           return
        }

        var fragment = fragmentManager.findFragmentByTag(tag)
        if(fragment == null){
             fragment = block.invoke()
        }

        fragmentMap[tag] = fragment
    }

    private fun show(tag: String,block:()->Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        initFragment(tag,block)
        fragmentMap.keys.forEach { key ->
            val fragment = fragmentMap[key]
            if(key == tag) {
                if(fragment!!.isAdded) {
                    fragmentTransaction.show(fragment)
                }else{
                    fragmentTransaction.add(containerLayoutId,fragment,tag)
                }
            }else{
                if(fragment!!.isAdded) {
                    fragmentTransaction.hide(fragment)
                }
            }
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    /**
     * 切换tab的可用状态，在xml需要实现enable属性true,false的状态
     */
    private fun changeTabEnableState(id: Int) {
        tabViews.forEach { tabView ->
            tabView.isEnabled = tabView.id != id
        }
    }

}