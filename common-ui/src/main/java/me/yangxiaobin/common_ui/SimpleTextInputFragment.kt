package me.yangxiaobin.common_ui

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import me.yangxiaobin.android.kotlin.codelab.ext.WrapContent
import me.yangxiaobin.android.kotlin.codelab.ext.dp2px
import me.yangxiaobin.colors.HexColors
import me.yangxiaobin.colors.colorInt

open class SimpleTextInputFragment : TemplateFragment() {

    protected lateinit var edit: EditText

    protected lateinit var tv1: TextView
    protected lateinit var tv2: TextView
    override fun fillLayout(parent: ViewGroup) {

        val ctx = requireContext()
        val image = ImageView(ctx)
        image.setBackgroundResource(R.drawable.ic_close_vector_black_round)

        edit = EditText(ctx)
        edit.hint = "Input here."
        edit.setBackgroundColor(HexColors.PINK_700.colorInt)
        image.setOnClickListener { edit.setText("") }

        val bt1 = Button(ctx)
        bt1.text = "Bt1"


        val bt2 = Button(ctx)
        bt2.text = "Bt2"


        tv1 = TextView(ctx)
        tv1.setBackgroundColor(HexColors.BLUE_600.colorInt)
        tv1.hint = "Tv1 here."
        tv1.text = ""

        tv2 = TextView(ctx)
        tv2.setBackgroundColor(HexColors.BLUE_600.colorInt)
        tv2.hint = "Tv2 here."
        tv2.text = ""

        bt1.setOnClickListener { onItemClick(bt1, 0) }
        bt2.setOnClickListener { onItemClick(bt2, 1) }

        val children = listOf<View>(image, edit, bt1, bt2, tv1, tv2)

        children.forEachIndexed { index, child ->
            val lp = FrameLayout.LayoutParams(WrapContent, WrapContent)
            lp.gravity = Gravity.CENTER_HORIZONTAL
            lp.topMargin = 120.dp2px.toInt() + 60.dp2px.toInt() * index
            child.layoutParams = lp
            parent.addView(child)

        }
    }

    open fun onItemClick(v: View, index: Int) {}

}
