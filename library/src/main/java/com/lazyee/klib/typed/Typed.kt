package com.lazyee.klib.typed

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:自定义类型别名
 * Date: 2023/6/15 09:45
 */
typealias VoidCallback = ()->Unit
typealias IndexedCallback = (Int)->Unit
typealias TCallback<T> = (T)->Unit