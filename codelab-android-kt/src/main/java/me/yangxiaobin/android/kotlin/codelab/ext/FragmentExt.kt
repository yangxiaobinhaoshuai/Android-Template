package me.yangxiaobin.android.kotlin.codelab.ext

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.showFragmentToast(text: String) = this.requireContext().showContextToast(text)

fun Fragment.showFragmentToast(@StringRes stringResId: Int) = this.requireContext().showContextToast(stringResId)
