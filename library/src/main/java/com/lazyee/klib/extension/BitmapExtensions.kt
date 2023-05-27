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
fun Bitmap.savePNGFile(file: File,quality: Int = 100){
    val fos = FileOutputStream(file)
    compress(Bitmap.CompressFormat.PNG,quality,fos)
    fos.flush()
    fos.close()
}

fun Bitmap.saveJPEGFile(file: File,quality: Int = 100){
    val fos = FileOutputStream(file)
    compress(Bitmap.CompressFormat.JPEG,quality,fos)
    fos.flush()
    fos.close()
}

fun Bitmap.save(file:File,format:Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,quality:Int = 100){
    val fos = FileOutputStream(file)
    compress(format,quality,fos)
    fos.flush()
    fos.close()
}

