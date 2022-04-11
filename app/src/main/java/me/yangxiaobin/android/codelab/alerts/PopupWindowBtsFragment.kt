package me.yangxiaobin.android.codelab.alerts

import android.view.View
import android.widget.PopupWindow
import me.yangxiaobin.android.codelab.common.EmptyFragment

class PopupWindowBtsFragment :  EmptyFragment() {


    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)

        val pop = PopupWindow()
    }

}
