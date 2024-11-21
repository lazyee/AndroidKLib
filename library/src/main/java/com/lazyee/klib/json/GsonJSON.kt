package com.lazyee.klib.json

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonJSON :JSON<Gson> {
    override val instance: Gson
        get() = Gson()

    override fun <R> toJson(entity: R?): String? {
        return instance.toJson(entity)
    }

    override fun <R> fromJson(json: String?, clazz: Class<R>): R? {
       return instance.fromJson(json,clazz)
    }

    /**
     * fromJson(jsonString,object: TypeToken<XXX<YYY>>(){})
     */
    fun <R> fromJson(json:String?,typeToken: TypeToken<R>):R{
        return instance.fromJson(json,typeToken.type)
    }
}