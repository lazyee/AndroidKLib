package com.lazyee.klib.util

import android.content.Context
import android.widget.Toast
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.lazyee.klib.extension.safeToLong

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2022/5/5 3:24 下午
 */
object AppUtils {
    /**
     * 比较版本名,如果当前版本名比待比较的版本名大，那么返回true
     * @receiver String
     * @param currentVersionName String 1.1.1.1
     * @param compareVersionName String 1.1.1.2
     * @return Boolean
     */
    fun compareVersionName(currentVersionName:String,compareVersionName: String): Boolean {
        val currentVersionNameArr = currentVersionName.split("\\.".toRegex()).toTypedArray()
        val compareVersionNameArr = compareVersionName.split("\\.".toRegex()).toTypedArray()
        val loopCount = compareVersionNameArr.size.coerceAtLeast(currentVersionNameArr.size)
        val currentVersionArr = arrayOfNulls<String>(loopCount)
        for (i in currentVersionNameArr.indices) {
            currentVersionArr[i] = currentVersionNameArr[i]
        }
        val compareVersionArr = arrayOfNulls<String>(loopCount)
        for (i in compareVersionNameArr.indices) {
            compareVersionArr[i] = compareVersionNameArr[i]
        }
        for (i in 0 until loopCount) {
            val currentVersion = currentVersionArr[i].safeToLong()
            val compareVersion = compareVersionArr[i].safeToLong()
            if (currentVersion != compareVersion) {
                return currentVersion > compareVersion
            }
        }

        return false
    }
}