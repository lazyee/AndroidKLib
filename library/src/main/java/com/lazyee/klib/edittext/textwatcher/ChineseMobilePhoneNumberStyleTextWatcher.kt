package com.lazyee.klib.edittext.textwatcher

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ReplacementSpan

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:中国手机号码自动空开间隔 138 0000 0000
 * Date: 2023/7/17 17:36
 */
class ChineseMobilePhoneNumberStyleTextWatcher(private val margin:Int) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        s?:return
//        s.clearSpans()
        val spans = s.getSpans(0,s.length,SpacingSpan::class.java)
        spans.forEach { s.removeSpan(it) }
        if(s.length >= 3){
            s.setSpan(SpacingSpan(margin),2,3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        if(s.length >= 7){
            s.setSpan(SpacingSpan(margin),6,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

    }

    class SpacingSpan(private val spacing: Int) : ReplacementSpan() {

        override fun getSize(
            paint: Paint,
            text: CharSequence?,
            start: Int,
            end: Int,
            fm: Paint.FontMetricsInt?
        ): Int {
            return paint.measureText(text, start, end).toInt() + spacing
        }

        override fun draw(
            canvas: Canvas,
            text: CharSequence?,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {
            text?:return
            canvas.drawText(text, start, end, x, y.toFloat(), paint)
        }
    }
}