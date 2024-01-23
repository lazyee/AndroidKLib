package com.lazyee.klib.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import java.lang.Exception

private const val DEFAULT_DELAY_DURATION:Long = 200L
open class DelayDialog : Dialog {

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int = 0) : super(context, themeResId)
    constructor(
        context: Context,
        cancelable: Boolean = true,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    private var isDelayEnd = true

    private var delayDuration: Long = DEFAULT_DELAY_DURATION


    fun setDelayDuration(millis: Long) {
        delayDuration = millis
    }

    override fun show() {
        if (isShowing) return
        if (!isDelayEnd) return
        isDelayEnd = false
        Handler().postDelayed({
            try {
                if(!isShowing && !isDelayEnd){
                    super.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isDelayEnd = true
            }
        }, delayDuration)
    }

    override fun dismiss() {
        super.dismiss()
        isDelayEnd = true
    }
}