package com.lazyee.klib.permission

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:权限状态
 * Date: 2024/3/22 11:22
 */
data class PermissionStatus(val permission:String,
                            var isGranted:Boolean = false,
                            var doNotAskAgain:Boolean = false)
