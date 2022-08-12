package me.yangxiaobin.kotlin.compose.lib

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import me.yangxiaobin.android.kotlin.codelab.base.AbsBottomSheetDialogFragment
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams

open class AbsComposableBsdFragment : AbsBottomSheetDialogFragment() {


    override fun createRoot(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext())
            .apply {
                layoutParams = MatchParentParams
                setBackgroundColor(Color.WHITE)
                isClickable = true
                setContent {
                    MaterialTheme {
                        composableContent.invoke()
                    }
                }
            }
    }


    open val composableContent: @Composable () -> Unit = {}
}
