package com.lazyee.klib.edittext.textwatcher

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:使用这个必须要将editText输入控制在1234567890.的范围
 * Date: 2023/6/15 10:54
 */
class DecimalInputTextWatcher(private val decimalLength:Int) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


    }

    override fun afterTextChanged(s: Editable?) {
        s?:return
        if(s.length > 1 && s.startsWith('0') && !s.startsWith("0.")) {
            val result = s.trimStart('0')
            val delCount = s.length - result.length
            s.delete(0,delCount)
            if(TextUtils.isEmpty(s)){
                s.append("0")
            }
        }

        if(s.isNotEmpty() && s.startsWith(".")){
            s.insert(0,"0")
        }

        val pointCount = s.count { it == '.' }
        if(pointCount > 1){
            while (s.count { it == '.' } > 1){
                val lastPointIndex = s.indexOfLast { it == '.' }
                s.delete(lastPointIndex,lastPointIndex + 1)
            }
        }

        if(s.contains('.')){
            val pointIndex = s.indexOf('.')
            val end = pointIndex + decimalLength + 1
            if(s.length > end){
                s.delete(end,s.length)
            }
        }
    }
}