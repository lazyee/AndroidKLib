package com.lazyee.klib.debug

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:debug config 接口
 * Date: 2022/5/16 11:27 上午
 */
abstract class DebugConfig {
    var isSelected = false
    abstract fun getKey():String
}