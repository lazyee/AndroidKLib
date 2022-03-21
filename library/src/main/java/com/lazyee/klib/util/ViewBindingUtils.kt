package com.lazyee.klib.util

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description: ViewBinding工具类
 * Date: 2022/3/21 3:50 下午
 */
object ViewBindingUtils {
    private fun <T:ViewBinding> getClass(clazz: Class<Any>): Class<T> {
        val type = clazz.genericSuperclass as ParameterizedType
        return type.actualTypeArguments[0] as Class<T>

    }
    fun <T:ViewBinding> getViewBinding(clazz: Class<Any>, layoutInflater: LayoutInflater):T{
        lateinit var binding:T

        val method = getClass<T>(clazz).getMethod("inflate",LayoutInflater::class.java)
        binding = method.invoke(null,layoutInflater) as T
        return binding
    }

    fun <T:ViewBinding> getViewBinding(clazz: Class<Any>, layoutInflater: LayoutInflater,viewGroup: ViewGroup?,isAttachToRoot:Boolean):T{
        lateinit var binding:T
        val method = getClass<T>(clazz).getMethod("inflate",LayoutInflater::class.java,ViewGroup::class.java,Boolean::class.java)
        binding = method.invoke(null,layoutInflater,viewGroup,isAttachToRoot) as T
        return binding
    }
}