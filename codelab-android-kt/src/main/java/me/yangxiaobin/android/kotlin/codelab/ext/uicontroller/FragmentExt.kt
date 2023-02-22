package me.yangxiaobin.android.kotlin.codelab.ext.uicontroller

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.showContextToast

public fun Fragment.showFragmentToast(text: String) = this.requireContext().showContextToast(text)

public fun Fragment.showFragmentToast(@StringRes stringResId: Int) = this.requireContext().showContextToast(stringResId)
