package com.lazyee.klib.constant

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:App常量
 * Date: 2022/4/29 3:02 下午
 */
object AppConstants {
    /**
     * 软键盘高度
     * 这个不一定有值，是在BaseActivity中设置键盘显示监听之后才有可能会有值，这个高度不及时更新,
     * 除非你给每一个activity都设置监听才会获取到最新的值
     */
    var SOFT_KEYBOARD_HEIGHT = 0
}