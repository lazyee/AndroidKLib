package com.lazyee.klib.listener

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:文件复制监听
 * Date: 2023/7/26 15:08
 */
interface OnFileCopyListener {
    fun onCopyStart()
    fun onCopyProgress(progress:Long,total:Long)
    fun onCopyComplete()
    fun onCopyFailed(errorMessage:String)
}