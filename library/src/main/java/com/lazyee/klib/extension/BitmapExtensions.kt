package com.lazyee.klib.extension

import android.graphics.Bitmap
import android.util.Log
import java.io.File
import java.io.FileOutputStream

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:Bitmap拓展方法
 * Date: 2022/12/1 09:27
 */
fun Bitmap.toPNGFile(file: File){
    val fos = FileOutputStream(file)
    compress(Bitmap.CompressFormat.PNG,100,fos)
    fos.flush()
    fos.close()
}

fun Bitmap.toJPEGFile(file: File){
    val fos = FileOutputStream(file)
    compress(Bitmap.CompressFormat.JPEG,100,fos)
    fos.flush()
    fos.close()
}
