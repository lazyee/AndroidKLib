package com.lazyee.klib.extension

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.lazyee.klib.util.AppUtils

/**
 * @Author leeorz
 * @Date 2020/11/2-6:58 PM
 * @Description: BaseActivity 的拓展方法
 */

val Activity.extras:Bundle?
    get() = intent.extras

/**
 * 打开一个Activity
 * @receiver Activity
 * @param clazz Class<out Activity>?
 * @param action String?
 * @param data Uri?
 * @param setIntentExtraBlock Function1<Intent, Unit>?
 * @param setBundleBlock Function1<Bundle, Unit>?
 * @param flag Int?
 * @param requestCode Int?
 */
fun Activity.goto(clazz:Class<out Activity>? = null,
                  action: String? = null,
                  data: Uri? = null,
                  setIntentExtraBlock:((Intent)->Unit)? = null,
                  setBundleBlock: ((Bundle)->Unit)? = null,
                  flag:Int? = null,
                  requestCode:Int? = null){

    val intent = AppUtils.createIntent(this,
            clazz,
            action,
            data,
            setIntentExtraBlock,
            setBundleBlock,
            flag)
    intent?:return
    if (requestCode == null){
        startActivity(intent)
    }else{
        startActivityForResult(intent,requestCode)
    }
}
