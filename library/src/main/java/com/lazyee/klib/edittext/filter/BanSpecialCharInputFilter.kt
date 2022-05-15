package com.lazyee.klib.edittext.filter

import android.text.InputFilter
import android.text.Spanned

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:禁止输入指定字符,example: arrayListOf("<", ">", "&")
 * Date: 2022/5/11 3:02 下午
 */
class BanSpecialCharInputFilter(private val banSpecialChars: List<String>) :InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        source?:return ""
        val content = source.toString()
        if (!checkInputLegal(content)) {
            return replaceAllIllegalChar(content)
        }
        return source
    }

    private fun checkInputLegal(input: String): Boolean {
        banSpecialChars.forEach {
            if (input.contains(it)) {
                return false
            }
        }
        return true
    }

    private fun replaceAllIllegalChar(input: String): String {
        var result = input
        banSpecialChars.forEach {
            result = result.replace(it, "")
        }
        return result
    }
}