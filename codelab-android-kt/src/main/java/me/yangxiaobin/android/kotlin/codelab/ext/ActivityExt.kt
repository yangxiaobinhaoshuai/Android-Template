package me.yangxiaobin.android.kotlin.codelab.ext

import android.app.Activity
import androidx.annotation.StringRes


fun Activity.showActivityToast(text: String) = this.showContextToast(text)

fun Activity.showActivityToast(@StringRes stringResId: Int) = this.showContextToast(stringResId)
