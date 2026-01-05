package com.lazyee.klib.extension

import android.content.res.Resources
import android.util.TypedValue
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

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
val Float.dp2px: Float get() = dp2px()

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
val Double.dp2px: Double get() = dp2px()

/**
 * dp转px
 */
fun Int.dp2px():Int{
    val density: Float = Resources.getSystem().displayMetrics.density
    return (this * density + 0.5f).toInt()
}

/**
 * dp转px
 */
val Int.dp2px: Int get() = dp2px()

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
val Float.px2dp: Float get() = px2dp()

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
val Int.px2dp: Int get() = px2dp()

/**
 * px转dp
 */
fun Double.px2dp(): Double {
    val density = Resources.getSystem().displayMetrics.density
    return (this / density + 0.5f)
}

/**
 * px转dp
 */
val Double.px2dp: Double get() = px2dp()

/**
 * px转sp
 */
fun Int.px2sp(): Int {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return (this / scaledDensity + 0.5f).toInt()
}

/**
 * px转sp
 */
val Int.px2sp: Int get() = px2sp()

/**
 * px转sp
 */
fun Float.px2sp(): Float {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return this / scaledDensity
}

/**
 * px转sp
 */
val Float.px2sp: Float get() = px2sp()

/**
 * px转sp
 */
fun Double.px2sp(): Double {
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return this / scaledDensity
}

/**
 * px转sp
 */
val Double.px2sp: Double get() = px2sp()

/**
 * sp2px
 */
fun Int.sp2px():Int{
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return (this * scaledDensity + 0.5f).toInt()
}

/**
 * sp2px
 */
val Int.sp2px: Int get() = sp2px()

/**
 * sp2px
 */
fun Double.sp2px():Double{
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return this * scaledDensity
}

/**
 * sp2px
 */
fun Float.sp2px():Float{
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    return this * scaledDensity
}

/**
 * sp2px
 */
val Float.sp2px: Float get() = sp2px()

fun Number?.toDisplayPrice(groupingSeparator: Char = ',', decimalSeparator: Char = '.'):String{
    this?:return ""
    val number = if(this is Float) (this * 100).toInt() / 100.0 else this.toDouble()
    val pattern = if (number % 1.0 > 0) "#,##0.00" else "#,###"
    val symbols = DecimalFormatSymbols().apply {
        this.groupingSeparator = groupingSeparator
        this.decimalSeparator = decimalSeparator
    }
    val df = DecimalFormat(pattern, symbols)
    df.roundingMode = RoundingMode.FLOOR
    return df.format(number)
}