package com.lazyee.klib.extension

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:Bitmap拓展方法
 * Date: 2022/12/1 09:27
 */
fun Bitmap.savePNGFile(file: File,targetWidth: Int = -1,targetHeight: Int = -1,quality: Int = 100){
    save(file,targetWidth,targetHeight, Bitmap.CompressFormat.PNG,quality)
}

fun Bitmap.savePNGFile(file: File,quality: Int = 100){
    save(file,-1,-1, Bitmap.CompressFormat.PNG,quality)
}

fun Bitmap.saveJPEGFile(file: File,targetWidth: Int = -1,targetHeight: Int = -1, quality: Int = 100){
    save(file,targetWidth,targetHeight, Bitmap.CompressFormat.JPEG,quality)
}

fun Bitmap.saveJPEGFile(file: File, quality: Int = 100){
    save(file,-1,-1, Bitmap.CompressFormat.JPEG,quality)
}

fun Bitmap.save(file:File,targetWidth:Int = -1,targetHeight:Int = -1, format:Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,quality:Int = 100){
    val saveBitmap:Bitmap = if(targetWidth != -1 && targetHeight != -1){
        Bitmap.createScaledBitmap(this,targetWidth,targetHeight,false)
    }else{
        this
    }

    val fos = FileOutputStream(file)
    saveBitmap.compress(format,quality,fos)
    fos.flush()
    fos.close()
}

/**
 * 转换到base64字符串
 */
fun Bitmap.toBase64String(format:Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,flags:Int = Base64.DEFAULT):String{
    val bos = ByteArrayOutputStream()
    compress(format,100,bos)
    val base64String = Base64.encodeToString(bos.toByteArray(),flags)
    bos.flush()
    bos.close()
    return base64String
}
