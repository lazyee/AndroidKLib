package com.lazyee.klib.recyclerview.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:
 * Date: 2022/7/20 15:28
 */
class LinearSpacingItemDecoration(
    private val top: Int = 0,
    private val bottom: Int = 0,
    private val left: Int = 0,
    private val right: Int = 0
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.layoutManager !is LinearLayoutManager) throw Exception("只支持LinearLayoutManager")
        val linearLayoutManager = parent.layoutManager as LinearLayoutManager
        if (linearLayoutManager.orientation == RecyclerView.VERTICAL) {
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.top = top
            }
            if (parent.getChildAdapterPosition(view) != parent.childCount - 1) {
                outRect.bottom = bottom
            }
            outRect.left = left
            outRect.right = right
        } else {
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.left = left
            }

            if (parent.getChildAdapterPosition(view) != parent.childCount - 1) {
                outRect.right = right
            }
            outRect.top = top
            outRect.bottom = bottom
        }
    }
}