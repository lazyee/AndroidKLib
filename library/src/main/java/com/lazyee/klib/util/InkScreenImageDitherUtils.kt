package com.lazyee.klib.util

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.floor

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:墨水屏抖动算法工具类
 * Date: 2023/7/2 16:47
 */
object InkScreenImageDitherUtils {

    /**
     * 获取颜色误差值
     */
    private fun getColorErr(color1: PixelColor, color2: PixelColor, rate: Int): PixelColor {
        val r1 = color1.red
        val g1 = color1.green
        val b1 = color1.blue

        val r2 = color2.red
        val g2 = color2.green
        val b2 = color2.blue

        val red = floor( (r1 - r2) / rate.toFloat()).toInt()
        val green = floor( (g1 - g2 ) / rate.toFloat()).toInt()
        val blue = floor( (b1 - b2) / rate.toFloat()).toInt()
        return PixelColor(red,green,blue)
    }


    private fun getNearColorV2(color: PixelColor, palette: Array<PixelColor>): PixelColor {
        var minDistanceSquared = 255 * 255 + 255 * 255 + 255 * 255 + 1
        var bestIndex = 0
        val red = color.red
        val green = color.green
        val blue = color.blue

        for (i in palette.indices) {

            val redDiff = (red and 0xff) - (palette[i].red and 0xff)
            val greenDiff = (green and 0xff) - (palette[i].green and 0xff)
            val blueDiff = (blue and 0xff) - (palette[i].blue and 0xff)

            val distanceSquared = redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff
            if (distanceSquared < minDistanceSquared) {
                minDistanceSquared = distanceSquared
                bestIndex = i
            }

        }
        return palette[bestIndex]
    }

    private fun updatePixel(pixels: MutableList<PixelColor>,index:Int,color:PixelColor){
        if(index >= pixels.size)return
        pixels[index] = color
    }

    private fun updatePixelErr(pixels: MutableList<PixelColor>, index:Int, err:PixelColor, rate:Int) {

        if(index >= pixels.size)return
        val originColor = pixels[index]
        val originRed = originColor.red
        val originGreen = originColor.green
        val originBlue = originColor.blue

        val errRed = err.red
        val errGreen = err.green
        val errBlue = err.blue

        pixels[index] = PixelColor(getAvailableColorValue(originRed + errRed * rate) ,
            getAvailableColorValue(originGreen + errGreen * rate),
            getAvailableColorValue(originBlue + errBlue * rate))
    }

    private fun getAvailableColorValue(colorVal:Int): Int {
        if(colorVal < 0){
            return 0
        }
        if(colorVal > 255){
            return 255
        }
        return colorVal
    }

    private fun getBitmapPixels(bitmap: Bitmap): MutableList<PixelColor> {
        val tempPixels = IntArray(bitmap.width * bitmap.height)

        bitmap.getPixels(tempPixels,0,bitmap.width,0,0,bitmap.width,bitmap.height)
        val pixels = mutableListOf<PixelColor>()
        tempPixels.forEach {
            pixels.add(PixelColor(Color.red(it), Color.green(it), Color.blue(it)))
        }
        return pixels
    }

    fun dithering(bitmap: Bitmap, threshold:Int, filter:InkFilterMode): Bitmap {
        val bayerThresholdMap = arrayOf(
            intArrayOf(15, 135, 45, 165),
            intArrayOf(195, 75, 225, 105),
            intArrayOf(60, 180, 30, 150),
            intArrayOf(240, 120, 210, 90)
        )

        val lumR = FloatArray(256) { it * 0.299f }
        val lumG = FloatArray(256) { it * 0.587f }
        val lumB = FloatArray(256) { it * 0.114f }

        val pixels = getBitmapPixels(bitmap)

        pixels.forEachIndexed { index, color ->

            val red = color.red
            val green = color.green
            val blue = color.blue
            val newRed = floor(lumR[red] + lumG[green] + lumB[blue]).toInt()
            pixels[index] = PixelColor(newRed,green,blue)
        }

        val bitmapWidth = bitmap.width

        pixels.forEachIndexed { index, color ->
            var red = color.red
//            var green = color.green
//            var blue = color.blue
            when (filter) {
                InkFilterMode.BINARY -> {//none
                    red = if(red < threshold) 0 else 255
                }
                InkFilterMode.BAYER -> {//bayer
                    // 4x4 Bayer ordered dithering algorithm
                    val x = index % bitmapWidth
                    val y = floor(index.toDouble() / bitmapWidth).toInt()
                    val map = floor( (red + bayerThresholdMap[x % 4][y % 4]) / 2.0f );
                    red = if( map < threshold)  0 else 255
                }
                InkFilterMode.FLOYD_STEINBERG -> {//Floyda-Steinberg
                    // Floyda-Steinberg dithering algorithm
                    val newRed = if(red < 129) 0 else 255
                    val errRed = floor((red - newRed) / 16f).toInt()
                    red = newRed

                    updatePixelsRedValue(pixels,index + 1,errRed * 7)
                    updatePixelsRedValue(pixels,index + bitmapWidth - 1, errRed * 3)
                    updatePixelsRedValue(pixels,index + bitmapWidth,errRed * 5)
//                    updatePixelsRedValue(pixels,index + bitmapWidth + 1,errRed * 1)

                }
                InkFilterMode.ATKINSON -> {
                    // Bill Atkinson's dithering algorithm
                    val newRed = if(red < threshold) 0 else 255
                    val errRed = floor((red - newRed) / 8f).toInt()
                    red = newRed

                    updatePixelsRedValue(pixels, index + 1,errRed)
                    updatePixelsRedValue(pixels, index + 2,errRed)
                    updatePixelsRedValue(pixels, index + bitmapWidth - 1,errRed)
                    updatePixelsRedValue(pixels, index + bitmapWidth,errRed)
                    updatePixelsRedValue(pixels, index + bitmapWidth + 1,errRed)
                    updatePixelsRedValue(pixels, index + 2 * bitmapWidth,errRed)

                }
            }

            pixels[index] = PixelColor(red,red,red)
        }

        return createDitherBitmap(pixels,bitmap)
    }

    private fun updatePixelsRedValue(pixels: MutableList<PixelColor>,index:Int,errRedValue:Int){
        if(index >= pixels.size)return
        val color = pixels[index]
        val red = color.red + errRedValue
        var green = color.green
        var blue = color.blue
        pixels[index] = PixelColor(red,green,blue)
    }

    fun ditheringCanvasByPalette(bitmap: Bitmap, palette: Array<PixelColor>, filter:InkFilterMode): Bitmap {
        val bitmapWidth = bitmap.width
        val pixels = getBitmapPixels(bitmap)
        pixels.forEachIndexed { index,color ->
            val newColor = getNearColorV2(color,palette)
            when (filter) {
                InkFilterMode.BINARY -> {
                    updatePixel(pixels,index,newColor)
                }
                InkFilterMode.BAYER->{  }
                InkFilterMode.FLOYD_STEINBERG -> {
                    val errColor = getColorErr(color, newColor, 16)

                    updatePixel(pixels,index,newColor)
                    updatePixelErr(pixels,index + 1,errColor,7)
                    updatePixelErr(pixels,index + bitmapWidth - 1,errColor,3)
                    updatePixelErr(pixels,index + bitmapWidth,errColor,5)
//                    updatePixelErr(pixels,index + bitmapWidth + 1,errColor,1)

                }
                InkFilterMode.ATKINSON -> {
                    val errColor = getColorErr(color, newColor, 8);

                    updatePixel(pixels,index,newColor)
                    updatePixelErr(pixels,index + 1,errColor,1)
                    updatePixelErr(pixels,index + 2,errColor,1)
                    updatePixelErr(pixels,index + bitmapWidth - 1,errColor,1)
                    updatePixelErr(pixels,index + bitmapWidth ,errColor,1)
                    updatePixelErr(pixels,index + bitmapWidth + 1,errColor,1)
                    updatePixelErr(pixels,index + 2 * bitmapWidth,errColor,1)
                }
            }
        }

        return createDitherBitmap(pixels,bitmap)
    }

    private fun createDitherBitmap(pixels: List<PixelColor>, bitmap: Bitmap): Bitmap {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true)
        val realPixels = IntArray(pixels.size)
        pixels.forEachIndexed{ index,color, ->
            realPixels[index] = Color.rgb(color.red,color.green,color.blue)
        }
        mutableBitmap.setPixels(realPixels,0,bitmap.width,0,0, bitmap.width,bitmap.height)
        return mutableBitmap
    }


}

class PixelColor(var red:Int,var green:Int,var blue:Int,var alpha:Int = 255)

object InkPalette{
    /**
     * 3色 黑白红
     */
    val BWR = arrayOf<PixelColor>(
        PixelColor(0, 0, 0, 255),
        PixelColor(255, 255, 255, 255),
        PixelColor(255, 0, 0, 255)
    )

    /**
     * 4色 黑白红黄
     */
    val BWRY = arrayOf<PixelColor>(
        PixelColor(0, 0, 0, 255),
        PixelColor(255, 255, 255, 255),
        PixelColor(255, 0, 0, 255),
        PixelColor(255, 255, 0, 255)
    )

    /**
     * 2色 黑白
     */
    val BW = arrayOf<PixelColor>(
        PixelColor(0, 0, 0, 255),
        PixelColor(255, 255, 255, 255),
    )

    /**
     * 6色 黑白红黄蓝绿
     */
    val BWRYBG = arrayOf<PixelColor>(
        PixelColor(0, 0, 0, 255),
        PixelColor(255, 255, 255, 255),
        PixelColor(255, 0, 0, 255),
        PixelColor(255, 255, 0, 255),
        PixelColor(0, 0, 255, 255),
        PixelColor(0, 255, 0, 255)
    )
}


/**
 * 滤镜
 */
enum class InkFilterMode{
    BINARY,
    BAYER,
    FLOYD_STEINBERG,
    ATKINSON
}
