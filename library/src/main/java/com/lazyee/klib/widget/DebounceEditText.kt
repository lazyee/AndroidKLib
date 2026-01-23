package com.lazyee.klib.widget

import android.R
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.lazyee.klib.typed.ValueChanged
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * 防抖EditText
 */
private const val DEFAULT_DEBOUNCE_INTERVAL_MS: Long = 300L

class DebounceEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var debounceDisposable: Disposable? = null
    private val textSubject = PublishSubject.create<String>()

    private var onDebounceTextChangeCallback: ValueChanged<String>? = null

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textSubject.onNext(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun setOnDebounceTextChangedCallback(callback: ValueChanged<String>?){
        setOnDebounceTextChangedCallback(DEFAULT_DEBOUNCE_INTERVAL_MS,callback)
    }

    fun setOnDebounceTextChangedCallback(
        intervalMs: Long = DEFAULT_DEBOUNCE_INTERVAL_MS,
        callback: ValueChanged<String>?
    ) {
        this.onDebounceTextChangeCallback = callback

        debounceDisposable?.dispose()
        debounceDisposable = textSubject
            .debounce(intervalMs, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { text ->
                onDebounceTextChangeCallback?.invoke(text)
            }
    }

    override fun onDetachedFromWindow() {
        debounceDisposable?.dispose()
        super.onDetachedFromWindow()
    }
}