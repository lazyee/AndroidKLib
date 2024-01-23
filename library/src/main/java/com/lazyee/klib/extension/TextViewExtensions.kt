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