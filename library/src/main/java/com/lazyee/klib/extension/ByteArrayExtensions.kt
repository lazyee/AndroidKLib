package com.lazyee.klib.extension

/**
 * @Author leeorz
 * @Date 2020/11/5-3:25 PM
 * @Description:
 */

/**
 *
 * @receiver ByteArray
 * @return String
 */
fun ByteArray.hex(): String {
    return joinToString("") { "%02X".format(it) }
}