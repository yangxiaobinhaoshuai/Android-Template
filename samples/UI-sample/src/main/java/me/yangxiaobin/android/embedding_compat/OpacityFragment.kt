package me.yangxiaobin.android.embedding_compat

import android.view.View
import me.yangxiaobin.android.kotlin.codelab.ext.uiwidget.setOpacity
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.common_ui.EmptyFragment

class OpacityFragment : EmptyFragment() {

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        view.setBackgroundColor(HexColors.BLUE_200.colorInt)
    }

    override fun onButtonClick(index: Int, v: View) {
        super.onButtonClick(index, v)

        when (index) {
            0 -> v.setOpacity(10)
            1 -> v.setOpacity(30)
            2 -> v.setOpacity(60)
            else -> Unit
        }
    }
}
