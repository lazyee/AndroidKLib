package com.lazyee.klib.recyclerview.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
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
class GridSpacingItemDecoration(private val space:Float,private val color:Int = Color.TRANSPARENT) : RecyclerView.ItemDecoration(){
    private var paint : Paint = Paint()
    init {
        paint.color = color
    }
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//            super.getItemOffsets(outRect, view, parent, state)
        if (parent.layoutManager !is GridLayoutManager) throw Exception("只支持GridLayoutManager")
        val gridLayoutManager = (parent.layoutManager as GridLayoutManager)
        val position = gridLayoutManager.getPosition(view)
        outRect.bottom = if(isLastLine(position,gridLayoutManager,parent)) 0 else space.toInt()
        outRect.right = if(isLineEnd(position,gridLayoutManager)) 0 else space.toInt()
    }

    private fun isLineEnd(position:Int,layoutManager: GridLayoutManager):Boolean{
        return position % layoutManager.spanCount + 1 == layoutManager.spanCount
    }

    private fun isLastLine(position:Int, layoutManager: GridLayoutManager, recyclerView: RecyclerView):Boolean{
        return  ceil(position / layoutManager.spanCount * 1.0) == ceil (recyclerView.adapter!!.itemCount / layoutManager.spanCount * 1.0)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager
        val childCount = parent.childCount
        if(childCount - 1 == 0)return
        (0 until childCount - 1).forEach { position ->
            val childView = parent.getChildAt(position)

            childView.run {
                c.drawRect(right.toFloat() ,
                    top.toFloat(),
                    right.toFloat() + if(isLineEnd(position,gridLayoutManager)) 0f  else space,
                    bottom.toFloat(),paint)

                c.drawRect(left.toFloat() ,
                    bottom.toFloat(),
                    right.toFloat() + space,
                    bottom.toFloat() + if(isLastLine(position,gridLayoutManager,parent))0f else space,paint)
            }

        }
    }

}