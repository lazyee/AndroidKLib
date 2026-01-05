package com.lazyee.klib.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Looper
import com.lazyee.klib.bean.ImageSize
import com.lazyee.klib.listener.OnFileCopyListener
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:文件操作拓展方法
 * Date: 2022/5/20 9:47 上午
 */


/**
 * 获取音频时间
 * 推荐临时使用，在需要循环获取音频时间的场景，不建议使用这个方法
 */
fun File.getAudioDuration(): Long {
    try {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(absolutePath)
        return mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()?:0L
    }catch (e:Exception){
        return 0L
    }
}

/**
 * 获取图片大小
 */
fun File.getImageSize(): ImageSize {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    val bitmap = BitmapFactory.decodeFile(absolutePath)
    return ImageSize(bitmap.width,bitmap.height)
}

/**
 * 复制文件，增加文件复制监听
 */
fun File.copy(destFilePath:String):Boolean {
    if(!exists()){
        return false
    }
    if(!isFile){
        return false
    }
    if(!canRead()){
        return false
    }

    val fis = FileInputStream(this)
    val destFile = File(destFilePath)
    if(!destFile.exists()){
        destFile.createNewFile()
    }
    val fos = FileOutputStream(destFile)
    val buffer = ByteArray(8 * 1024)
    var copyProgress = 0L
    var byteRead = 0
    while (-1 != fis.read(buffer).also { byteRead = it }){
        copyProgress += byteRead
        fos.write(buffer,0,byteRead)
    }
    fis.close()
    fos.flush()
    fos.close()

    return true
}

/**
 * 复制文件，增加文件复制监听
 * OnFileCopyListener 运行在子线程
 */
fun File.copy(destFilePath:String, listener: OnFileCopyListener){
    if(!exists()){
        listener.onCopyFailed("源文件不存在")
        return
    }
    if(!isFile){
        listener.onCopyFailed("源文件不是一个文件")
        return
    }
    if(!canRead()){
        listener.onCopyFailed("源文件不可读")
        return
    }
    Thread{
        val fis = FileInputStream(this)
        val destFile = File(destFilePath)
        if(!destFile.exists()){
            destFile.createNewFile()
        }
        val fos = FileOutputStream(destFile)
        val buffer = ByteArray(8 * 1024)
        var copyProgress = 0L
        var byteRead = 0
        listener.onCopyStart()
        while (-1 != fis.read(buffer).also { byteRead = it }){
            copyProgress += byteRead
            listener.onCopyProgress(copyProgress,length())
            fos.write(buffer,0,byteRead)
        }
        fis.close()
        fos.flush()
        fos.close()

        listener.onCopyComplete()
    }.start()
}
