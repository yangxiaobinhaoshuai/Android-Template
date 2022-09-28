package me.yangxiaobin.android.embedding_compat

import android.view.View
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt
import me.yangxiaobin.common_ui.EmptyFragment

class FragmentA : EmptyFragment() {

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        view.setBackgroundColor(HexColors.BLUE_200.colorInt)
    }
}
