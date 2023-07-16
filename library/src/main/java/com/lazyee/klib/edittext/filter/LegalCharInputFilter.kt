package com.lazyee.klib.edittext.filter

import android.text.InputFilter
import android.text.Spanned
import java.lang.StringBuilder

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:只能输入指定的字符
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

        /**
         * 整形数字
         */
        const val INT = "0123456789"
        /**
         * 小数
         */
        const val DECIMAL = "0123456789."
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