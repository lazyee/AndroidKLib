package com.lazyee.klib.extension

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ImageSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.lazyee.klib.click.OnSingleClick
import com.lazyee.klib.click.SingleClick
import java.lang.Exception

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

/**
 * 设置margin
 */
fun View.setMargins(size:Int){
    setMargins(size,size,size,size)
}

/**
 * 设置margin
 */
fun View.setMargins(left:Int? = null,top:Int? = null,right:Int? = null,bottom:Int? = null){
    if(left == null && top == null && right == null && bottom == null) return
    //LinearLayout
    if(this.layoutParams is LinearLayout.LayoutParams){
        (this.layoutParams as LinearLayout.LayoutParams).run {
            this.setMargins(left?:this.leftMargin,
                top?:this.topMargin,
                right?:this.rightMargin,
                bottom?:this.rightMargin)
        }
        requestLayout()
        return
    }

    //FrameLayout
    if(this.layoutParams is FrameLayout.LayoutParams){
        (this.layoutParams as FrameLayout.LayoutParams).run {
            this.setMargins(left?:this.leftMargin,
                top?:this.topMargin,
                right?:this.rightMargin,
                bottom?:this.rightMargin)
        }
        requestLayout()
        return
    }

    //RelativeLayout
    if(this.layoutParams is RelativeLayout.LayoutParams){
        (this.layoutParams as RelativeLayout.LayoutParams).run {
            this.setMargins(left?:this.leftMargin,
                top?:this.topMargin,
                right?:this.rightMargin,
                bottom?:this.rightMargin)
        }
        requestLayout()
        return
    }

    //ConstraintLayout
    if(this.layoutParams is ConstraintLayout.LayoutParams){
        (this.layoutParams as ConstraintLayout.LayoutParams).run {
            this.setMargins(left?:this.leftMargin,
                top?:this.topMargin,
                right?:this.rightMargin,
                bottom?:this.rightMargin)
        }
        requestLayout()
        return
    }
}
