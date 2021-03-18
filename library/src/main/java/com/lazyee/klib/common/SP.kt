package com.lazyee.klib.common

import android.content.Context
import com.lazyee.klib.util.LogUtils

/**
 * @Author leeorz
 * @Date 2020/10/30-11:46 AM
 * @Description:读写sp的工具类
 */
class SP constructor(context: Context, name:String? = null,mode:Int? = null) {
    private val  TAG = "[SP]"
    private val defaultName = "default"
    private val sharedPreferences by lazy { context.getSharedPreferences(name ?: defaultName,mode?:Context.MODE_PRIVATE) }

    /**
     * 储存基本数据类型到sp
     * @param key String 关键字
     * @param value Any? 值，任意类型，但是实际这个方法只支持String,Boolean,Int,Long,Float,如果传入这个几种类型以外的类型，将不会执行写入操作
     */
    fun put(key:String,value:Any?){

        LogUtils.e(TAG,"key:$key,value:$value")
        if (value == null){
            removeByKey(key)
            return
        }
        val edit = sharedPreferences.edit()
        when (value) {
            is String -> {
                edit.putString(key,value).apply()
            }
            is Boolean -> {
                edit.putBoolean(key,value).apply()
            }
            is Int -> {
                edit.putInt(key,value).apply()
            }
            is Long -> {
                edit.putLong(key,value).apply()
            }
            is Float -> {
                edit.putFloat(key,value).apply()
            }
        }
    }

    fun putStringSet(key:String,values:Set<String>?){
        sharedPreferences.edit().putStringSet(key,values).apply()
    }

    fun removeByKey(key:String){
        sharedPreferences.edit().remove(key).apply()
    }

    fun string(key:String):String? {
        return get { sharedPreferences.getString(key,null) }
    }

    fun stringSet(key: String):Set<String>?{
        return get { sharedPreferences.getStringSet(key,null) }
    }

    fun int(key:String):Int{
        return get { sharedPreferences.getInt(key,0) } ?: 0
    }

    fun boolean(key:String):Boolean{
        return get { sharedPreferences.getBoolean(key,false) } ?: false
    }

    fun float(key:String):Float{
        return get { sharedPreferences.getFloat(key,0f) } ?: 0f
    }

    fun long(key:String):Long{
        return get { sharedPreferences.getLong(key,0L) } ?: 0L
    }

    private fun <T> get(block:()->T):T?{
        try {
            return block()
        }catch (e:ClassCastException){
            e.printStackTrace()
        }
        return null
    }
}