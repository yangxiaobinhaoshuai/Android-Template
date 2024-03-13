package me.yangxiaobin.android.kotlin.codelab.ext.uicontroller

import android.app.Activity
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.showContextToast


public fun Activity.showActivityToast(text: String) = this.showContextToast(text)

public fun Activity.showActivityToast(@StringRes stringResId: Int) = this.showContextToast(stringResId)

public fun Activity.setActionBarTitle(title:String){
    (this as? AppCompatActivity)?.supportActionBar?.title = title
}

