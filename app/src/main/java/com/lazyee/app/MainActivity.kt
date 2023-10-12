package com.lazyee.app

import android.os.Bundle
import com.lazyee.app.databinding.ActivityMainBinding
import com.lazyee.klib.base.BaseActivity
import com.lazyee.klib.extension.goto
import com.lazyee.klib.extension.setSingleClick
import com.lazyee.klib.util.AppUtils
import java.io.File

/**
 * @Author leeorz
 * @Date 3/30/21-5:53 PM
 * @Description:
 */
class MainActivity: BaseActivity(){
    private val mBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        mBinding.btnSingleClick.setSingleClick {
            goto(SecondActivity::class.java)
        }

        mBinding.btnShareFile.setOnClickListener {
            AppUtils.shareFile(this@MainActivity,"com.lazyee.app.provider.FileProvider", File(filesDir.absolutePath + File.separator + "2023_09_23_10_17_19.txt"))
        }

    }
}