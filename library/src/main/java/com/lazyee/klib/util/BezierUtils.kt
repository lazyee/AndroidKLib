package com.lazyee.klib.util

import android.graphics.PointF
import kotlin.math.pow

/**
 * ClassName: BezierUtils
 * Description:贝塞尔曲线工具类，https://cubic-bezier.com/
 * Date: 2025/5/23 14:33
 * @author Leeorz
 */
object BezierUtils {
    /**
     * 二阶贝塞尔曲线公式（3个点）
     * t:t ∈ [0, 1]
     * startPoint:起始点
     * controlPoint:控制点
     * endPoint:结束点
     */
    fun getQuadraticBezierPoint(t: Float, startPoint: PointF, controlPoint: PointF, endPoint: PointF): PointF {
        val x = (1 - t).pow(2) * startPoint.x +
                2 * (1 - t) * t * controlPoint.x +
                t.pow(2) * endPoint.x
        val y = (1 - t).pow(2) * startPoint.y +
                2 * (1 - t) * t * controlPoint.y +
                t.pow(2) * endPoint.y
        return PointF(x, y)
    }

    /**
     * 三阶贝塞尔曲线公式（4个点）
     * t:t ∈ [0, 1]
     * startPoint:起始点
     * controlPoint1:控制点1
     * controlPoint2:控制点2
     * endPoint:结束点
     */
    fun getCubicBezierPoint(t: Float, startPoint: PointF, controlPoint1: PointF, controlPoint2: PointF, endPoint: PointF): PointF {
        val u = 1 - t
        val tt = t * t
        val uu = u * u
        val uuu = uu * u
        val ttt = tt * t

        val x = uuu * startPoint.x +
                3 * uu * t * controlPoint1.x +
                3 * u * tt * controlPoint2.x +
                ttt * endPoint.x

        val y = uuu * startPoint.y +
                3 * uu * t * controlPoint1.y +
                3 * u * tt * controlPoint2.y +
                ttt * endPoint.y

        return PointF(x, y)
    }
}