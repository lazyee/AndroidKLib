package com.lazyee.klib.util

import android.content.Context
import android.widget.Toast
import com.google.android.material.snackbar.BaseTransientBottomBar

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2022/5/5 3:24 下午
 */
object ToastUtils {
    @Deprecated("use toastShort method")
    fun shortToast(context: Context, msg:String){
        toast(context,msg,Toast.LENGTH_SHORT)
    }

    fun toastShort(context:Context,msg:String){
        toast(context,msg,Toast.LENGTH_SHORT)
    }

    @Deprecated("use toastLong method")
    fun longToast(context:Context,msg:String){
        toast(context,msg,Toast.LENGTH_LONG)
    }

    fun toastLong(context: Context,msg:String){
        toast(context,msg,Toast.LENGTH_LONG)
    }

    private fun toast(context: Context, msg:String,duration:Int){
        Toast.makeText(context,msg,duration).show()
    }
}