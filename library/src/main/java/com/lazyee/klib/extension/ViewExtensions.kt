package com.lazyee.klib.extension

import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
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

/**
 * 设置控件大小
 */
fun View.setSize(w:Number ?= null,h:Number? = null){
    val lp = layoutParams
    w?.run { lp.width = w.toInt() }
    h?.run { lp.width = h.toInt() }
    requestLayout()
}

/**
 * 设置margin
 */
fun View.setMargins(margin:Int){
    setMargins(margin,margin,margin,margin)
}

/**
 * 设置margin
 */
fun View.setMargins(left: Int? = null, top: Int? = null, right:Int? = null, bottom:Int? = null){
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
