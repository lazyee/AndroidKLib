package com.lazyee.klib.filter.edittext

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText
import java.lang.StringBuilder
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.max

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:中英文混合混合输入的时候,以中文为基础，英文是中文的几分一长度,
 *              比如说一个中文字符长度是2，一个英文长度是1，
 *              那么最大长度可以输入16个中文字符的输入框，最大可以输入32个英文字符
 * Date: 2022/5/13 10:21 下午
 */
class ChineseEnglishMixedLimitLengthInputFilter(val editText: EditText,
                                                      val maxLength:Int = 16,
                                                      private val singleChineseLength:Int = 2) :
    InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        val realMaxLength = maxLength * 2
        var count = 0

        editText.text.forEach {
            count += if(isChinese(it)) singleChineseLength else 1
        }

        val resultStringBuilder = StringBuilder()
        source?.forEach {
            if(count < realMaxLength){
                count += if(isChinese(it)) singleChineseLength else 1
                resultStringBuilder.append(it)
            }
        }

        return resultStringBuilder
    }

    private val chinesePattern by lazy { Pattern.compile("[\u4e00-\u9fa5]") }
    private fun isChinese(char:Char): Boolean = chinesePattern.matcher(char.toString()).matches()

}