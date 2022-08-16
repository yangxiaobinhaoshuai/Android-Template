package me.yangxiaobin.common_ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams
import me.yangxiaobin.android.kotlin.codelab.ext.WrapContent
import me.yangxiaobin.android.kotlin.codelab.ext.dp2px
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt

/**
 * Native ui button 页面
 */
open class EmptyFragment : AbsFragment() {

    protected open val buttonsCount: Int = 4

    private val topMargin =  100.dp2px.toInt()


    @SuppressLint("SetTextI18n")
    override fun createRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return (customRootViewGroup(requireContext()) ?: FrameLayout(requireContext()))
            .apply {
                this.layoutParams = MatchParentParams
                this.setBackgroundColor(getBackgroundColor())


                repeat(buttonsCount + customChildren().size) { index: Int ->

                    val lp = FrameLayout.LayoutParams(WrapContent, WrapContent)
                    lp.gravity = Gravity.CENTER_HORIZONTAL
                    lp.topMargin = if (index == 0) topMargin
                    else topMargin + (40 + 40).dp2px.toInt() * index


                    if (index < buttonsCount) {
                        val bt = Button(requireContext())
                            .apply {
                                this.text = "Button: $index"
                                this.setTextColor(HexColors.RED_A200.colorInt)
                            }

                        bt.setOnClickListener { this@EmptyFragment::onButtonClick.invoke(index,it) }

                        this.addView(bt, lp)
                    } else {
                        this.addView(customChildren()[index - buttonsCount], lp)
                    }
                }

            }
    }

    protected open fun onButtonClick(index: Int,v:View) = Unit

    @ColorInt protected open fun getBackgroundColor() = HexColors.YELLOW_A200.colorInt

    protected open fun customChildren(): List<View> = emptyList()

    protected open fun customRootViewGroup(context: Context): ViewGroup? = null
}
