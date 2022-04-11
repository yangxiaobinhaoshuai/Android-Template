package me.yangxiaobin.android.codelab.alerts

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.view.children
import me.yangxiaobin.android.codelab.common.EmptyFragment

class PopupWindowBtsFragment : EmptyFragment() {

    private lateinit var pop: PopupWindow


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        pop = PopupWindow()
        pop.contentView = TextView(requireContext()).apply { this.text = "I'm PopupWindow Text." }
        pop.height = 100
        pop.width = 200


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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        pop.dismiss()
    }

}
