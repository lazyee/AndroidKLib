package com.lazyee.klib.annotation

import androidx.lifecycle.ViewModelProvider


/*******************注意！！注意！！注意！！*******************
 *  在kotlin中需要用@get:set:field:之类的关键字修饰
 *  @get:ViewModel
 *  private val mBaiduViewModel by lazy { ViewModelProvider(this).get(BaiduViewModel::class.java) }
 *  @field:ViewModel
 *  private lateinit var mBaiduViewModel2:BaiduViewModel
 *  ********************************************************* */


/**
 * 在kotlin中需要用@get:set:field:之类的关键字修饰
 * example: @get:ViewModel
 */
@Target(AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModel

/**
 * 在kotlin中需要用@get:set:field:之类的关键字修饰
 * example: @get:Repository
 */
@Target(AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Repository
