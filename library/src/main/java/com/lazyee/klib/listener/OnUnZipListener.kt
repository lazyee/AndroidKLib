package com.lazyee.klib.listener

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:解压缩监听
 * Date: 2023/9/26 11:05
 */
interface OnUnZipListener {
    fun onUnZipStart()
    fun onUnZipProgress(fileName:String)
    fun onUnZipEnd()
}