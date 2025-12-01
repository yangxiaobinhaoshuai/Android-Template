package com.yangxiaobin.route_sample

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import me.yangxiaobin.android.kotlin.codelab.base.ability.nav.sendNavResult
import me.yangxiaobin.common_ui.EmptyFragment

class JumpTargetFragment : EmptyFragment() {

    override fun afterViewCreated(view: View) {
        super.afterViewCreated(view)
        logD("JumpTargetFragment arguments: $arguments")
    }

    override fun getRootContainer(context: Context): FrameLayout {
        val fl = FrameLayout(context)
        fl.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        fl.setBackgroundColor(Color.YELLOW)

        sendNavResult(bundleOf("result" to "23243423"))
        return fl
    }

}