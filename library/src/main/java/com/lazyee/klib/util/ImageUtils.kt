package com.lazyee.klib.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2023/10/25 09:35
 */

object ImageUtils{
    private const val TAG = "[ImageUtils]"
    /**
     * jpeg格式下最大只能输出宽或高为65500的图片，否则compress会输出false
     * https://blog.csdn.net/sinat_17133389/article/details/119152804
     */
    private const val JPEG_MAX_SIZE = 65500

    fun checkMergeJPEGLongImageSizeIsTooLong(filePathList: List<String>,maxWidth:Int = 1000): Boolean {
        var longImageHeight = 0
        val longImageItemList = handleMergeLongImageInfo(filePathList,maxWidth)
        if(longImageItemList.isEmpty()){
            return false
        }

        val longImageWidth = longImageItemList.first().scaleWidth

        if(longImageWidth > JPEG_MAX_SIZE){
            LogUtils.e(TAG,"图片宽度为[${longImageWidth}],已经超出JPEG最大限制[${JPEG_MAX_SIZE}]")
            return true
        }

        longImageHeight = longImageItemList.sumBy { it.scaleHeight }
        if(longImageHeight > JPEG_MAX_SIZE){
            LogUtils.e(TAG,"图片高度为[${longImageHeight}],已经超出JPEG最大限制[${JPEG_MAX_SIZE}]")
            return true
        }

        return false
    }

    private fun handleMergeLongImageInfo(filePathList: List<String>,maxWidth:Int = 1000):List<LongImageItem>{
        var longImageWidth = 0
        val longImageItemList = mutableListOf<LongImageItem>()
        filePathList.forEach {path ->
            if(!File(path).exists())return@forEach
            try {
                val option = BitmapFactory.Options()
                option.inJustDecodeBounds = true
                BitmapFactory.decodeFile(path,option)
                longImageItemList.add(LongImageItem(path,option.outWidth,option.outHeight))
                val imageWidth = min(maxWidth,option.outWidth)
                longImageWidth = max(imageWidth,longImageWidth)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        longImageItemList.forEach { image->
            image.scaleWidth = longImageWidth
            image.scaleHeight = ((image.scaleWidth / image.originWidth.toFloat())  * image.originHeight).toInt()
            calculateInSampleSize(image)
        }
        return longImageItemList
    }

    private fun calculateInSampleSize(image:LongImageItem) {
        var inSampleSize = 1
        if (image.originWidth > image.scaleWidth || image.originHeight > image.scaleHeight) {
            val halfWidth = image.originWidth / 2
            val halfHeight = image.originHeight / 2

            // 计算最大的缩放比例，确保最终图像的宽度和高度不小于所需的宽度和高度
            while (halfWidth / inSampleSize >= image.scaleWidth && halfHeight / inSampleSize >= image.scaleHeight) {
                inSampleSize *= 2
            }
        }
        image.inSampleSize = inSampleSize
    }


    fun mergeLongImage(filePathList: List<String>,outPath:String,callback:MergeLongImageCallback? = null) {
        var longImageHeight = 0
        var longImageWidth = 0
        val longImageItemList = handleMergeLongImageInfo(filePathList)
        if(longImageItemList.isEmpty()){
            callback?.onMergeFailed("没有要合并的有效图片文件")
            return
        }
        longImageWidth = longImageItemList.first().scaleWidth
        longImageItemList.forEach { image->
            longImageHeight += image.scaleHeight
        }

        if(longImageWidth == 0 || longImageHeight == 0){
            callback?.onMergeFailed("计算出长图的宽度或高度为0，无法合成长图")
            return
        }

        callback?.onMergeStart()
        try {
            val longImageBitmap = Bitmap.createBitmap(longImageWidth,longImageHeight, Bitmap.Config.RGB_565)
            val canvas = Canvas(longImageBitmap)
            var top = 0
            val fos = FileOutputStream(outPath)
            longImageItemList.forEachIndexed { index,image->
                callback?.onHandleImage(index,image.filePath)
                val option = BitmapFactory.Options()
                option.inJustDecodeBounds = false
                option.inSampleSize = image.inSampleSize

                val bitmap = BitmapFactory.decodeFile(image.filePath,option)
                val bottom = top + image.scaleHeight
                val dstRect = Rect(0,top,longImageWidth,bottom)
                top = bottom

                canvas.drawBitmap(bitmap,null,dstRect,null)
                bitmap.recycle()
            }
            callback?.onCompressImage()
            longImageBitmap.compress(Bitmap.CompressFormat.JPEG,85,fos)
            fos.close()
            callback?.onMergeComplete(outPath)
        }catch (e:Exception){
            e.printStackTrace()
            callback?.onMergeFailed(e.message?:"合并失败")
        }

    }

    private data class LongImageItem(val filePath:String,
                         var originWidth:Int = 0,
                         var originHeight:Int = 0,
                        var scaleWidth:Int = 0,
                        var scaleHeight:Int = 0,
                        var inSampleSize:Int = 1)

    interface MergeLongImageCallback{
        fun onMergeStart()
        fun onHandleImage(index:Int,filePath:String)
        fun onCompressImage()
        fun onMergeComplete(outPath:String)
        fun onMergeFailed(msg:String)
    }
}
