package me.yangxiaobin.image_edit.drawable

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import me.yangxiaobin.android.kotlin.codelab.base.AbsFragment
import me.yangxiaobin.android.kotlin.codelab.base.ability.LogAbility
import me.yangxiaobin.android.kotlin.codelab.ext.MarginLayoutParams
import me.yangxiaobin.android.kotlin.codelab.ext.MatchParentParams
import me.yangxiaobin.android.kotlin.codelab.ext.NormalLayoutParams
import me.yangxiaobin.android.kotlin.codelab.ext.context.rotated
import me.yangxiaobin.android.kotlin.codelab.ext.context.toResDrawable
import me.yangxiaobin.android.kotlin.codelab.log.AndroidLogger
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.image_edit.R
import me.yangxiaobin.logger.core.LogFacade

class DrawableFragment : AbsFragment() {

    override val logger: LogFacade get() = AndroidLogger

    override val LogAbility.TAG: String get() = "DrawableFragment"

    override fun createRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FrameLayout(requireContext()).apply {
        this.layoutParams = MatchParentParams
        //this.setWillNotDraw(false)
        this.setBackgroundColor(HexColors.BLUE_400.colorInt)

        val imageView = ImageView(requireContext())
        val imageLp = FrameLayout.LayoutParams(200, 300)
        imageLp.gravity = Gravity.CENTER

        //imageView.setImageResource(R.drawable.ic_android_logo)
        val d = R.drawable.ic_android_logo.toResDrawable(requireContext())
        imageView.setImageDrawable(d.rotated(90F))

        this.addView(imageView, imageLp)
    }

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

    }

}