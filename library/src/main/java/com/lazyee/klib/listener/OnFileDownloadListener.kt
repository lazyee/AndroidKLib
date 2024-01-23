package com.lazyee.klib.listener

import java.io.File

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:文件下载监听
 * Date: 2023/5/31 12:46
 */
interface OnFileDownloadListener {
    fun onDownloadStart(totalSize:Int)
    fun onDownloading(currentSize:Int,totalSize: Int)
    fun onDownloadComplete(file: File)
    fun onDownloadFailure(e:Exception)
}