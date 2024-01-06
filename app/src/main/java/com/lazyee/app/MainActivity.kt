package com.lazyee.app

import android.os.Bundle
import com.lazyee.app.databinding.ActivityMainBinding
import com.lazyee.klib.base.BaseActivity
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
class MainActivity: BaseActivity(),OnKeyboardVisibleListener{
    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        addOnKeyboardVisibleListener(this)

        LogUtils.e(TAG, "hasNewVersion:${AppUtils.hasNewVersion("V1.3.1","V1.3")}")
        mBinding.btnSingleClick.setSingleClick {
            goto(SecondActivity::class.java)
        }
        mBinding.btnInstallApk.setOnClickListener {
//            AppUtils.installApk(this,File(filesDir.absolutePath + File.separator + "base.apk"))
            AppUtils.installApk(this,"com.lazyee.app.provider.FileProvider",File(filesDir.absolutePath + File.separator + "base.apk"))
        }

        mBinding.btnShareFile.setOnClickListener {
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