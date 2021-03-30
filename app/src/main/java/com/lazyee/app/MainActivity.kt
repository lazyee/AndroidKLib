package com.lazyee.app

import android.os.Bundle
import com.lazyee.app.databinding.ActivityMainBinding
import com.lazyee.klib.base.BaseActivity
import com.lazyee.klib.extension.goto
import com.lazyee.klib.extension.setSingleClick

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
    }
}