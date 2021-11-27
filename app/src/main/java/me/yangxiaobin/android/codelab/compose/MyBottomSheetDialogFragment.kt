package me.yangxiaobin.android.codelab.compose

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import me.yangxiaobin.android.kotlin.codelab.base.AbsBottomSheetDialogFragment
import me.yangxiaobin.android.kotlin.codelab.ext.dp2px

class MyBottomSheetDialogFragment : AbsBottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = hybridContentView()

    private fun hybridContentView() = ComposeView(requireContext())
        .apply {
            setContent {
                AndroidEditTextView()
            }
        }

    @Composable
    private fun AndroidEditTextView() = AndroidView(
        factory = { context: Context ->
            EditText(context).apply {
                this.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200.dp2px.toInt())

                this.hint = "I'm Android EditText."
            }
        }
    )

}
