package com.lazyee.app

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lazyee.app.databinding.ActivityMainBinding
import com.lazyee.klib.annotation.ViewModel
import com.lazyee.klib.base.ViewBindingActivity
import com.lazyee.klib.extension.goto
import com.lazyee.klib.extension.setSingleClick
import com.lazyee.klib.listener.OnKeyboardVisibleListener
import com.lazyee.klib.util.AppUtils
import com.lazyee.klib.util.LogUtils
import java.io.File

/**
 * @Author leeorz
 * @Date 3/30/21-5:53 PM
 * @Description:
 */
class MainActivity: ViewBindingActivity<ActivityMainBinding>(),OnKeyboardVisibleListener{
    @get:ViewModel
    private val mViewModel by lazy { ViewModelProvider(this).get(TestViewModel::class.java) }

    override fun initView() {
        super.initView()
        addOnKeyboardVisibleListener(this)

        LogUtils.e(TAG, "hasNewVersion:${AppUtils.hasNewVersion("V1.3.1","V1.3")}")

        mViewBinding.touchCallbackLayout.addTouchDownCallback { LogUtils.e(TAG,"onTouchDown") }

        mViewBinding.btnSingleClick.setSingleClick {
            goto(SecondActivity::class.java)
        }
        mViewBinding.btnInstallApk.setOnClickListener {
//            AppUtils.installApk(this,File(filesDir.absolutePath + File.separator + "base.apk"))
            AppUtils.installApk(this,"com.lazyee.app.provider.FileProvider",File(filesDir.absolutePath + File.separator + "base.apk"))
        }

        mViewBinding.btnShareFile.setOnClickListener {
            AppUtils.shareFile(this@MainActivity,"com.lazyee.app.provider.FileProvider", File(filesDir.absolutePath + File.separator + "2023_09_23_10_17_19.txt"))
        }
    }

    override fun onSoftKeyboardShow(keyboardHeight: Int) {
        LogUtils.e(TAG,"onSoftKeyboardShow keyboardHeight:$keyboardHeight")
    }

    override fun onSoftKeyboardHide() {
        LogUtils.e(TAG,"onSoftKeyboardHide")
    }
}