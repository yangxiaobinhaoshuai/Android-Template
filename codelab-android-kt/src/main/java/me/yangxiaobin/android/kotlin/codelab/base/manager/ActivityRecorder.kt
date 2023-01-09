package me.yangxiaobin.android.kotlin.codelab.base.manager

import android.app.Activity
import java.util.LinkedList

object ActivityRecorder {

    public val activities: LinkedList<Activity> by lazy { LinkedList<Activity>() }

    val topActivity: Activity get() = activities.last

    fun add(activity: Activity) {
        activities += activity
    }

    fun remove(activity: Activity) {
        activities -= activity
    }

}