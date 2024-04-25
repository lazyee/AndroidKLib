package com.lazyee.klib.extension

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.lazyee.klib.click.SingleClick
import com.lazyee.klib.typed.OnSingleClick


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

fun View.setBackground(corner:Float,
                       borderWidth:Int? = null,
                       borderColor:Int? = null,
                       backgroundColor:Int){
    setBackground(corner,corner,corner,corner,borderWidth,borderColor,backgroundColor)
}

fun View.setBackground(topLeftCorner:Float = 0f,
                       topRightCorner:Float = 0f,
                       bottomLeftCorner:Float = 0f,
                       bottomRightCorner:Float = 0f,
                       borderWidth:Int? = null,
                       borderColor:Int? = null,
                       backgroundColor:Int){
    val drawable = GradientDrawable()
    drawable.cornerRadii = floatArrayOf(
        topLeftCorner, topLeftCorner,
        topRightCorner, topRightCorner,
        bottomLeftCorner, bottomLeftCorner,
        bottomRightCorner, bottomRightCorner
    )
    drawable.setColor(backgroundColor)

    if(borderColor != null && borderWidth != null){
        drawable.setStroke(borderWidth.toInt(),borderColor)
    }
    background = drawable
}

/**
 * 设置控件大小
 */
fun View.setSize(w:Number ?= null,h:Number? = null){
    layoutParams?:return
    val lp = layoutParams
    w?.run { lp.width = w.toInt() }
    h?.run { lp.height = h.toInt() }
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

fun View.hideKeyboard(){
    if(context !is Activity)return
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.showKeyboard(){
    if(context !is Activity)return
    val inputMethodManager =context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}
