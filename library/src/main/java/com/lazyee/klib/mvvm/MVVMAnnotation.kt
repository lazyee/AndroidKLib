package com.lazyee.klib.mvvm

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
