package com.lazyee.klib.extension

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * @Author leeorz
 * @Date 11/23/20-5:18 PM
 * @Description:
 */

/**
 *  在字符串中添加资源图片
 */
fun TextView.addDrawable(startIndex:Int = 0, vararg resIds:Int){
    val span:SpannableStringBuilder = SpannableStringBuilder(text)
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