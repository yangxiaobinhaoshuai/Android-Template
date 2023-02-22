package me.yangxiaobin.android.kotlin.codelab.ext.uiwidget

import android.content.Context
import android.widget.Toast


public fun Context.showContextToast(stringResId: Int) = Toast.makeText(this, this.getString(stringResId), Toast.LENGTH_SHORT).show()


public fun Context.showContextToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
