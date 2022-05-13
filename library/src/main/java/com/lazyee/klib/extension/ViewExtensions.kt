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

fun View.setSingleClick(onSingleClick: OnSingleClick){
    setOnClickListener { SingleClick.click(onSingleClick) }
}