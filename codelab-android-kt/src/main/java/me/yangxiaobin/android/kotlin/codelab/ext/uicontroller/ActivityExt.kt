package me.yangxiaobin.android.kotlin.codelab.ext.uicontroller

import android.app.Activity
import androidx.annotation.StringRes
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.showContextToast


fun Activity.showActivityToast(text: String) = this.showContextToast(text)

fun Activity.showActivityToast(@StringRes stringResId: Int) = this.showContextToast(stringResId)

