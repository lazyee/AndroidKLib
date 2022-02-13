package com.lazyee.klib.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * @Author leeorz
 * @Date 2020/11/2-6:58 PM
 * @Description: BaseFragment 的拓展方法
 */

/**
 * Fragment 打开一个Activity
 * @receiver Fragment
 * @param clazz Class<out Activity>?
 * @param flag Int?
 * @param requestCode Int?
 */
fun Fragment.goto(clazz:Class<out Activity>? = null,
                  bundle: Bundle? = null,
                  flag:Int? = null,
                  requestCode:Int? = null){

    activity?:return
    val intent = Intent(activity,clazz)
    if(flag != null) intent.flags = flag
    if(bundle != null) intent.putExtras(bundle)
    if (requestCode == null){
        startActivity(intent)
    }else{
        startActivityForResult(intent,requestCode)
    }
}