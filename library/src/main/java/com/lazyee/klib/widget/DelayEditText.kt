package com.lazyee.klib.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.lazyee.klib.typed.ValueChanged
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * 延迟输入回调的EditText
 */
private const val defaultDelayTimeMilliSeconds:Long = 300L//默认回调延时
open class DelayEditText : AppCompatEditText , TextWatcher {
    private var mDisposable: Disposable ? = null
    private var mPublishSubject: PublishSubject<String>? = null
    private var mOnDelayTextCallback:ValueChanged<String>? = null

    constructor(context: Context):super(context,null)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,android.R.attr.editTextStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr:Int):super(context,attrs,defStyleAttr)

    fun setOnDelayTextChangedCallback(callback: ValueChanged<String>?){
        setOnDelayTextChangedCallback(defaultDelayTimeMilliSeconds,callback)
    }

    fun setOnDelayTextChangedCallback(delayTimeMilliSeconds:Long,callback: ValueChanged<String>?){
        removeTextChangedListener(this)
        addTextChangedListener(this)
        mOnDelayTextCallback = callback
        if(mOnDelayTextCallback == null){
            mDisposable?.dispose()
        }else{
            mPublishSubject = PublishSubject.create()
            mDisposable = mPublishSubject!!.distinctUntilChanged()
                .debounce(delayTimeMilliSeconds,TimeUnit.MILLISECONDS)
                .subscribe {
                    mOnDelayTextCallback?.invoke(it)
                }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        text?:return
        mPublishSubject?.onNext(text.toString())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mDisposable?.dispose()
        mDisposable = null
    }
}