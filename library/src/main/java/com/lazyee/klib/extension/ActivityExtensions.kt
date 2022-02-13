package com.lazyee.klib.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle


/**
 * 打开一个Activity
 * @receiver Activity
 * @param clazz Class<out Activity>?
 * @param flag Int?
 * @param requestCode Int?
 */
fun Activity.goto(clazz:Class<out Activity>? = null,
                  bundle: Bundle? = null,
                  flag:Int? = null,
                  requestCode:Int? = null){

    val intent = Intent(this,clazz)
    if(flag != null) intent.flags = flag
    if(bundle != null) intent.putExtras(bundle)

    if (requestCode == null){
        startActivity(intent)
    }else{
        startActivityForResult(intent,requestCode)
    }
}
