package com.lazyee.klib.click

/**
 * @Author leeorz
 * @Date 3/30/21-5:40 PM
 * @Description:防止重复点击
 */
object SingleClick {
    private var intervalTime = 500L
    private var lastClickTime = 0L

    /**
     * 设置点击的时间间隔
     * @param time Long
     */
    fun init(time :Long){
        intervalTime = time
    }

    fun click(onClick:OnSingleClick){
        val currentTimeMillis = System.currentTimeMillis()
        if(currentTimeMillis - lastClickTime < intervalTime)return
        lastClickTime = currentTimeMillis
        onClick()
    }
}

typealias OnSingleClick = ()->Unit