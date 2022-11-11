package me.yangxiaobin.kotlin.compose.lib

import android.view.View
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import me.yangxiaobin.android.kotlin.codelab.base.AbsActivity

abstract class AbsComposableActivity : AbsActivity() {

    override fun getRootView(): View = ComposeView(this).apply {
        setContent {
            MaterialTheme {
                composableContent.invoke()
            }
        }
    }

    open val composableContent: @Composable () -> Unit = {}

}