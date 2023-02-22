package me.yangxiaobin.android.kotlin.codelab.ext.uicontroller

import android.app.Activity
import androidx.annotation.StringRes
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.showContextToast


public fun Activity.showActivityToast(text: String) = this.showContextToast(text)

public fun Activity.showActivityToast(@StringRes stringResId: Int) = this.showContextToast(stringResId)

