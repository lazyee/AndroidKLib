package com.lazyee.klib.edittext.filter

import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import java.lang.StringBuilder

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:只能输入指定的字符
 * Date: 2022/5/11 3:02 下午
 */
class DecimalCharInputFilter(private val decimalLength: Int) :InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        source?:return ""
        val destLength = dest?.length?:0
        var filterSource = replaceAllIllegalChar(source.toString())
        val destHasPoint = dest?.contains(".")?:false
        val filterSourceHasPoint = filterSource.contains(".")
        if(destHasPoint && filterSourceHasPoint){
            filterSource = filterSource.replace(".","",true)
        }

        val result:String = if (filterSource.startsWith( ".") && dest.isNullOrEmpty()) {
            subTextByDecimalLength("0$filterSource")
        }else{
            subTextByDecimalLength(dest.toString() + filterSource)
        }
        val res = result.substring(destLength,result.length)
        return res
    }

    private fun subTextByDecimalLength(input:String): String {
        val hasPoint = input.contains(".")
        if(!hasPoint)return input
        val arr = input.split(".")
        if(arr[1].length > this.decimalLength){
            return arr[0] + "." + arr[1].substring(0,decimalLength)
        }
        return input
    }

    private fun replaceAllIllegalChar(input: String): String {
        val legalRule = "1234567890."
        val result = StringBuilder()
        var hasPoint = false
        input.forEach {
            if(legalRule.contains(it)){
                if(hasPoint && it == '.'){
                   return@forEach
                }
                result.append(it)
                if(it == '.'){
                    hasPoint = true
                }
            }
        }
        return result.toString()
    }
}