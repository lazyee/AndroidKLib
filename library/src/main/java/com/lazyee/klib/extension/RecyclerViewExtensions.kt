package com.lazyee.klib.extension

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:RecyclerView拓展方法
 * Date: 2022/5/13 2:50 下午
 */

/**
 * 移动指定position到中间
 * @receiver RecyclerView
 * @param position Int
 * @param duration Int 滚动的毫秒数
 */
fun RecyclerView.scrollPositionToCenter(position:Int, duration:Int){
    try {
        val layoutManager = layoutManager as LinearLayoutManager
        val scroller =  CenterSmoothScroller(context,duration)
        scroller.targetPosition = if(position < 0) 0 else position
        if(scroller.isRunning)return
        layoutManager.startSmoothScroll(scroller)
    }catch (e:Exception){
        e.printStackTrace()
        scrollToPosition(position)
    }
}

private class CenterSmoothScroller(context: Context, private val duration:Int) : LinearSmoothScroller(context) {

    override fun calculateTimeForDeceleration(dx: Int): Int {
        return duration
    }

    override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
        return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
    }

    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
        return 50f / displayMetrics.densityDpi
    }
}

/**
 * 获取所有显示在屏幕上的itemView
 */
fun RecyclerView.getAllVisibleItems():List<VisibleItemView>{
    val allVisibleItemList = mutableListOf<VisibleItemView>()

    layoutManager?:return allVisibleItemList

    var firstVisibleItemPosition = -1
    var lastVisibleItemPosition = -1
    if(layoutManager is LinearLayoutManager){
        (layoutManager as LinearLayoutManager).run {
            firstVisibleItemPosition = findFirstVisibleItemPosition()
            lastVisibleItemPosition = findLastVisibleItemPosition()
        }
    }else if(layoutManager is GridLayoutManager){
        (layoutManager as GridLayoutManager).run {
            firstVisibleItemPosition = findFirstVisibleItemPosition()
            lastVisibleItemPosition = findLastVisibleItemPosition()
        }
    }

    if(firstVisibleItemPosition == -1 || lastVisibleItemPosition == -1){
        return allVisibleItemList
    }

    for (position in firstVisibleItemPosition ..lastVisibleItemPosition){
        val itemView  = layoutManager!!.findViewByPosition(position) ?: continue
        allVisibleItemList.add(VisibleItemView(position,itemView))
    }

    return allVisibleItemList
}

data class VisibleItemView(val position:Int,val itemView:View)