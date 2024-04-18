package com.lazyee.klib.handler

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.lazyee.klib.typed.VoidCallback

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2024/4/18 10:11
 */
class SimpleHandler:Handler(Looper.getMainLooper()) {
    fun callback(callback:VoidCallback){
        val msg = Message()
        msg.obj = callback
        sendMessage(msg)
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        (msg.obj as VoidCallback).invoke()
    }
}
