package com.lazyee.klib.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.hjq.permissions.XXPermissions
import com.lazyee.klib.R
import com.lazyee.klib.typed.AllGrantedCallback
import com.lazyee.klib.typed.GrantedCallback
import com.lazyee.klib.typed.DeniedCallback
import com.lazyee.klib.typed.TCallback
import com.lazyee.klib.util.AppUtils

/**
 * Author: leeorz
 * Email: 378229364@qq.com
 * Description:权限检测
 * Date: 2024/3/21 23:19
 */
@Deprecated("不推荐使用这个权限检查类，这个类目前做得很不完善", replaceWith = ReplaceWith("XXPermissions"))
class PermissionChecker {
    private var mActivity: FragmentActivity? = null
    private var mFragment: Fragment? = null
    private var mContext:Context
    private val mPermissions :MutableList<String> = mutableListOf()
    private var mGrantedCallback: GrantedCallback? = null
    private var mAllGrantedCallback:AllGrantedCallback? = null
    private var mDeniedCallback :DeniedCallback? = null

    private constructor(activity: FragmentActivity){

        mActivity = activity
        mContext = activity
    }
    private constructor(fragment: Fragment){
        mFragment = fragment
        mContext = fragment.requireActivity()
    }

    companion object{
        fun with(activity: FragmentActivity): PermissionChecker {
            return PermissionChecker(activity)
        }

        fun with(fragment: Fragment): PermissionChecker {
            return PermissionChecker(fragment)
        }

        val MANAGE_EXTERNAL_STORAGE = "android.permission.MANAGE_EXTERNAL_STORAGE"
    }

    /**
     * 打开权限设置界面，并且返回回调
     */
    fun startPermissionActivity(callback:TCallback<ActivityResult>){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

        var packageName:String? = mActivity?.packageName
        if(packageName == null){
            packageName =  mFragment?.activity?.packageName
        }

        val uri = Uri.fromParts("package",packageName,null)
        intent.data = uri

        if(mActivity != null){
            AppUtils.registerSimpleActivityResult(mActivity!!, intent, callback)
            return
        }

        if(mFragment != null){
            AppUtils.registerSimpleActivityResult(mFragment!!, intent, callback)
            return
        }

    }


    /**
     * 判断一个或多个权限是否全部授予了
     */
    fun isGranted(vararg permissions:String): Boolean {
        permissions.forEach {
            if(ContextCompat.checkSelfPermission(mContext,it) == PackageManager.PERMISSION_DENIED){
                return false
            }
        }
        return true
    }

    /**
     * 判断一个或多个权限是否全部拒绝了
     */
    fun isDenied(vararg permissions:String):Boolean{
        permissions.forEach {
            if(ContextCompat.checkSelfPermission(mContext,it) == PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

    fun permission(vararg permissions:String): PermissionChecker {
        mPermissions.clear()
        mPermissions.addAll(permissions)
        return this
    }

    fun manageExternalStoragePermission(): PermissionChecker {
        mPermissions.clear()
        if(Build.VERSION.SDK_INT >= 30){
            mPermissions.add(MANAGE_EXTERNAL_STORAGE)
        }else{
            mPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            mPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        return this
    }

    fun onAllGranted(callback: AllGrantedCallback):PermissionChecker{
        mAllGrantedCallback = callback
        return this
    }

    fun onGranted(callback:GrantedCallback): PermissionChecker {
        mGrantedCallback = callback
        return this
    }

    fun onDenied(callback: DeniedCallback): PermissionChecker {
        mDeniedCallback = callback
        return this
    }

    fun request(){
        if(mActivity != null){
            realRequest(mActivity!!.supportFragmentManager)
            return
        }

        if(mFragment != null){
            realRequest(mFragment!!.childFragmentManager)
            return
        }
    }


    private fun realRequest(fragmentManager: FragmentManager){
        val tag = "PermissionFragment"
        var targetFragment:Fragment? = fragmentManager.findFragmentByTag(tag)
        val transaction = fragmentManager.beginTransaction()
        targetFragment?.run {
            transaction.remove(this)
        }
        targetFragment = PermissionFragment(mPermissions,mAllGrantedCallback,mGrantedCallback,mDeniedCallback)
        transaction.add(targetFragment,tag)
        transaction.commitAllowingStateLoss()
    }

    /**
     * 必须设置为公开的class
     */
    class PermissionFragment(private val permissions:MutableList<String>,
                             private val allGrantedCallback: AllGrantedCallback? = null,
                             private val grantedCallback: GrantedCallback? = null,
                             private val deniedCallback: DeniedCallback? = null):Fragment(){
        private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ result->
            parentFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
            val grantedPermissionList = mutableListOf<PermissionStatus>()
            val deniedPermissionList = mutableListOf<PermissionStatus>()

            for (key in result.keys) {
                val permission = PermissionStatus(key)
                permission.isGranted = result.getValue(key)
                val doNotAskAgain = !ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),key)
                permission.doNotAskAgain = doNotAskAgain

                if(permission.isGranted){
                    grantedPermissionList.add(permission)
                }else{
                    deniedPermissionList.add(permission)
                }
            }
            if(grantedPermissionList.isNotEmpty()){
                grantedCallback?.invoke(deniedPermissionList.isEmpty(),grantedPermissionList.toTypedArray())
            }

            if(deniedPermissionList.isNotEmpty()){
                deniedCallback?.invoke(deniedPermissionList.toTypedArray())
            }

            if(deniedPermissionList.isEmpty()){
                allGrantedCallback?.invoke()
            }

        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            activityResultLauncher.launch(permissions.toTypedArray())
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            super.onCreateView(inflater, container, savedInstanceState)
            return inflater.inflate(R.layout.layout_debug_config,null,false)
        }
    }
}
