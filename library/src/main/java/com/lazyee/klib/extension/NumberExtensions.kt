package com.lazyee.klib.extension

import android.content.res.Resources
import android.util.TypedValue
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

import java.util.Locale





/**
 * dp转px
 */
fun Float.dp2px():Float{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
}
/**
 * dp转px
 */
fun Double.dp2px():Double{
    val density: Float = Resources.getSystem().displayMetrics.density
    return this * density
}
/**
 * dp转px
 */
fun Int.dp2px():Int{
    val density: Float = Resources.getSystem().displayMetrics.density
    return (this * density + 0.5f).toInt()
}

/**
 * px转dp
 */
fun Float.px2dp(): Float {
    val density = Resources.getSystem().displayMetrics.density
    return this / density + 0.5f
}

/**
 * px转dp
 */
fun Int.px2dp(): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (this / density + 0.5f).toInt()
}

/**
 * px转dp
 */
fun Double.px2dp(): Double {
    val density = Resources.getSystem().displayMetrics.density
    return (this / density + 0.5f)
}

fun Int.px2sp(): Int {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return (this / scaledDensity + 0.5f).toInt()
}

fun Float.px2sp(): Float {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return this / scaledDensity
}
fun Double.px2sp(): Double {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return this / scaledDensity
}

fun Int.sp2px():Int{
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return (this * scaledDensity + 0.5f).toInt()
}

fun Double.sp2px():Double{
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return this * scaledDensity
}

fun Float.sp2px():Float{
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return this * scaledDensity
}

fun Number?.toDisplayPrice():String{
    this?:return ""
    val number = if(this is Float) (this * 100).toInt() / 100.0 else this.toDouble()
    val df = DecimalFormat(if (number % 1.0 > 0) "#,##0.00" else "#,###")
    df.roundingMode = RoundingMode.FLOOR
    return df.format(number)

}