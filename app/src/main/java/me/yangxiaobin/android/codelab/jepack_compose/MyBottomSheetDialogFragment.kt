package me.yangxiaobin.android.codelab.jepack_compose

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.foundation.background
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.yangxiaobin.android.codelab.R
import me.yangxiaobin.android.kotlin.codelab.base.AbsBottomSheetDialogFragment

class MyBottomSheetDialogFragment : AbsBottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = hybridContentView()


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
