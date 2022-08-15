package me.yangxiaobin.android.keyboard

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.*
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.kotlin.compose.lib.AbsComposableBsdFragment
import me.yangxiaobin.logger.core.LogFacade

/**
 * 华为 / 鸿蒙 系统会遮住 edt 下面的布局
 * 其实是差了一个 statusBar 的高度
 */
class BottomSheetKeyboardFragment : AbsComposableBsdFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "Keyboard_BottomSheet"

    private val edtId = View.generateViewId()


    override val composableContent = @Composable {

        Surface(
            color = Color.Yellow
        ) {

            Column() {

                AndroidEdt()

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Text(text = "ABC")
                    Text(text = "123")
                }
            }

        }
    }

    @Composable
    fun AndroidEdt() {
        AndroidView(factory = { context ->
            val edt = EditText(context)
            edt.setBackgroundColor(HexColors.BLUE_900.colorInt)
            edt.id = edtId
            edt.layoutParams = ViewGroup.LayoutParams(MatchParent, 60.dp2px.toInt())
            edt
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
            .also {
                it.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                //it.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
    }


    override fun afterCreateView(view: View?) {
        super.afterCreateView(view)

        view ?: return

        val statusBarHeight = requireContext().statusBarSize

        val naviBar = requireContext().navigationBarSize.toFloat()

        val screenHeight = requireContext().screenSize.second

        logD(" status bar height :$statusBarHeight.")

        requireActivity()
            .getKeyboardHeightFlow(logFunc = { logD(it) })
            .onEach {  keyboardHeight ->
                //    BSDF keyboard height: 709
                //    requireView() height: 420
                //    edt height: 180
                //    screen height: 2098

                logD("""
                    BSDF keyboard height: $keyboardHeight
                    requireView() height: ${requireView().height}
                    edt height: ${requireView().findViewById<View>(edtId)?.height}
                    screen height: $screenHeight
                    status: $statusBarHeight
                    navi: $naviBar
                    view height: ${view.height}
                """.trimIndent())


                if (keyboardHeight.isPositive) {

                    //requireView().updateLayoutParams { this.height = it + view.height + statusBarHeight.toInt() }

                    requireView().setPadding(0,0,0, keyboardHeight + statusBarHeight)
                }
            }
            .launchIn(lifecycleScope)

    }


}
