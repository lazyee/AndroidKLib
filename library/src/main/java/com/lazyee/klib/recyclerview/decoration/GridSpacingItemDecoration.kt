package com.lazyee.klib.recyclerview.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:添加RecyclerView网格间距
 * Date: 2023/5/19 17:22
 */
class GridSpacingItemDecoration : RecyclerView.ItemDecoration{

    private var horizontalSpacing:Float = 0f
    private var verticalSpacing:Float = 0f
    private val paint : Paint = Paint()

    constructor(spacing: Float):this(spacing,null)
    constructor(spacing:Float,color:Int? = null):this(spacing,spacing,color)
    constructor(horizontalSpacing:Float,verticalSpacing:Float):this(horizontalSpacing,verticalSpacing,null)
    constructor(horizontalSpacing:Float,verticalSpacing:Float, color: Int? = null){
        this.horizontalSpacing = horizontalSpacing
        this.verticalSpacing = verticalSpacing
        paint.color = color ?: Color.TRANSPARENT
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager !is GridLayoutManager) throw Exception("只支持GridLayoutManager")
        val gridLayoutManager = (parent.layoutManager as GridLayoutManager)
        val position = gridLayoutManager.getPosition(view)

        /**
         * 一行有5个Item, 假设间隔是10，在不设置外边距的情况下：
         *  第1个Item :  left:0,right:8,第一个left取0
         *  第2个Item :  left:2,right:6
         *  第3个Item :  left:4,right:4
         *  第4个Item :  left:6,right:2
         *  第5个Item :  left:8,right:0,最后一个right取0
         *
         *  可以发现，right 和 left 相加都是10
         *
         */
        val spanCount = gridLayoutManager.spanCount
        val column: Int = position % gridLayoutManager.spanCount
        outRect.left = (column * horizontalSpacing / spanCount).toInt()
        outRect.right = (horizontalSpacing - (column + 1) * horizontalSpacing / spanCount).toInt()

        outRect.bottom = if(isLastLine(position,gridLayoutManager,parent)) 0 else verticalSpacing.toInt()
    }

    private fun isLineEnd(position:Int,layoutManager: GridLayoutManager):Boolean{
        return position % layoutManager.spanCount + 1 == layoutManager.spanCount
    }

    private fun isLastLine(position:Int, layoutManager: GridLayoutManager, recyclerView: RecyclerView):Boolean{
        Log.e("TAG","isLastLine:position:${position}")
        Log.e("TAG","isLastLine:recyclerView.childCount:${recyclerView.childCount}")
        Log.e("TAG","isLastLine:recyclerView.itemCount:${recyclerView.adapter?.itemCount}")
//        return  ceil((position + 1) / (layoutManager.spanCount * 1.0)) == ceil (recyclerView.childCount / (layoutManager.spanCount * 1.0))
//        return position + 1 >= (recyclerView.adapter?.itemCount?:0) - layoutManager.spanCount
        var itemCount = recyclerView.adapter?.itemCount?:0
        var lastLineCount = itemCount % layoutManager.spanCount
        if(lastLineCount == 0){
            lastLineCount = layoutManager.spanCount
        }
        return itemCount - position - 1 < lastLineCount
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager
        val childCount = parent.childCount
        if(childCount - 1 == 0)return
        (0 until childCount).forEach { position ->
            val childView = parent.getChildAt(position)

            childView.let {
                c.drawRect(it.right.toFloat() ,
                    it.top.toFloat(),
                    it.right.toFloat() + if(isLineEnd(position,gridLayoutManager)) 0f  else horizontalSpacing,
                    it.bottom.toFloat(),paint)

                c.drawRect(it.left.toFloat() ,
                    it.bottom.toFloat(),
                    it.right.toFloat() + horizontalSpacing,
                    it.bottom.toFloat() + if(isLastLine(position,gridLayoutManager,parent))0f else verticalSpacing,paint)
            }

        }
    }

}