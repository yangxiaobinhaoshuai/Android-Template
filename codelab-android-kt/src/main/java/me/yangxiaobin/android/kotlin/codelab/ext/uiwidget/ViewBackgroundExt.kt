package me.yangxiaobin.android.kotlin.codelab.ext.uiwidget

import android.view.View


/**
 * Set opacity
 * @param 40 i.e.
 */
fun View.setBackGroundOpacity(opacityInt: Int): View = apply {
    this.background?.alpha = (256 * opacityInt / 100F).toInt()
}

