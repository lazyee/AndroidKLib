package com.lazyee.klib.filter.edittext

import android.text.InputFilter
import android.text.Spanned
import java.lang.StringBuilder

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:禁止输入指定字符,example: arrayListOf("<", ">", "&")
 * Date: 2022/5/11 3:02 下午
 */
class LegalCharInputFilter(private val legalRule: String) :InputFilter {
    companion object{
        /**
         * 邮箱
         */
        const val EMAIL = "0123456789@ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz."

        /**
         * 号码
         */
        const val PHONE = "0123456789-"

        /**
         * 中国身份证
         */
        const val CHINESE_ID = "0123456789Xx"
    }
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        source?:return ""
        return replaceAllIllegalChar(source.toString())
    }


    private fun replaceAllIllegalChar(input: String): String {
        val result = StringBuilder()
        input.forEach {
            if(legalRule.contains(it)){
                result.append(it)
            }
        }
        return result.toString()
    }
}