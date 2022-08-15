 package me.yangxiaobin.android.kotlin.codelab.ext

import android.app.Activity
import android.graphics.Point
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

 /**
  * 弹起软键盘
  *
  * @see com.google.android.material.internal.ViewUtils.requestFocusAndShowKeyboard
  */
 fun View.showKeyboardWithDelay(delay: Long = 200L): Runnable {
     val r = Runnable { ViewCompat.getWindowInsetsController(this)?.show(WindowInsetsCompat.Type.ime()) }
     this.postDelayed(r, delay)
     return r
 }

 fun View.showKeyboardImmediately() = ViewCompat.getWindowInsetsController(this)?.show(WindowInsetsCompat.Type.ime())

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


 /**
  * Keyboard 高度通常都是 709
  */
 fun Activity.getKeyboardHeightFlow(logFunc: (message: String) -> Unit = { message: String -> android.util.Log.e("KeyboardHeightFlow", message) }): Flow<Int> {

     val transparentPopupWindow = PopupWindow()
     val popupContentView = View(this@getKeyboardHeightFlow)
     popupContentView.setBackgroundResource(android.R.color.transparent)


     fun handleGlobalLayoutChange(producerScope: ProducerScope<Int>) {
         val screenSize = Point()
         this@getKeyboardHeightFlow.windowManager.defaultDisplay.getSize(screenSize)
         val visibleR = Rect()
         popupContentView.getWindowVisibleDisplayFrame(visibleR)

         //val orientation: Int = this@getKeyboardHeightFlow.resources.configuration.orientation

         val keyboardHeight: Int = screenSize.y - visibleR.bottom

         // 姑且认为键盘高度 > 500 ,若小于 500 则不认为是键盘高度引起变化
         if (keyboardHeight > 500)  producerScope.trySend(keyboardHeight)
         else producerScope.trySend(0)

         logFunc.invoke("Keyboard height changed: $keyboardHeight, screenY: ${screenSize.y}, visibleRect.bottom: ${visibleR.bottom}")
     }

     with(transparentPopupWindow) {
         contentView = popupContentView
         softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
         inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
         width = 0
         height = WindowManager.LayoutParams.MATCH_PARENT
     }

     val parentView = this.findViewById<View>(android.R.id.content)

     if (!transparentPopupWindow.isShowing && parentView.windowToken != null) {
         transparentPopupWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0)
     } else logFunc.invoke("parentView windowToken is null, have the view be attached?")

    return callbackFlow {

         val listener = { handleGlobalLayoutChange(this) }

         popupContentView.viewTreeObserver.addOnGlobalLayoutListener(listener)

         awaitClose { popupContentView.viewTreeObserver.removeOnGlobalLayoutListener(listener) }
     }
        .distinctUntilChanged()
         // 去掉第一次的 0
        .drop(1)

 }
