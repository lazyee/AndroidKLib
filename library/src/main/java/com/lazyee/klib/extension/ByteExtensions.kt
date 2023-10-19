package com.lazyee.klib.extension

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:byte拓展方法
 * Date: 2023/10/19 10:59
 */

/**
 * byte[]转16进制
 */
fun ByteArray?.toHexString(): String {
    this?:return ""
    if (isEmpty()) return ""
    val sb = StringBuilder(size)
    for (byteChar in this) {
        sb.append(String.format("%02X", byteChar))
    }
    return sb.toString()
}

