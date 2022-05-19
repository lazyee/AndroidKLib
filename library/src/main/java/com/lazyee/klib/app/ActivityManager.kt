package com.lazyee.klib.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*
import kotlin.reflect.KClass

/**
 * @Author leeorz
 * @Date 2020/11/3-2:51 PM
 * @Description:手动管理的Activity Stack,监听activity生命周期
 */
@SuppressLint("StaticFieldLeak")
object ActivityManager{
    private val activityLifecycleCallbacks :ActivityLifecycleCallbacks = ActivityLifecycleCallbacks()

    /**
     * 注册生命周期监听
     */
    fun registerActivityLifecycleCallbacks(application: Application){
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }
    var foregroundActivity:Activity? = null

    private val activityList:MutableList<Activity> = mutableListOf()

    /**
     * 获取最后的activity实例
     * @return Activity?
     */
    fun lastActivity(): Activity? = activityList.lastOrNull()
    val last:Activity?
        get() = activityList.lastOrNull()

    /**
     * 添加activity
     * @param activity Activity
     */
    fun add(activity: Activity){
        activityList.add(activity)
    }

    /**
     * 移除
     * @param activityArr Array<out Activity>
     */
    fun remove(vararg activityArr: Activity){
        activityArr.forEach { activityList.remove(it) }
    }

    /**
     * 根据ClassName移除
     * @param classNameArr Array<out String> simpleName
     */
    fun removeByClassName(vararg classNameArr:String){
        classNameArr.forEach {className:String->
            activityList.removeAll { it::class.java.simpleName == className }
        }
    }

    /**
     * 返回指定页面
     * @param activity Activity
     */
    fun backTo(clazz:Class<Activity>){
        backTo(clazz.simpleName)
    }
    /**
     * 返回指定页面
     * @param activitySimpleName String
     */
    fun backTo(activitySimpleName:String){
        val reversedList = activityList.reversed()
        for(act in reversedList){
            if(act.javaClass.simpleName == activitySimpleName){
                break
            }
            finish(act);
        }
    }

    /**
     * 结束指定Activity
     * @param activity Activity
     */
    fun finish(activity: Activity){
        if (!activity.isFinishing && !activity.isDestroyed) {
            activity.finish()
        }
        remove(activity)
    }

    /**
     * 结束指定的Activity
     * @param activitySimpleName String
     */
    fun finish(activitySimpleName: String){
        val targetActivity = activityList.find { it.javaClass.simpleName == activitySimpleName } ?: return
        finish(targetActivity)
    }

    /**
     * 结束其他所有的 Activity
     * @param acts Activity vararg
     */
    fun finishAllExcept(vararg activityArr: Class<Activity>?){
        val newActList = ArrayList(activityList)
        for (act in newActList){
            if(activityArr.find { it?.simpleName == act.javaClass.simpleName } == null){
                continue
            }else{
                finish(act)
            }
        }
    }

    /**
     * 结束所有的 Activity
     */
    fun finishAll(){
        finishAllExcept()
    }

    private class ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks{
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            add(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            foregroundActivity = activity
        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {
            if(foregroundActivity == activity){
                foregroundActivity = null
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            remove(activity)
        }
    }
}