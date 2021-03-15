package com.lazyee.klib.app

import android.app.Activity

/**
 * @Author leeorz
 * @Date 2020/11/3-2:51 PM
 * @Description:手动管理的Activity Stack
 */
object ActivityStack{
    private val stack:MutableList<Activity> = mutableListOf()

    /**
     * 获取最后的activity实例
     * @return Activity?
     */
    fun current(): Activity?{
        if (stack.isEmpty())return null
        return stack.last()
    }
    /**
     * 添加activity
     * @param activity Activity
     */
    fun add(activity: Activity){
        stack.add(activity)
    }

    /**
     * 根据索引返回activity 如果为负数，则从尾部开始获取，比如 -1 就是倒数第二个
     * @param index
     * * @return Activity?
     */
    fun byIndex(index:Int):Activity?{
        if(index < 0){
            val newIndex = stack.size - 1 + index
            if (newIndex < 0 || newIndex > stack.size - 1){
                return null
            }
            return stack[newIndex]
        }

        if(index > stack.size - 1){
            return null
        }
        return stack[index]
    }

    /**
     * 移除
     * @param activityArr Array<out Activity>
     */
    fun remove(vararg activityArr: Activity){
        activityArr.forEach { stack.remove(it) }
    }

    /**
     * 根据ClassName移除
     * @param classNameArr Array<out String> simpleName
     */
    fun removeByClassName(vararg classNameArr:String){
        classNameArr.forEach {className:String->
            stack.removeAll { it::class.java.simpleName == className }
        }
    }

    /**
     * 返回指定页面
     * @param activity Activity
     */
    fun goBack(activity: Activity){
        for (index in stack.count() - 1 downTo 0){
            if (stack[index] != activity){
                stack[index].finish()
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
        activity.finish()
        remove(activity)
    }

    /**
     * 结束其他所有的 Activity
     * @param activity Activity
     */
    fun finishOtherActivity(activity: Activity?){
        val iterator = stack.iterator()
        while (iterator.hasNext()){
            val act = iterator.next()
            if(act == activity){
                break
            }else{
                act.finish()
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
}