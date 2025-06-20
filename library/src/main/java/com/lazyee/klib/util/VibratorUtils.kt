package com.lazyee.klib.util

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission

/**
 * ClassName: VibratorUtils
 * Description:震动工具类
 * Date: 2025/6/19 16:15
 * @author Leeorz
 */
object VibratorUtils {
    // 简单震动：默认震动 200 毫秒
    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(context: Context, durationMillis: Long = 200,amplitude:Int = -1) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(durationMillis, amplitude))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(durationMillis)
        }
    }

    // 自定义震动节奏（pattern）：[延迟, 震动, 停止, 震动, ...]
    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibratePattern(context: Context, pattern: LongArray, repeat: Int = -1) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeat))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, repeat)
        }
    }

    // 取消震动
    @RequiresPermission(Manifest.permission.VIBRATE)
    fun cancel(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        vibrator?.cancel()
    }
}