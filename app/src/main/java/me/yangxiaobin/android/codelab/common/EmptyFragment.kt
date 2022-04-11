package me.yangxiaobin.android.codelab.common

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams
import me.yangxiaobin.android.kotlin.codelab.ext.WrapContent
import me.yangxiaobin.android.kotlin.codelab.ext.dp2px
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.toColor

open class EmptyFragment : AbsFragment() {

    protected open val buttonsCount: Int = 4


    @SuppressLint("SetTextI18n")
    override fun createRootView(): View {
        return FrameLayout(requireContext())
            .apply {
                this.layoutParams = MatchParentParams
                this.setBackgroundColor(HexColors.YELLOW_A200.toColor)


                repeat(buttonsCount) { index: Int ->

                    val bt = Button(requireContext())
                        .apply {
                            val lp = FrameLayout.LayoutParams(WrapContent, WrapContent)
                            lp.gravity = Gravity.CENTER_HORIZONTAL
                            lp.topMargin = if (index == 0) 100.dp2px.toInt()
                            else 100.dp2px.toInt() + (40 + 40).dp2px.toInt() * index

                            this.layoutParams = lp
                            this.text = "Button: $index"
                        }

                    this.addView(bt)
                }

            }
    }
}
