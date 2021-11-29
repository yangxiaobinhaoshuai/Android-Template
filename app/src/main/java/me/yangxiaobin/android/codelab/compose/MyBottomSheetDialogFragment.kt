package me.yangxiaobin.android.codelab.compose

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.DialogFragment
import me.yangxiaobin.android.codelab.R
import me.yangxiaobin.android.kotlin.codelab.base.AbsBottomSheetDialogFragment
import me.yangxiaobin.android.kotlin.codelab.ext.dp2px

class MyBottomSheetDialogFragment : AbsBottomSheetDialogFragment() {

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_my_bottom_sheet_dialog, container, false)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = hybridContentView()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { d ->
            d.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    private fun hybridContentView() = ComposeView(requireContext())
        .apply {
            setContent {
                Column {
                    AndroidEditTextView()
                    ComposeOperatorBar()
                }
            }
        }

    @Composable
    private fun AndroidEditTextView() = AndroidView(
        factory = { context: Context ->

//            EditText(context).apply {
//                this.layoutParams =
//                    ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                    )
//
//                this.hint = "I'm Android EditText."
//            }

            layoutInflater.inflate(R.layout.edit_text_panel, view as ViewGroup, false)
        }
    )

    @Composable
    private fun ComposeOperatorBar() = Row {

        Text("时间")

        Text("执行人")

        Text("发送")
    }

}
