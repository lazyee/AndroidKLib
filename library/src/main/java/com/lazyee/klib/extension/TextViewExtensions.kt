package com.lazyee.klib.extension

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ImageSpan
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description: TextView 拓展方法
 * Date: 2022/5/13 2:44 下午
 */

/**
 *  在字符串中添加资源图片
 */
fun TextView.addDrawable(startIndex:Int = 0, vararg resIds:Int){
    val ssb = SpannableStringBuilder(text)
    for (index in resIds.count() - 1 downTo 0){
        ssb.insert(startIndex,getDrawablePlaceHolderText("${resIds[index]}_$index"))
    }
    for(index in resIds.indices){
        val placeHolder = getDrawablePlaceHolderText("${resIds[index]}_$index")
        ContextCompat.getDrawable(context,resIds[index])?.apply {
            setBounds(0,0,intrinsicWidth,intrinsicHeight)
            val imageSpan = ImageSpan(this, ImageSpan.ALIGN_BASELINE)
            val spanIndex = ssb.indexOf(placeHolder)
            if(spanIndex != -1){
                ssb.setSpan(imageSpan,spanIndex,spanIndex + placeHolder.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }
    }
    text = ssb
}

/**
 * 设置文本或者Gone
 */
fun TextView.setTextOrGone(str:String?){
    if(TextUtils.isEmpty(str)){
        gone()
    }else{
        visible()
        this.text = str
    }
}

/**
 * 获取图片的标示符
 */
private fun getDrawablePlaceHolderText(key:String):String = "[###IMAGEPLACEHOLDER[$key]###]"