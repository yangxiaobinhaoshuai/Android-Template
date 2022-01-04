package me.yangxiaobin.android.kotlin.codelab.ext

import android.content.Context
import android.widget.Toast


fun Context.showToast(stringResId: Int) =
    Toast.makeText(this, this.getString(stringResId), Toast.LENGTH_SHORT).show()


fun Context.showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
