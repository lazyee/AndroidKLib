package com.lazyee.app

import com.lazyee.klib.extension.toDisplayPrice
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)

        println(9.toDisplayPrice())
        println(9.4.toDisplayPrice())
        println(9.8.toDisplayPrice())
        println(9.8889.toDisplayPrice())
        println(9.0f.toDisplayPrice())
        println(9.2f.toDisplayPrice())
        println(9.653f.toDisplayPrice())
        println(9.658f.toDisplayPrice())
    }
}