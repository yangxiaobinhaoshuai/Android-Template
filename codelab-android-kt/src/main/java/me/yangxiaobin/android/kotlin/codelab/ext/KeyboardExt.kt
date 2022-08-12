 package me.yangxiaobin.android.kotlin.codelab.ext

import android.app.Activity
import android.view.View
import android.widget.PopupWindow
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.postDelayed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

 /**
  * 弹起软键盘
  */
 fun View.showKeyboardWithDelay(delay: Long = 200L) {
     this.postDelayed(delay) {
         ViewCompat.getWindowInsetsController(this)?.show(WindowInsetsCompat.Type.ime())
     }
 }

 /**
  * 隐藏软键盘
  */
fun View.hideKeyboard() = ViewCompat.getWindowInsetsController(this)?.hide(WindowInsetsCompat.Type.ime())

/**
 * 软键盘是否可见
 */
fun View.isKeyboardShown(): Boolean = ViewCompat.getRootWindowInsets(this)?.isVisible(WindowInsetsCompat.Type.ime()) ?: false


/**
 * 监控键盘是否弹起
 *
 * true for show
 *
 * @receiver Used to retrieve the window, Any view in the target window is ok.
 */
fun View?.getKeyboardVisibilityFLow(xMs: Long = 100L): Flow<Boolean> =
    if (this == null) emptyFlow()
    else flow {

        withContext(Dispatchers.IO){
            while (true) {

                delay(xMs)

                val isShown = ViewCompat.getRootWindowInsets(this@getKeyboardVisibilityFLow)
                    ?.isVisible(WindowInsetsCompat.Type.ime())
                    ?: false

                emit(isShown)
            }
        }

    }.distinctUntilChanged()


 fun Activity.getKeyboardHeightFlow(): Flow<Int> = flow {
     val transparentPopupWindow = PopupWindow()
     // TODO
 }
