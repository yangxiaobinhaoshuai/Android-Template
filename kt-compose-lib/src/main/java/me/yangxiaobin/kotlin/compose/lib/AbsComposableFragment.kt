package me.yangxiaobin.kotlin.compose.lib

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams

open class AbsComposableFragment : AbsFragment() {

    override fun createRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext())
        .apply {
            this.layoutParams = MatchParentParams
            this.setBackgroundColor(Color.WHITE)
            this.isClickable = true
            setContent {
                MaterialTheme {
                    composableContent.invoke()
                }
            }
        }

    open val composableContent: @Composable () -> Unit = {}

}
