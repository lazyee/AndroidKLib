package com.lazyee.klib.edittext.textwatcher

import android.text.Editable
import android.text.TextWatcher

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:获取编辑中添加或者删除的文字
 * Date: 2023/4/5 17:12
 */
class EditTextAddOrDeleteWatcher : TextWatcher {
    private var mEditCallback: EditCallback? = null
    private var beforeString = ""
    private var deleteCount = 0
    private var addedCount = 0
    private var editIndex = -1

    fun setEditCallback(callback: EditCallback?){
        mEditCallback = callback
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeString = s.toString()
        deleteCount = 0
        addedCount = 0
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        editIndex = start
        deleteCount = before
        addedCount = count
    }

    override fun afterTextChanged(s: Editable?) {
        if(deleteCount > 0){//删除的文字
            val deletedText = beforeString.substring(editIndex,editIndex + deleteCount)
            mEditCallback?.onTextDeleted(editIndex,deletedText)
        }

        if(addedCount > 0){//添加的文字
            val addedText = s.toString().substring(editIndex,editIndex + addedCount)
            mEditCallback?.onTextAdded(editIndex,addedText)
        }
    }

    interface EditCallback{
        fun onTextAdded(index:Int,addedText:String)
        fun onTextDeleted(index:Int,deletedText:String)
    }

}