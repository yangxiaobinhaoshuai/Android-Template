package me.yangxiaobin.android.kotlin.codelab.ext

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.postDelayed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow

/**
 * 弹起软键盘
 */
fun View.showKeyboard(delay: Long = 200L): View = apply {
    this.postDelayed(delay) {
        ViewCompat.getWindowInsetsController(this)?.show(WindowInsetsCompat.Type.ime())
    }
}


/**
 * 隐藏软键盘
 */
fun View.hideKeyboard(): View = apply {
    ViewCompat.getWindowInsetsController(this)?.hide(WindowInsetsCompat.Type.ime())
}

/**
 * 软键盘是否可见
 */
fun View.isKeyboardShown():Boolean =  ViewCompat.getRootWindowInsets(this)?.isVisible(WindowInsetsCompat.Type.ime()) ?: false


/**
 * 监控键盘是否弹起
 *
 * true for show
 *
 * @receiver Used to retrieve the window, Any view in the target window is ok.
 */
fun View?.getKeyboardVisibilityFLow(xMs: Long = 200L): Flow<Boolean> =
    if (this == null) emptyFlow()
    else flow {

        while (true) {

            delay(xMs)

            val isShown = ViewCompat.getRootWindowInsets(this@getKeyboardVisibilityFLow)
                ?.isVisible(WindowInsetsCompat.Type.ime())
                ?: false


            emit(isShown)
        }

    }.distinctUntilChanged()
