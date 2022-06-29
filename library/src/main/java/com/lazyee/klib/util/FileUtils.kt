package com.lazyee.klib.util

import android.os.Environment
import android.os.StatFs
import android.text.TextUtils

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:与文件相关的工具类
 * Date: 2022/6/22 13:22
 */
object FileUtils {

    /**
     * Return the total size of file system.
     *
     * @param anyPathInFs Any path in file system.
     * @return the total size of file system
     */
    fun getFsTotalSize(anyPathInFs: String): Long{
        if(TextUtils.isEmpty(anyPathInFs))return 0L
        val statFs = StatFs(anyPathInFs)
        var blockSize:Long = 0
        var totalSize:Long = 0
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2){
            blockSize = statFs.blockSizeLong
            totalSize = statFs.blockCountLong
        }else{
            blockSize = statFs.blockSize.toLong()
            totalSize = statFs.blockCount.toLong()
        }
        return blockSize * totalSize
    }

    /**
     * Return the available size of file system.
     *
     * @param anyPathInFs Any path in file system.
     * @return the available size of file system
     */
    fun getFsAvailableSize(anyPathInFs: String): Long {
        if(TextUtils.isEmpty(anyPathInFs))return 0L
        val statFs = StatFs(anyPathInFs)
        var blockSize:Long = 0
        var availableSize:Long = 0
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2){
            blockSize = statFs.blockSizeLong
            availableSize = statFs.availableBlocksLong
        }else{
            blockSize = statFs.blockSize.toLong()
            availableSize = statFs.availableBlocks.toLong()
        }

        return blockSize * availableSize

    }
}