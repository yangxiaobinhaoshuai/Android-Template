package me.yangxiaobin.android.ui.kit.samples

import android.graphics.drawable.ColorDrawable
import android.view.View
import me.yangxiaobin.android.kotlin.codelab.ext.androidColor
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.android.ui.kit.R
import me.yangxiaobin.android.ui.kit.createShapeDrawable
import me.yangxiaobin.android.ui.kit.setSelectorBackground
import me.yangxiaobin.android.ui.kit.setSelectorColor
import me.yangxiaobin.common_ui.EmptyFragment
import me.yangxiaobin.logger.core.LogFacade

class SelectorsFragment : EmptyFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "SelectorsFragment"

    override fun getBackgroundColor(): Int = androidColor.WHITE

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

    }

    override fun onButtonClick(index: Int, v: View) {
        super.onButtonClick(index, v)

        when (index) {
            0 -> v.setSelectorColor(
                onPressColor = androidColor.RED,
                onNormalColor = androidColor.BLUE,
                ignoreOriginalBackground = true
            )
            1 -> v.setSelectorBackground(
                onPressDrawable = createShapeDrawable(solidColor = androidColor.RED, radiusDp = 20),
                onNormalDrawable = ColorDrawable(androidColor.YELLOW),
                ignoreOriginalBackground = true
            )

            2 -> v.setSelectorBackground(
                onPressDrawable = createShapeDrawable(requireContext(), solidColorRes = R.color.md_purple_300, radiusDp = 20),
                onNormalDrawable = ColorDrawable(androidColor.YELLOW),
                ignoreOriginalBackground = true
            )
            else -> Unit
        }
    }
}
