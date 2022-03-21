package com.lazyee.klib.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

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
    fun current(): Activity? = activityList.lastOrNull()

    /**
     * 添加activity
     * @param activity Activity
     */
    fun add(activity: Activity){
        activityList.add(activity)
    }

    /**
     * 根据索引返回activity 如果为负数，则从尾部开始获取，比如 -1 就是倒数第二个
     * @param index
     * * @return Activity?
     */
    fun byIndex(index:Int):Activity?{
        if(index < 0){
            val newIndex = activityList.size - 1 + index
            if (newIndex < 0 || newIndex > activityList.size - 1){
                return null
            }
            return activityList[newIndex]
        }

        if(index > activityList.size - 1){
            return null
        }
        return activityList[index]
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
    fun goBack(activity: Activity){
        for (index in activityList.count() - 1 downTo 0){
            if (activityList[index] != activity){
                finishActivity(activityList[index])
            }else{
                break
            }
        }
    }

    /**
     * 结束指定Activity
     * @param activity Activity
     */
    fun finishActivity(activity: Activity){
        if (!activity.isFinishing && !activity.isDestroyed) {
            activity.finish()
        }
        remove(activity)
    }

    /**
     * 结束其他所有的 Activity
     * @param activity Activity
     */
    fun finishOtherActivity(activity: Activity?){
        val iterator = activityList.iterator()
        while (iterator.hasNext()){
            val act = iterator.next()
            if(act == activity){
                break
            }else{
                finishActivity(act)
                iterator.remove()
            }
        }
    }

    /**
     * 结束所有的 Activity
     * @param activity Activity
     */
    fun finishAllActivity(){
        finishOtherActivity(null)
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