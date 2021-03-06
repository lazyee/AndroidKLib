package com.lazyee.klib.extension

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ImageSpan
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.lazyee.klib.click.OnSingleClick
import com.lazyee.klib.click.SingleClick

/**
 * @Author leeorz
 * @Date 11/23/20-5:18 PM
 * @Description:View的拓展方法
 */

fun View.disable(){
    if(this.isEnabled){
        this.isEnabled = false
    }
}

fun View.enable(){
    if(!this.isEnabled){
        this.isEnabled = true
    }
}

fun View.gone(){
    if(this.visibility != View.GONE){
        this.visibility = View.GONE
    }
}

fun View.visible(){
    if(this.visibility != View.VISIBLE){
        this.visibility = View.VISIBLE
    }
}

fun View.invisible(){
    if(this.visibility != View.INVISIBLE){
        this.visibility = View.INVISIBLE
    }
}

fun TextView.setTextOrGone(str:String?){
    if(TextUtils.isEmpty(str)){
        gone()
    }else{
        visible()
        this.text = str
    }
}

fun View.setSingleClick(onSingleClick: OnSingleClick){
    setOnClickListener { SingleClick.click(onSingleClick) }
}

/**
 *  在字符串中添加资源图片
 */
fun TextView.addDrawable(startIndex:Int = 0, vararg resIds:Int){
    val span = SpannableStringBuilder(text)
    for (index in resIds.count() - 1 downTo 0){
        span.insert(startIndex,getDrawablePlaceHolderText("${resIds[index]}_$index"))
    }

    val lineHeight = lineHeight
    for(index in resIds.indices){
        val placeHolder = getDrawablePlaceHolderText("${resIds[index]}_$index")
        val drawable = ContextCompat.getDrawable(context,resIds[index])
        drawable!!.setBounds(4,
            0,
            (drawable.intrinsicWidth * (lineHeight/ drawable.intrinsicHeight.toFloat())).toInt() + 4,
            lineHeight
        )
        val imageSpan = ImageSpan(drawable,ImageSpan.ALIGN_BASELINE)

        val spanIndex = span.indexOf(placeHolder)
        if(spanIndex != -1){
            span.setSpan(imageSpan,spanIndex,spanIndex + placeHolder.length,Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
    }
    text = span
}

/**
 * 获取图片的标示符
 */
private fun getDrawablePlaceHolderText(key:String):String = "[###IMAGEPLACEHOLDER[$key]###]"

/**
 * 移动指定position到中间
 * @receiver RecyclerView
 * @param position Int
 * @param duration Int 滚动的毫秒数
 */
fun RecyclerView.scrollPositionToCenter(position:Int,duration:Int){
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
