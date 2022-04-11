package me.yangxiaobin.android.codelab.common

import android.view.View
import android.widget.FrameLayout
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.toColor

open class EmptyFragment : AbsFragment() {


    override fun createRootView(): View{
        return FrameLayout(requireContext())
            .apply {
                this.layoutParams = MatchParentParams
                this.setBackgroundColor(HexColors.YELLOW_A200.toColor)
            }
    }
}
