package com.lazyee.klib.typed

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:自定义方法别名
 * Date: 2023/6/15 09:45
 */
typealias ValueGetter<T> = () -> T
typealias ValueSetter<T> = (T) -> Unit
typealias ValueChanged<T> = (T) -> Unit

typealias VoidCallback = () -> Unit
typealias IndexedCallback = (Int) -> Unit
typealias TCallback<T> = (T) -> Unit
typealias TCallback2<T1,T2> = (T1,T2) -> Unit
typealias TCallback3<T1,T2,T3> = (T1,T2,T3) -> Unit
typealias TCallback4<T1,T2,T3,T4> = (T1,T2,T3,T4) -> Unit
typealias TCallback5<T1,T2,T3,T4,T5> = (T1,T2,T3,T4,T5) -> Unit