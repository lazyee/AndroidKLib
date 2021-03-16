package com.lazyee.klib.fix

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import com.gyf.immersionbar.ImmersionBar
import com.lazyee.klib.extension.screenWidth
import com.lazyee.klib.util.AppUtils.isLandscape

class AndroidBug5497Workaround private constructor(private val activity: Activity) {
    private val mChildOfContent: View
    private var usableHeightPrevious = 0
    private val frameLayoutParams: FrameLayout.LayoutParams

    fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()
        if (isLandscape(activity)) {
            frameLayoutParams.height = mChildOfContent.rootView.height
            frameLayoutParams.width = mChildOfContent.rootView.width - ImmersionBar.getNavigationBarHeight(activity)
            mChildOfContent.requestLayout()
            return
        }
        if (usableHeightNow != usableHeightPrevious) {
            val usableHeightSansKeyboard = mChildOfContent.rootView.height - ImmersionBar.getNavigationBarHeight(activity)
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            if (heightDifference > usableHeightSansKeyboard / 4) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard
            }
            frameLayoutParams.width = activity.screenWidth
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top
    }

    companion object {
        // For more information, see https://code.google.com/p/android/issues/detail?id=5497
        // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
        fun assistActivity(activity: Activity): AndroidBug5497Workaround {
            return AndroidBug5497Workaround(activity)
        }
    }

    init {
        val content = activity.findViewById<View>(android.R.id.content) as FrameLayout
        mChildOfContent = content.getChildAt(0)
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener { possiblyResizeChildOfContent() }
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    }
}