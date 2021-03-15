package com.lazyee.klib.extension

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.lazyee.klib.util.AppUtils

/**
 * @Author leeorz
 * @Date 2020/11/2-6:58 PM
 * @Description: BaseFragment 的拓展方法
 */

/**
 * Fragment 打开一个Activity
 * @receiver Fragment
 * @param clazz Class<out Activity>?
 * @param action String?
 * @param data Uri?
 * @param setIntentExtraBlock Function1<Intent, Unit>?
 * @param setBundleBlock Function1<Bundle, Unit>?
 * @param flag Int?
 * @param requestCode Int?
 */
fun Fragment.goto(clazz:Class<out Activity>? = null,
                  action: String? = null,
                  data: Uri? = null,
                  setIntentExtraBlock:((Intent)->Unit)? = null,
                  setBundleBlock: ((Bundle)->Unit)? = null,
                  flag:Int? = null,
                  requestCode:Int? = null){

    activity?:return
    val intent = AppUtils.createIntent(activity!!,
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