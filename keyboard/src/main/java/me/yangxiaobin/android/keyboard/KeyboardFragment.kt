package me.yangxiaobin.android.keyboard

import android.view.ViewGroup
import android.widget.EditText
import androidx.compose.foundation.background
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import me.yangxiaobin.android.kotlin.codelab.base.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParent
import me.yangxiaobin.android.kotlin.codelab.ext.dp2px
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.kotlin.compose.lib.AbsComposableFragment
import me.yangxiaobin.logger.core.LogFacade

class KeyboardFragment : AbsComposableFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "KeyboardHeight"


    override val composableContent = @Composable {
        Surface(Modifier.background(color = colorResource(id = android.R.color.holo_orange_light))) {
            AndroidEdt()
        }
    }


    @Composable
    private fun AndroidEdt() {
        AndroidView(
            factory = { context ->
                val edt = EditText(context)
                edt.setBackgroundColor(HexColors.BLUE_800.colorInt)
                edt.layoutParams = ViewGroup.LayoutParams(MatchParent, 60.dp2px.toInt())
                edt
            }
        )
    }

}
