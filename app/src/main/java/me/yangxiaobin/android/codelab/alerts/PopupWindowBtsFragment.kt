package me.yangxiaobin.android.codelab.alerts

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.doOnPreDraw
import me.yangxiaobin.android.codelab.common.EmptyFragment
import me.yangxiaobin.android.kotlin.codelab.ext.WrapContent
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.toColor

class PopupWindowBtsFragment : EmptyFragment() {

    private lateinit var pop: PopupWindow


    @SuppressLint("SetTextI18n")
    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        pop = PopupWindow()
        pop.contentView = TextView(requireContext()).apply {
            this.text = "I'm PopupWindow Text."
            this.setBackgroundColor(HexColors.BLUE_A200.toColor)
        }
        pop.height = 200
        pop.width = 600


        var offset = 100
        var preIndex = -1

        (view as ViewGroup).children.forEachIndexed { index, v ->
            v.setOnClickListener {
                pop.dismiss()
                if (preIndex != index) offset = 100
                when (index) {
                    0 -> pop.showAtLocation(view, Gravity.NO_GRAVITY, offset, offset)
                    1 -> pop.showAtLocation(view, Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, offset, offset)
                    2 -> pop.showAsDropDown(v)
                    3 -> pop.showAsDropDown(v, offset, offset)
                    else -> Unit
                }
                offset += 100
                preIndex = index
            }

        }


        val bt = Button(requireContext())
        bt.text = "Edge Button"
        (view as ViewGroup).addView(bt)
        bt.doOnPreDraw {
            val lp = it.layoutParams as FrameLayout.LayoutParams
            lp.gravity = Gravity.END
            lp.width = WrapContent
            lp.height = WrapContent
            it.layoutParams = lp
        }
        bt.setOnClickListener {
            pop.showAsDropDown(it, -600, -20)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        pop.dismiss()
    }

}
