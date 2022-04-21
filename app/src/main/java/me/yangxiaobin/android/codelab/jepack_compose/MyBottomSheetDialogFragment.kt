package me.yangxiaobin.android.codelab.jepack_compose

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import me.yangxiaobin.android.codelab.R
import me.yangxiaobin.android.kotlin.codelab.base.AbsBottomSheetDialogFragment
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.getKeyboardVisibilityFLow
import me.yangxiaobin.android.kotlin.codelab.ext.launchIn
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.kotlin.codelab.ext.curThread
import me.yangxiaobin.logger.core.LogFacade

class MyBottomSheetDialogFragment : AbsBottomSheetDialogFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "BottomSheetDialog"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = hybridContentView()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.getKeyboardVisibilityFLow()
            // 丢掉第一次 false
            .drop(1)
            .onEach {
                logD("keyboard visibility :$it, cur t :${curThread.name}.")
            }
            .flowOn(Dispatchers.Main)
            .launchIn(lifecycleScope, Dispatchers.IO)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { d ->

            d.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            d.setCanceledOnTouchOutside(true)

            disableSwipeDownDismiss(d)
        }
    }

    /**
     * 禁止下拉拖拽消失
     */
    private fun disableSwipeDownDismiss(d: Dialog) {

        val behaviour = (d as BottomSheetDialog).behavior
        behaviour.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun hybridContentView() = ComposeView(requireContext())
        .apply {
            setContent {

                Surface(
                    Modifier
                        .padding(top = 200.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    color = Color.Green,
                    shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp),
                ) {
                    Column {
                        AndroidEditTextView()
                        ComposeOperatorBar()
                    }
                }

            }
        }

    @Composable
    private fun AndroidEditTextView() = AndroidView(
        factory = { _: Context ->

            layoutInflater.inflate(R.layout.edit_text_panel, view as ViewGroup, false)

        },

        update = {

        }
    )

    @Composable
    private fun ComposeOperatorBar() = Row(
        modifier = Modifier
    ) {

        Text("时间")

        Text("执行人")

        Text("发送")
    }

}
