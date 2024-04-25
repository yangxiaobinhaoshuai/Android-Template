package me.yangxiaobin.common_ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt

abstract class TemplateFragment : AbsFragment() {

    override fun createRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return (customRootViewGroup(requireContext()) ?: getRootContainer(requireContext()))
            .apply {
                this.layoutParams = MatchParentParams
                this.setBackgroundColor(getBackgroundColor())

                // Fill your layout here.
                fillLayout(this)
            }
    }
    abstract fun fillLayout(parent:ViewGroup)

    protected open fun getRootContainer(context: Context) = FrameLayout(context)

    protected open fun customRootViewGroup(context: Context): ViewGroup? = null

    @ColorInt
    protected open fun getBackgroundColor() = HexColors.YELLOW_A200.colorInt
}
