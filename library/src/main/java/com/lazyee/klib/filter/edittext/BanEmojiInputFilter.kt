package com.lazyee.klib.filter.edittext

import android.text.InputFilter
import android.text.Spanned
import android.text.method.BaseKeyListener

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:禁止输入emoji
 * Date: 2022/5/11 2:25 下午
 */
class BanEmojiInputFilter : InputFilter{

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        source?:return ""
        val result = StringBuilder()
        for (i in start until end){
            val type = Character.getType(source[i])
            if(type != Character.SURROGATE.toInt() && type != Character.OTHER_SYMBOL.toInt()){
                result.append(source[i])
            }
        }
        return result.toString()
    }
}
