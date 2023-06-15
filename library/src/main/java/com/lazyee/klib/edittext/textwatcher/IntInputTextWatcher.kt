package com.lazyee.klib.edittext.textwatcher

import android.text.Editable
import android.text.TextWatcher

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:需要设置EditText的digits为1234567890
 * Date: 2023/6/15 10:54
 */
class IntInputTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


    }

    override fun afterTextChanged(s: Editable?) {
        s?:return
        if(s.length > 1 && s.startsWith('0')) {
            val result = s.trimStart('0')
            s.clear()
            s.append(result)
        }
    }
}