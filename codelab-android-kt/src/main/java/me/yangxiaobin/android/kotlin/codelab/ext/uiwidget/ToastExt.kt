package me.yangxiaobin.android.kotlin.codelab.ext.uiwidget

import android.content.Context
import android.widget.Toast


fun Context.showContextToast(stringResId: Int) = Toast.makeText(this, this.getString(stringResId), Toast.LENGTH_SHORT).show()


fun Context.showContextToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
