package com.lazyee.klib.http

/**
 * @Author leeorz
 * @Date 3/8/21-5:32 PM
 * @Description:HttpCode
 */
object HttpCode {
    private val apiSuccessCodeList:MutableList<String> = mutableListOf<String>().also {
        it.add("200")//默认业务正确的
    }

    fun isSuccessful(code:String?):Boolean = apiSuccessCodeList.find { it == code } != null

    /**
     * 添加成功状态码
     * @param code String
     */
    fun addSuccessCode(code:String){
        apiSuccessCodeList.add(code)
    }

}